package io.github.vatisteve.tkbc.db.service.sp;

import io.github.vatisteve.tkbc.db.model.StatisticParameter;
import lombok.Getter;
import org.apache.commons.lang3.Validate;

/**
 * Simple IN parameter implementation for stored procedure executions.
 * Holds a non-null name and value; type is derived from the value's runtime class.
 */
@Getter
public class StoredProcedureParameterIn<T> implements StatisticParameter {

    private final String name;
    private final T value;

    public StoredProcedureParameterIn(String name, T value) {
        Validate.notBlank(name, "Parameter name cannot be blank");
        Validate.notNull(value, "Parameter value cannot be null");
        this.name = name;
        this.value = value;
    }

    @Override
    public Class<?> getType() {
        return value.getClass();
    }

}
