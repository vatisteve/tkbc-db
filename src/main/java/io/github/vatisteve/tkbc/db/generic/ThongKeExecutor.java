package io.github.vatisteve.tkbc.db.generic;

import io.github.vatisteve.tkbc.db.model.ThongKeDto;
import io.github.vatisteve.tkbc.db.model.ThongKeParameter;

import java.io.Serializable;
import java.util.List;

/**
 * @author tinhnv - Jan 19 2025
 */
public interface ThongKeExecutor {
    <I extends Serializable, R extends ThongKeDto<I>, P extends ThongKeParameter> void execute(R dto, List<P> parameters);
}
