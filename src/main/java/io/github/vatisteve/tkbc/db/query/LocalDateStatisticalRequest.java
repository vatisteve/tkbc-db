package io.github.vatisteve.tkbc.db.query;

import io.github.vatisteve.tkbc.db.constants.Quarter;
import io.github.vatisteve.tkbc.db.constants.StatisticType;
import io.github.vatisteve.tkbc.db.generic.StatisticalRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * @author tinhnv
 * @since Jan 14, 2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalDateStatisticalRequest implements StatisticalRequest<String, LocalDateTime> {

    private StatisticType statisticType;
    private String startDate;
    private String endDate;
    private Quarter quarter;
    private Month month;
    private int year;

    @Override
    public LocalDateTime getFrom() {
        if (statisticType == null) return toStartOfDay(LocalDate.now());
        YearMonth currentYearMonth = YearMonth.now(zoneId());
        if (year == 0) year = currentYearMonth.getYear();
        switch (statisticType) {
            case YEAR:
            case FIRST_SIX_MONTH:
            case FIRST_NINE_MONTH: return toStartOfDay(LocalDate.of(year, 1, 1));
            case QUARTER: {
                if (quarter == null) throw new MissingStatisticDataException("quarter");
                switch (quarter) {
                    case I: return toStartOfDay(LocalDate.of(year, 1, 1));
                    case II: return toStartOfDay(LocalDate.of(year, 4, 1));
                    case III: return toStartOfDay(LocalDate.of(year, 7, 1));
                    case IV: return toStartOfDay(LocalDate.of(year, 10, 1));
                    default: throw new MissingStatisticDataException(quarter.getValue());
                }
            }
            case MONTH: {
                if (month == null) month = currentYearMonth.getMonth();
                return toStartOfDay(LocalDate.of(year, month.getValue(), 1));
            }
            case OPTION: {
                return toStartOfDay(getLocalDateFromRequestParam(startDate));
            }
            default: throw new MissingStatisticDataException(statisticType.getValue());
        }
    }

    @Override
    public LocalDateTime getTo() {
        if (statisticType == null) return toEndOfDay(LocalDate.now());
        YearMonth currentYearMonth = YearMonth.now(zoneId());
        if (year == 0) year = currentYearMonth.getYear();
        switch (statisticType) {
            case YEAR: return toEndOfDay(LocalDate.of(year, 12, 31));
            case FIRST_SIX_MONTH: return toEndOfDay(LocalDate.of(year, 6, 30));
            case FIRST_NINE_MONTH: return toEndOfDay(LocalDate.of(year, 9, 30));
            case QUARTER: {
                if (quarter == null) throw new MissingStatisticDataException("quarter");
                switch (quarter) {
                    case I: return toEndOfDay(LocalDate.of(year, 3, 31));
                    case II: return toEndOfDay(LocalDate.of(year, 6, 30));
                    case III: return toEndOfDay(LocalDate.of(year, 9, 30));
                    case IV: return toEndOfDay(LocalDate.of(year, 12, 31));
                    default: throw new MissingStatisticDataException(quarter.getValue());
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
                return toEndOfDay(LocalDate.of(year, month.getValue(), dayOfMonth));
            }
            case OPTION: {
                return toEndOfDay(getLocalDateFromRequestParam(endDate));
            }
            default: throw new MissingStatisticDataException(statisticType.getValue());
        }
    }

    private static LocalDateTime toStartOfDay(LocalDate localDate) {
        return localDate.atStartOfDay();
    }
    
    private static LocalDateTime toEndOfDay(LocalDate localDate) {
        return localDate.plusDays(1).atStartOfDay().minusNanos(1);
    }

    private static LocalDate getLocalDateFromRequestParam(String localDateAsString) {
        if (localDateAsString == null || localDateAsString.isEmpty()) return LocalDate.now();
        return LocalDate.parse(localDateAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
