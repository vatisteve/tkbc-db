package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tinhnv - Jan 19 2025
 */
@Data
@Slf4j
public class StringStatistic implements Statistic<String> {

    private String value;

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public void sumNext(Statistic<String> other) {
        log.warn("String Statistic does not support sumNext operation");
    }

    @Override
    public void setValue(Object value) {
        if (value == null) return;
        if (value instanceof String) {
            this.value = (String) value;
        } else {
            this.value = value.toString();
        }
    }

    @Override
    public Statistic<String> newInstance() {
        return new StringStatistic();
    }

}
