# TKBC-DB

Lightweight statistics DTO + stored procedure executor for JPA

## Overview

TKBC-DB is a small library that helps you:

- Define strongly‑typed “statistic” cells that hold a value and know how to sum themselves.
- Build tree/flat DTOs that contain a model descriptor and an ordered list of statistics.
- Execute database stored procedures using JPA EntityManager and automatically map result sets back to the registered statistics.

The library focuses on reporting/analytics scenarios where you already have optimized SQL/stored procedures and you only need a clean, reusable way to transport and aggregate results in Java.

## Installation

Maven

```xml
<dependency>
  <groupId>io.github.vatisteve</groupId>
  <artifactId>tkbc-db</artifactId>
  <version>x.x.x</version><!--Sepcify latest version-->
</dependency>
```

You will also need JPA (Hibernate) and SLF4J in your application (see pom.xml in this repo for versions used during development).

## Core concepts

- `Statistic<T>` - A single cell of your report (e.g., totalCount, amount, custom object…). It exposes:

  - `getValue()`/`setValue(Object)`
  - `getType()`: `Class<T>`
  - `sumNext(Statistic<T>)`: accumulates another cell into this one
  - `newInstance()`: creates a new empty statistic of the same kind
  - Optional: `useResultMapper()`: `Consumer<Object[]>` for row‑level mapping when a statistic is populated from an Object[] row instead of a single scalar
  - Optional: `joinPreviousGroup()`: controls grouping for multi‑resultSet stored procedures (defaults to true)

- `StatisticDto<I>` - A data carrier for a row (or node) in your report. It holds:

  - `ModelInfo<I> model`: your descriptor (id, code, name)
  - `List<Statistic<?>> statistics`: ordered collection of cells
  - `List<StatisticDto<I>> children`: optional hierarchy

  Helpers:

  - `fillStatistics(Supplier<T>, int size)`: quickly fill with the same statistic type n times
  - `addChild(child)`/`sumNextStatistics(children)`: accumulates a child row into the current row

- `StoredProcedureStatisticExecutor` - Executes a stored procedure via JPA EntityManager and maps the result(s) back to your registered statistics.
- `StoredProcedureParameterIn<T>` - A simple IN parameter holder for stored procedures, with validation.
- Helpers: Built‑in numeric/object statistics live in helper package (`LongStatistic`, `IntegerStatistic`, `DoubleStatistic`, `BigDecimalStatistic`, `StringStatistic`, `ObjectStatistic`, `TriStateStatistic`) and utilities like `StatisticHelper.fillStatistics`.

### Normal usage example

- Statistic response model: holds a model descriptor and a list of statistics

    ```java
    public class ExampleStatisticModel extends StatisticDto<Long> {
        public ExampleStatisticModel(ExampleModel model) {
            super(model);
        }
    }
    ```

- Statistic response wrapper: collects results into a single response object

    ```java
    import java.io.Serial;
    import java.io.Serializable;
    
    public class ExampleStatisticResponse extends StatisticResponseWrapperImpl<Long, ExampleStatisticModel> implements Serializable {
        @Serial
        private static final long serialVersionUID = -1L;
    }
    ```

- Statistic request model (optional)

    ```java
    public class ExampleStatisticRequest extends InstantStatisticalRequest {
        private List<Long> modelIds = new ArrayList<>();
    
    }
    ```

- Statistic handler

    ```java
    @PersistenceContext private EntityManager entityManager;
    
    public ExampleStatisticResponse handle(ExampleStatisticRequest query, String storedProcedureName, int numberOfStatistic) {
        StoredProcedureStatisticExecutor executor = new StoredProcedureStatisticExecutor(storedProcedureName, entityManager);
    
        TimeDeterminer<Instant> timeDeterminer = query.getTimeDeterminer();
    
        // Accumulator (a summary/total row)
        ExampleStatisticModel totalRow = new ExampleStatisticModel(ExampleModel.builder().name("SUM").build());
    
        ExampleStatisticResponse response = query.getModelIds().stream().map(modelId -> {
            // 1) Build stored procedure parameters
            List<StatisticParameter> params = List.of(
                new StoredProcedureParameterIn<>("MODEL_ID", modelId),
                new StoredProcedureParameterIn<>("START_TIME", timeDeterminer.getFrom()),
                new StoredProcedureParameterIn<>("END_TIME", timeDeterminer.getTo())
            );
    
            // 2) Resolve the model (your domain data, e.g., organization)
            ExampleModel model = getModel(modelId);
            if (donVi == null) {
                return null; // Skips this row
            }
    
            // 3) Prepare a DTO row and pre-register statistics
            ExampleStatisicModel row = new ExampleStatisticResponse(model);
            // Default all statistic cells to Long
            StatisticHelper.fillStatistics(row.getStatistics(), numberOfStatistic, LongStatistic::new);
    
            // 4) Execute the stored procedure to fill values in order
            executor.execute(row, params);
    
            // 5) Accumulate into the total row
            totalRow.sumNextStatistics(row.getStatistics());
            return row;
        }).collect(StatisticResponseWrapper.collectFor(ExampleStatisticResponse::new));
    
        response.add(totalRow);
        return response;
    }
    ```

- Overriding some columns’ types (e.g., amounts as Double)

    If the stored procedure returns mixed types (e.g., mostly counts as Long, but money columns as Double), initialize everything to Long and then replace specific indices:

    ```java
    @Override
    private void fillStatistics(List<Statistic<?>> statistics, int numberOfStatistic) {
        // default Longs
        StatisticHelper.fillStatistics(statistics, numberOfStatistic, LongStatistic::new);
        // columns 7, 9, 11 (0‑based: 6, 8, 10) are amounts
        statistics.set(6, new DoubleStatistic());
        statistics.set(8, new DoubleStatistic());
        statistics.set(10, new DoubleStatistic());
    }
    ```

    **Important**
    
    - The order of registered statistics must match the order returned by the stored procedure for that result set.
    - The executor groups contiguous statistics by type and by whether they use a row mapper (useResultMapper() != null). Each group expects one JDBC result set in sequence. If groups > result sets, a warning/error is logged and remaining groups are skipped.

- Using a custom statistic object (row‑mapper style)

    Sometimes one statistic cell represents a structured DTO built from a single row (Object[]) instead of a single scalar.
    
    ```java
    public class CustomStatistic implements Statistic<CustomStatistic.Value> {
    
        @lombok.Data
        public static class Value {
            private int year;
            private long data1;
            private long data2;
        }
    
        private final Value value = new Value();
    
        @Override public Value getValue() { return value; }
        @Override public Class<Value> getType() { return Value.class; }
        @Override public Statistic<Value> newInstance() { return new TinhHinhXuLyDonDto(); }
    
        @Override
        public void sumNext(Statistic<Value> other) {
            // Implement if you need aggregation across rows
            this.value.data1 += other.getValue().data1;
            this.value.data2 += other.getValue().data2;
        }
    
        @Override
        public void setValue(Object o) {
            if (o instanceof Value v) {
                this.value.year = v.year;
                this.value.data1 = v.data1;
                this.value.data2 = v.data2;
            } else {
                log.warn("{} cannot be assigned to CustomStatistic", o);
            }
        }
    
        @Override
        public function.Consumer<Object[]> useResultMapper() {
            return row -> {
                try {
                    value.setYear((Integer) row[0]);
                    value.setData1(((java.math.BigInteger) row[1]).longValue());
                    value.setData2(((java.math.BigInteger) row[2]).longValue());
                } catch (Exception e) {
                    log.error("Failed to map row data to CustomStatistic: {}", e.getMessage());
                }
            };
        }
    }
    ```

- Using the custom statistic in a handler

    ```java
    public ExampleStatisticResponse handle(CustomStatisticQuery query) {
        final int limitYears = 10; // example guard
        int nrYear = query.getToYear() - query.getFromYear() + 1;
        if (nrYear > limitYears) {
            query.setToYear(query.getFromYear() + limitYears);
            nrYear = limitYears;
        }
    
        List<Long> modelIds = Objects.requireNonNullElse(query.getModelIds(), Collections.emptyList());
        ExampleStatisticResponse result = new ExampleStatisticResponse();
    
        StoredProcedureStatisticExecutor executor = new StoredProcedureStatisticExecutor("STORED_PROCEDURE_NAME", entityManager);
    
        for (Long modelId : modelIds) {
            ExampleModel model = getModel(modelId);
            if (donVi == null) {
                continue;
            }
    
            // 1) Prepare DTO row and register N custom statistics
            ExampleStatisticModel statisticModel = new ExampleStatisticModel(model);
            statisticModel.fillStatistics(CustomStatistic::new, nrYear);
    
            // 2) Build parameters
            List<StatisticParameter> params = List.of(
                new StoredProcedureParameterIn<>("MODEL_ID", modelId),
                new StoredProcedureParameterIn<>("FROM", query.getFromYear()),
                new StoredProcedureParameterIn<>("NR_YEAR", nrYear)
            );
    
            // 3) Execute and collect
            executor.execute(statisticModel, params);
            result.add(statisticModel);
        }
        return result;
    }
    ```

## How `StoredProcedureStatisticExecutor` maps data?

1) You prepare the target DTO and register statistics in order.
2) The executor splits your statistics into groups where each group shares:
   - same `getType()` class, and
   - same `useResultMapper()` presence (either all null or all non‑null).
3) For each group, it expects one JDBC result set from the stored procedure:
   - If `useResultMapper()` is non‑null: it treats the result as `List<Object[]>` and applies the mapper for each registered statistic (row‑wise).
   - Otherwise: it treats the result as `List<?>` and assigns each value to the corresponding statistic via `setValue(value)`.
4) If the database returns fewer/more rows than registered, a warning/error will be logged. Your code ignores extra rows; missing rows produce an error log, and your statistic may remain default.

## Parameters and validation

- `StoredProcedureParameterIn` validates inputs (name not blank, value not null). It exposes `getName()`, `getValue()`, `getType()`.
- `StoredProcedureStatisticExecutor` validates:
  - `procedureName` not empty
  - non‑null `EntityManager`
  - `execute(dto, parameters)`: `parameters` not null, `dto` not null, `dto.getStatistics()` not null
Failures will throw an IllegalArgumentException or a NullPointerException depending on Apache Commons Validate evaluation; tests in this repo accept either as RuntimeException.

## Tips and best practices

- Always pre‑register statistics in the exact order that your stored procedure returns values (per result set).
- When mixing scalar and row‑mapped statistics, group them contiguously so that each group corresponds to a distinct result set.
- Prefer explicit numeric types (`LongStatistic`, `DoubleStatistic`, `BigDecimalStatistic`) matching your DB schema to avoid casting surprises.
- For totals, use `StatisticDto.sumNextStatistics` or `addChild` to aggregate children into parents.
- Log levels: enable TRACE to see detailed mapping info from the executor.

## FAQ

Q: Do I need to write custom mappers?

A: Only if your statistic uses a row DTO (Object[]) — then implement useResultMapper(). For simple scalar columns, setValue(Object) is enough and built‑in statistics already handle common types.

Q: How do multiple result sets work?

A: Grouping is automatic: contiguous statistics with the same type and same mapper presence form one group, and the executor consumes one result set per group from the stored procedure.

Q: Can I use it without stored procedures?

A: The executor in this module is stored procedure-oriented. You can still populate statistics manually or build another executor if needed.
