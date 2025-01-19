package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import lombok.Data;

import java.util.logging.Level;

/**
 * @author tinhnv - Jan 19 2025
 */
@Data
public class ObjectStatistic implements Statistic<Object> {

    private Object value;

    @Override
    public Class<Object> getType() {
        return Object.class;
    }

    @Override
    public void sumNext(Statistic<?> other) {
        log.log(Level.INFO, "Object Statistic does not support sumNext operation");
    }

}
