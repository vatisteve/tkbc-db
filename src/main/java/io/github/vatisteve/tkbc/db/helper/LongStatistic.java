package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;

import java.util.logging.Level;

/**
 * @author tinhnv - Jan 19 2025
 */
public class LongStatistic implements Statistic<Long> {

    private long value;

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public Class<Long> getType() {
        return Long.class;
    }

    @Override
    public void sumNext(Statistic<?> other) {
        Object otherValue = other.getValue();
        if (otherValue instanceof Long) {
            value += (Long) otherValue;
        } else {
            log.log(Level.WARNING, () -> otherValue + " is not a Long");
        }
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Long) {
            this.value = (Long) value;
        } else {
            log.log(Level.WARNING, () -> value + " is not a Long");
        }
    }

}
