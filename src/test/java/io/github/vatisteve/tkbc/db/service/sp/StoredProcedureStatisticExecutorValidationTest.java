package io.github.vatisteve.tkbc.db.service.sp;

import io.github.vatisteve.tkbc.db.generic.ModelInfo;
import io.github.vatisteve.tkbc.db.generic.Statistic;
import io.github.vatisteve.tkbc.db.model.StatisticDto;
import junit.framework.TestCase;
import lombok.Getter;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

/**
 * Validation-focused tests for StoredProcedureStatisticExecutor
 */
public class StoredProcedureStatisticExecutorValidationTest extends TestCase {

    private EntityManager dummyEntityManager() {
        return (EntityManager) Proxy.newProxyInstance(
                EntityManager.class.getClassLoader(),
                new Class[]{EntityManager.class},
                (proxy, method, args) -> { throw new UnsupportedOperationException("Not implemented in test"); }
        );
    }

    public void testConstructorRejectsEmptyProcedureName() {
        try {
            new StoredProcedureStatisticExecutor("", dummyEntityManager());
            fail("Expected IllegalArgumentException for empty procedure name");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    public void testConstructorRejectsNullEntityManager() {
        try {
            new StoredProcedureStatisticExecutor("proc", null);
            fail("Expected exception for null EntityManager");
        } catch (RuntimeException expected) {
            // Apache Validate may throw NPE for null argument, accept any runtime exception
        }
    }

    public void testExecuteRejectsNullParameters() {
        StoredProcedureStatisticExecutor exec = new StoredProcedureStatisticExecutor("proc", dummyEntityManager());
        StatisticDto<Long> dto = new StatisticDto<>(new SimpleModelInfo<>(1L, "C1", "Name1"));
        try {
            exec.execute(dto, null);
            fail("Expected exception for null parameters");
        } catch (RuntimeException expected) {
            // Apache Validate may throw NPE for null argument, accept any runtime exception
        }
    }

    public void testExecuteRejectsNullCursor() {
        StoredProcedureStatisticExecutor exec = new StoredProcedureStatisticExecutor("proc", dummyEntityManager());
        try {
            exec.execute(null, Collections.emptyList());
            fail("Expected exception for null cursor");
        } catch (RuntimeException expected) {
            // Apache Validate may throw NPE for null argument, accept any runtime exception
        }
    }

    public void testExecuteRejectsNullStatisticsFromCursor() {
        StoredProcedureStatisticExecutor exec = new StoredProcedureStatisticExecutor("proc", dummyEntityManager());
        NullStatisticDto<Long> dto = new NullStatisticDto<>(new SimpleModelInfo<>(2L, "C2", "Name2"));
        try {
            exec.execute(dto, Collections.emptyList());
            fail("Expected exception for null statistics");
        } catch (RuntimeException expected) {
            // Apache Validate may throw NPE for null argument, accept any runtime exception
        }
    }

    // --- Helpers ---

    @Getter
    private static class SimpleModelInfo<I extends Serializable> implements ModelInfo<I> {
        private final I id;
        private final String code;
        private final String name;

        private SimpleModelInfo(I id, String code, String name) {
            this.id = id;
            this.code = code;
            this.name = name;
        }

    }

    private static class NullStatisticDto<I extends Serializable> extends StatisticDto<I> {
        public NullStatisticDto(ModelInfo<I> model) {
            super(model);
        }

        @Override
        public List<Statistic<?>> getStatistics() {
            return null; // Simulate bad DTO state
        }
    }
}
