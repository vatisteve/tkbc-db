package io.github.vatisteve.tkbc.db.generic;

import java.io.Serializable;

/**
 * @author tinhnv - Jan 19 2025
 */
public interface ModelInfo<I extends Serializable> {
    I getId();
    String getCode();
    String getName();
}
