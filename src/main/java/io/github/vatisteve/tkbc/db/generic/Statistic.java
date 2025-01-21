package io.github.vatisteve.tkbc.db.generic;

/**
 * @author tinhnv - Jan 19 2025
 */
public interface Statistic<T> {
    T getValue();
    Class<T> getType();
    void sumNext(Statistic<T> other);
    void setValue(Object value);
}
