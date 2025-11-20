package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * Statistic implementation holding a BigDecimal value with safe accumulation and parsing.
 */
@Slf4j
public class BigDecimalStatistic implements Statistic<BigDecimal> {

    private BigDecimal value = BigDecimal.ZERO;

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public Class<BigDecimal> getType() {
        return BigDecimal.class;
    }

    @Override
    public void sumNext(Statistic<BigDecimal> other) {
        BigDecimal otherValue = other.getValue();
        if (otherValue != null) {
            value = value.add(otherValue);
        } else {
            log.warn("Null BigDecimal value detected");
        }
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof BigDecimal) {
            this.value = (BigDecimal) value;
        } else if (value instanceof Number) {
            this.value = new BigDecimal(value.toString());
        } else if (value instanceof String) {
            this.value = new BigDecimal((String) value);
        } else {
            log.warn("{} cannot be assigned to BigDecimal", value);
        }
    }

    @Override
    public Statistic<BigDecimal> newInstance() {
        return new BigDecimalStatistic();
    }

}
