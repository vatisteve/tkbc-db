package io.github.vatisteve.tkbc.db.service;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import io.github.vatisteve.tkbc.db.generic.ThongKeExecutor;
import io.github.vatisteve.tkbc.db.model.ThongKeDto;
import lombok.Value;
import org.apache.commons.lang3.Validate;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.io.Serializable;
import java.util.List;

/**
 * @author tinhnv - Jan 19 2025
 */
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
            sp.registerStoredProcedureParameter(param.name, param.value.getClass(), ParameterMode.IN);
            sp.setParameter(param.name, param.value);
        });
        return sp;
    }

    @Override
    public <R extends ThongKeDto<I>> void execute(R cursor, List<StoredProcedureParameterIn<?>> parameters) {
        StoredProcedureQuery sp = createStoredProcedureQuery(parameters);
        if (sp.execute()) {
            List<?> statistics = sp.getResultList();
            for (int i = 0; i < cursor.getStatistics().size(); i++) {
                Statistic<?> stat = cursor.getStatistics().get(i);
                stat.setValue(statistics.get(i));
                cursor.addStatistics(stat);
            }
        }
    }

    @Value
    public static class StoredProcedureParameterIn<T> {
        String name;
        T value;
    }

}
