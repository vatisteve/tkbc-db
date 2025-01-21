package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author tinhnv - Jan 19 2025
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StatisticHelper {

    public static <T extends Statistic<?>> List<T> createStatistics(int size, Supplier<T> supplier) {
        List<T> statistics = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            statistics.add(supplier.get());
        }
        return statistics;
    }

    public static <T extends Statistic<?>> void fillStatistics(List<T> statistics, int size, Supplier<T> supplier) {
        for (int i = 0; i < size; i++) {
            statistics.add(supplier.get());
        }
    }

}
