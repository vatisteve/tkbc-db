package io.github.vatisteve.tkbc.db.generic;

import java.util.logging.Logger;

/**
 * @author tinhnv - Jan 19 2025
 */
public interface Statistic<T> {
    Logger log = Logger.getLogger("Statistic");
    T getValue();
    Class<T> getType();
    void sumNext(Statistic<?> other);
    void setValue(Object value);
}
