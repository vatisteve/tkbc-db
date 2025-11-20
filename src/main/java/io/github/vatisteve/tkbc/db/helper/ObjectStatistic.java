package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.Level;

/**
 * Generic statistic that holds an arbitrary Object value. No accumulation support.
 * @author tinhnv - Jan 19 2025
 */
@Data
@Slf4j
public class ObjectStatistic implements Statistic<Object> {

    private Object value;

    @Override
    public Class<Object> getType() {
        return Object.class;
    }

    @Override
    public void sumNext(Statistic<Object> other) {
        log.warn("Object Statistic does not support sumNext operation");
    }

    @Override
    public Statistic<Object> newInstance() {
        return new ObjectStatistic();
    }

}
