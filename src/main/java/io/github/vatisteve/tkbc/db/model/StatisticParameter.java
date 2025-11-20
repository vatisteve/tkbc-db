package io.github.vatisteve.tkbc.db.model;

/**
 * Marker for an input parameter used when executing statistics queries.
 * Provides the parameter name, runtime type and value.
 */
public interface StatisticParameter {
    String getName();
    Object getValue();
    Class<?> getType();
}
