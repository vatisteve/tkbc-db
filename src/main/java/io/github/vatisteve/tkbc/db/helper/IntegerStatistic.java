package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tinhnv - Jan 22 2025
 */
@Slf4j
public class IntegerStatistic implements Statistic<Integer> {

    private int value;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public void sumNext(Statistic<Integer> other) {
        int otherValue = other.getValue() == null ? 0 : other.getValue();
        value += otherValue;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            this.value = ((Number) value).intValue();
        } else if (value instanceof String) {
            this.value = Integer.parseInt((String) value);
        } else {
            log.warn("{} cannot be assigned to Integer", value);
        }
    }

    @Override
    public Statistic<Integer> newInstance() {
        return new IntegerStatistic();
    }

}
