package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author tinhnv - Jan 19 2025
 */
@Slf4j
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
    public void sumNext(Statistic<Double> other) {
        double otherValue = other.getValue() == null ? 0D : other.getValue();
        value = BigDecimal.valueOf(otherValue).add(BigDecimal.valueOf(value)).doubleValue();
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            this.value = ((Number) value).doubleValue();
        } else if (value instanceof String) {
            this.value = Double.parseDouble(String.valueOf(value));
        } else {
            log.warn("{} cannot be assigned to Double", value);
        }
    }

}
