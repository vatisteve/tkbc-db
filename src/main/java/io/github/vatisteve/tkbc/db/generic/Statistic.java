package io.github.vatisteve.tkbc.db.generic;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.function.Consumer;

/**
 * @author tinhnv - Jan 19 2025
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface Statistic<T> {

    T getValue();

    Class<T> getType();

    void sumNext(Statistic<T> other);

    void setValue(Object value);

    default Consumer<Object[]> useResultMapper() {
        return null;
    }

    default boolean joinPreviousGroup() {
        return true;
    }

    Statistic<T> newInstance();

}
