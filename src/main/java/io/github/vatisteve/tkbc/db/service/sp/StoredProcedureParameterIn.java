package io.github.vatisteve.tkbc.db.service.sp;

import io.github.vatisteve.tkbc.db.model.StatisticParameter;
import lombok.Getter;
import org.apache.commons.lang3.Validate;

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
