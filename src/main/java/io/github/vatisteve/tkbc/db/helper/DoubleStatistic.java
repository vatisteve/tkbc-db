package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;

import java.math.BigDecimal;
import java.util.logging.Level;

/**
 * @author tinhnv - Jan 19 2025
 */
public class DoubleStatistic implements Statistic<Double> {

    private double value;

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public Class<Double> getType() {
        return Double.class;
    }

    @Override
    public void sumNext(Statistic<?> other) {
        Object otherValue = other.getValue();
        if (otherValue instanceof Double) {
            value = BigDecimal.valueOf((Double) otherValue).add(BigDecimal.valueOf(value)).doubleValue();
        } else {
            log.log(Level.WARNING, () -> otherValue + " is not a Double");
        }
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Double) {
            this.value = (Double) value;
        } else {
            log.log(Level.WARNING, () -> value + " is not a Double");
        }
    }

}
