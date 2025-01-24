package io.github.vatisteve.tkbc.db.generic;

import io.github.vatisteve.tkbc.db.model.StatisticDto;
import io.github.vatisteve.tkbc.db.model.StatisticParameter;

import java.io.Serializable;
import java.util.List;

/**
 * @author tinhnv - Jan 19 2025
 */
public interface StatisticExecutor {
    <I extends Serializable, R extends StatisticDto<I>, P extends StatisticParameter> void execute(R dto, List<P> parameters);
}
