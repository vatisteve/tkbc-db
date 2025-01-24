package io.github.vatisteve.tkbc.db.query;

import io.github.vatisteve.tkbc.db.constants.Quarter;
import io.github.vatisteve.tkbc.db.constants.StatisticType;
import io.github.vatisteve.tkbc.db.generic.StatisticalRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.*;

/**
 * @author tinhnv - Jan 19 2025
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InstantStatisticalRequest implements StatisticalRequest<Instant, Instant> {

    private StatisticType statisticType;
    private Instant startDate;
    private Instant endDate;
    private Quarter quarter;
    private Month month;
    private int year;

    @Override
    public Instant getFrom() {
        if (statisticType == null) return toStartOfDay(Instant.now());
        YearMonth currentYearMonth = YearMonth.now(zoneId());
        if (year == 0) year = currentYearMonth.getYear();
        switch (statisticType) {
            case YEAR:
            case FIRST_SIX_MONTH:
            case FIRST_NINE_MONTH:
                return toStartOfDayAt1st(year, 1);
            case QUARTER: {
                if (quarter == null) throw new MissingStatisticDataException("quarter");
                switch (quarter) {
                    case I:
                        return toStartOfDayAt1st(year, 1);
                    case II:
                        return toStartOfDayAt1st(year, 4);
                    case III:
                        return toStartOfDayAt1st(year, 7);
                    case IV:
                        return toStartOfDayAt1st(year, 10);
                    default:
                        throw new MissingStatisticDataException(quarter.getValue());
                }
            }
            case MONTH: {
                if (month == null) month = currentYearMonth.getMonth();
                return toStartOfDayAt1st(year, month.getValue());
            }
            case OPTION: {
                if (startDate == null) startDate = Instant.now();
                return toStartOfDay(startDate);
            }
            default:
                throw new MissingStatisticDataException(statisticType.getValue());
        }
    }

    private Instant toStartOfDayAt1st(int year, int month) {
        return ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, zoneId()).toInstant();
    }

    private Instant toStartOfDay(Instant instant) {
        return instant.atZone(zoneId()).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant();
    }

    @Override
    public Instant getTo() {
        if (statisticType == null) return toEndOfDay(Instant.now());
        YearMonth currentYearMonth = YearMonth.now(zoneId());
        if (year == 0) year = currentYearMonth.getYear();
        switch (statisticType) {
            case YEAR:
                return toEndOfDay(year, 12, 31);
            case FIRST_SIX_MONTH:
                return toEndOfDay(year, 6, 30);
            case FIRST_NINE_MONTH:
                return toEndOfDay(year, 9, 30);
            case QUARTER: {
                if (quarter == null) throw new MissingStatisticDataException("quarter");
                switch (quarter) {
                    case I:
                        return toEndOfDay(year, 3, 31);
                    case II:
                        return toEndOfDay(year, 6, 30);
                    case III:
                        return toEndOfDay(year, 9, 30);
                    case IV:
                        return toEndOfDay(year, 12, 31);
                    default:
                        throw new MissingStatisticDataException(quarter.getValue());
                }
            }
            case MONTH: {
                int dayOfMonth;
                if (month == null) {
                    month = currentYearMonth.getMonth();
                    dayOfMonth = currentYearMonth.lengthOfMonth();
                } else {
                    dayOfMonth = YearMonth.of(year, month).lengthOfMonth();
                }
                return toEndOfDay(year, month.getValue(), dayOfMonth);
            }
            case OPTION: {
                if (startDate == null) startDate = Instant.now();
                return toEndOfDay(startDate);
            }
            default:
                throw new MissingStatisticDataException(statisticType.getValue());
        }
    }

    private Instant toEndOfDay(int year, int month, int dayOfMonth) {
        return ZonedDateTime.of(year, month, dayOfMonth, 23, 59, 59, 0, zoneId()).toInstant();
    }

    private Instant toEndOfDay(Instant instant) {
        return instant.atZone(zoneId()).withHour(23).withMinute(59).withSecond(59).withNano(0).toInstant();
    }

}
