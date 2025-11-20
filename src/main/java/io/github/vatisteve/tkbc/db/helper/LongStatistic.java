package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import lombok.extern.slf4j.Slf4j;

/**
 * Statistic implementation for long-based counters with accumulation and tolerant parsing.
 * @author tinhnv - Jan 19 2025
 */
@Slf4j
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
    public void sumNext(Statistic<Long> other) {
        long otherValue = other.getValue() == null ? 0L : other.getValue();
        value += otherValue;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            this.value = ((Number) value).longValue();
        } else if (value instanceof String) {
            this.value = Long.parseLong((String) value);
        } else {
            log.warn("{} cannot be assigned to Long", value);
        }
    }

    @Override
    public Statistic<Long> newInstance() {
        return new LongStatistic();
    }

}
