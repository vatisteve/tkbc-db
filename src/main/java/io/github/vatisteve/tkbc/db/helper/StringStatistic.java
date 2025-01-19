package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import lombok.Data;

import java.util.logging.Level;

/**
 * @author tinhnv - Jan 19 2025
 */
@Data
public class StringStatistic implements Statistic<String> {

    private String value;

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public void sumNext(Statistic<?> other) {
        log.log(Level.INFO, "String Statistic does not support sumNext operation");
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            this.value = value.toString();
        }
    }

}
