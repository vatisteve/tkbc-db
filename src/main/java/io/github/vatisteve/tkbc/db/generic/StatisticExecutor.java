package io.github.vatisteve.tkbc.db.generic;

import io.github.vatisteve.tkbc.db.model.StatisticDto;
import io.github.vatisteve.tkbc.db.model.StatisticParameter;

import java.io.Serializable;
import java.util.List;

/**
 * Abstraction responsible for executing a statistic query and populating a DTO
 * with values for its registered statistics.
 * Implementations may fetch data from different sources (e.g. Stored Procedures).
 *
 * @author tinhnv - Jan 19 2025
 */
public interface StatisticExecutor {
    <I extends Serializable, R extends StatisticDto<I>, P extends StatisticParameter> void execute(R dto, List<P> parameters);
}
