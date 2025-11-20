package io.github.vatisteve.tkbc.db.generic;

import io.github.vatisteve.tkbc.db.constants.Quarter;
import io.github.vatisteve.tkbc.db.constants.StatisticType;

import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.Temporal;

/**
 * Abstraction describing a statistical time request. Depending on {@link io.github.vatisteve.tkbc.db.constants.StatisticType}
 * implementations provide start/end boundaries in a concrete temporal type and
 * optional fields such as month, quarter or year.
 * @author tinhnv
 * @since Dec 27, 2023
 */
public interface StatisticalRequest<T, R extends Temporal> {

    StatisticType getStatisticType();

    // Type: OPTION
    T getStartDate();
    T getEndDate();

    // Type: QUARTER
    Quarter getQuarter();

    // Type: MONTH
    Month getMonth();

    // Type: YEAR
    int getYear();

    R getFrom();

    R getTo();

    default TimeDeterminer<R> getTimeDeterminer() {
        return new TimeDeterminer<>(getFrom(), getTo());
    }

    default ZoneId zoneId() {
        return ZoneId.systemDefault();
    }

    class MissingStatisticDataException extends RuntimeException {
        private static final long serialVersionUID = 3033812082679210302L;
        public MissingStatisticDataException(String objectName) {
            super(String.format("Missing %s value for statistical query!", objectName));
        }
    }

}
