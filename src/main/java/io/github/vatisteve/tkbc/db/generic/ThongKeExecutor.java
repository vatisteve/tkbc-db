package io.github.vatisteve.tkbc.db.generic;

import io.github.vatisteve.tkbc.db.model.ThongKeDto;
import io.github.vatisteve.tkbc.db.service.StoredProcedureThongKeExecutor.StoredProcedureParameterIn;

import java.io.Serializable;
import java.util.List;

/**
 * @author tinhnv - Jan 19 2025
 */
public interface ThongKeExecutor<I extends Serializable>  {
    <R extends ThongKeDto<I>> void execute(R cursor, List<StoredProcedureParameterIn<?>> parameters);
}
