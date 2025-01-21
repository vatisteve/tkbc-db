package io.github.vatisteve.tkbc.db.service;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import io.github.vatisteve.tkbc.db.generic.ThongKeExecutor;
import io.github.vatisteve.tkbc.db.model.ThongKeDto;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.io.Serializable;
import java.util.List;

/**
 * @author tinhnv - Jan 19 2025
 */
@Slf4j
public abstract class StoredProcedureThongKeExecutor<I extends Serializable> implements ThongKeExecutor<I> {

    private final String procedureName;
    private final EntityManager entityManager;

    protected StoredProcedureThongKeExecutor(String procedureName, EntityManager entityManager) {
        Validate.notEmpty(procedureName, "procedureName must not be empty");
        Validate.notNull(entityManager, "entityManager must not be null");
        this.procedureName = procedureName;
        this.entityManager = entityManager;
    }

    private StoredProcedureQuery createStoredProcedureQuery(List<StoredProcedureParameterIn<?>> parameters) {
        StoredProcedureQuery sp = entityManager.createStoredProcedureQuery(procedureName);
        parameters.forEach(param -> {
            log.trace("Set Stored Procedure parameter [{}], with value [{}]", param.getName(), param.getValue());
            sp.registerStoredProcedureParameter(param.name, param.value.getClass(), ParameterMode.IN);
            sp.setParameter(param.name, param.value);
        });
        return sp;
    }

    @Override
    public <R extends ThongKeDto<I>> void execute(R cursor, List<StoredProcedureParameterIn<?>> parameters) {
        Validate.notNull(parameters, "parameters must not be null");
        Validate.notNull(cursor, "cursor must not be null");
        Validate.notNull(cursor.getStatistics(), "cursor statistics must not be null");
        StoredProcedureQuery sp = createStoredProcedureQuery(parameters);
        if (sp.execute()) {
            List<?> queriedStatistics = sp.getResultList();
            List<Statistic<?>> registeredStatistics = cursor.getStatistics();
            if (queriedStatistics.size() != registeredStatistics.size()) {
                log.warn("Queried statistics data is not the same size as the registered one [{}/{}]", queriedStatistics.size(), registeredStatistics.size());
            }
            for (int i = 0; i < registeredStatistics.size(); i++) {
                Statistic<?> stat = registeredStatistics.get(i);
                Object value = queriedStatistics.get(i);
                log.trace("Registered statistics index [{}] set value [{}] with type [{}]", i, value, stat.getType());
                stat.setValue(value);
            }
        } else {
            log.error("Execute store procedure [{}] failed", procedureName);
        }
    }

    @Value
    public static class StoredProcedureParameterIn<T> {
        String name;
        T value;
    }

}
