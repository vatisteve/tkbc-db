package io.github.vatisteve.tkbc.db.generic;

import java.io.Serializable;

/**
 * Minimal information describing a model/entity used as a grouping key in statistics.
 * Implementations typically carry id, code and human-readable name.
 * @author tinhnv - Jan 19 2025
 */
public interface ModelInfo<I extends Serializable> {
    I getId();
    String getCode();
    String getName();
}
