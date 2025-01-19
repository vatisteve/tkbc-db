package io.github.vatisteve.tkbc.db.generic;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * @author tinhnv
 * @since Dec 27, 2023
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface EnumResponse<L extends Serializable, V extends Serializable> {
    L getLabel();
    V getValue();
}
