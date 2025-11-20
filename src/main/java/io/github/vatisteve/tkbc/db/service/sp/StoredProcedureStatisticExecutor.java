package io.github.vatisteve.tkbc.db.service.sp;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import io.github.vatisteve.tkbc.db.generic.StatisticExecutor;
import io.github.vatisteve.tkbc.db.model.StatisticDto;
import io.github.vatisteve.tkbc.db.model.StatisticParameter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * StatisticExecutor implementation that reads statistics from a database Stored Procedure.
 * It registers IN parameters, executes the procedure and maps each result set row into
 * the registered {@link io.github.vatisteve.tkbc.db.generic.Statistic} instances.
 * Groups of statistics are split by type and whether a custom result mapper is used.
 * @author tinhnv - Jan 19 2025
 */
@Slf4j
public class StoredProcedureStatisticExecutor implements StatisticExecutor {

    private final String procedureName;
    private final EntityManager entityManager;

    public StoredProcedureStatisticExecutor(String procedureName, EntityManager entityManager) {
        Validate.notEmpty(procedureName, "procedureName must not be empty");
        Validate.notNull(entityManager, "entityManager must not be null");
        this.procedureName = procedureName;
        this.entityManager = entityManager;
    }

    private <P extends StatisticParameter> StoredProcedureQuery createStoredProcedureQuery(List<P> parameters) {
        StoredProcedureQuery sp = entityManager.createStoredProcedureQuery(procedureName);
        parameters.forEach(param -> {
            log.trace("Set Stored Procedure parameter [{}], with value [{}]", param.getName(), param.getValue());
            sp.registerStoredProcedureParameter(param.getName(), param.getType(), ParameterMode.IN);
            sp.setParameter(param.getName(), param.getValue());
        });
        return sp;
    }

    @Override
    public <I extends Serializable, R extends StatisticDto<I>, P extends StatisticParameter> void execute(R cursor, List<P> parameters) {
        Validate.notNull(parameters, "parameters must not be null");
        Validate.notNull(cursor, "cursor must not be null");
        Validate.notNull(cursor.getStatistics(), "cursor statistics must not be null");
        List<StatisticGroup> groups = splitStatisticsType(cursor.getStatistics());
        StoredProcedureQuery sp = createStoredProcedureQuery(parameters);
        if (sp.execute()) getResult(groups, sp);
        else log.error("Execute store procedure [{}] failed", procedureName);
    }

    @SuppressWarnings("unchecked")
    private static void getResult(List<StatisticGroup> groups, StoredProcedureQuery sp) {
        for (StatisticGroup group : groups) {
            List<Statistic<?>> registeredStatistics = group.getStatistics();
            if (group.useMapper) {
                List<Object[]> queriedStatistics = sp.getResultList();
                checkStatisticSizeAndLogWarning(group, queriedStatistics, registeredStatistics);
                for (int i = 0; i < registeredStatistics.size(); i++) {
                    Statistic<?> stat = registeredStatistics.get(i);
                    Object[] values = queriedStatistics.get(i);
                    log.trace("Registered statistics index [{}] set value with [{}] fields", i, values.length);
                    stat.useResultMapper().accept(values);
                }
            } else {
                List<?> queriedStatistics = sp.getResultList();
                checkStatisticSizeAndLogWarning(group, queriedStatistics, registeredStatistics);
                for (int i = 0; i < registeredStatistics.size(); i++) {
                    Statistic<?> stat = registeredStatistics.get(i);
                    Object value = queriedStatistics.get(i);
                    log.trace("Registered statistics index [{}] set value [{}] with type [{}]", i, value, stat.getType());
                    stat.setValue(value);
                }
            }
            if (!sp.hasMoreResults() && groups.size() > 1) {
                log.error("There is only one result set! Remaining {} statistics group(s) will be omitted", groups.size() - 1);
                break;
            }
        }
    }

    private static void checkStatisticSizeAndLogWarning(StatisticGroup group, List<?> queriedStatistics, List<Statistic<?>> registeredStatistics) {
        if (queriedStatistics.size() > registeredStatistics.size()) {
            log.warn("The queried statistics data result size is greater than the registered one [{}/{}] - Query group [{}] ",
                    queriedStatistics.size(), registeredStatistics.size(), group.getType());
        } else if (queriedStatistics.size() < registeredStatistics.size()) {
            log.error("The queried statistics data result size is less than the registered one [{}/{}] - Query group [{}]",
                    queriedStatistics.size(), registeredStatistics.size(), group.getType());
        }
    }

    private List<StatisticGroup> splitStatisticsType(List<Statistic<?>> statistics) {
        List<StatisticGroup> groups = new ArrayList<>();
        if (statistics.isEmpty()) return groups;
        Statistic<?> first = statistics.get(0);
        StatisticGroup group = new StatisticGroup(first);
        group.add(first);
        for (int i = 1; i < statistics.size(); i++) {
            Statistic<?> current = statistics.get(i);
            if (current.joinPreviousGroup()) {
                group.getStatistics().add(current);
            } else {
                groups.add(group);
                group = new StatisticGroup(current);
                group.add(current);
            }
        }
        groups.add(group);
        return groups;
    }

    @Value
    private static class StatisticGroup {
        Class<?> type;
        boolean useMapper;
        List<Statistic<?>> statistics = new ArrayList<>();
        public StatisticGroup(Statistic<?> init) {
            this.type = init.getType();
            this.useMapper = init.useResultMapper() != null;
        }
        public void add(Statistic<?> statistic) {
            statistics.add(statistic);
        }
    }

}
