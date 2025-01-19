package io.github.vatisteve.tkbc.db.helper;

import io.github.vatisteve.tkbc.db.generic.Statistic;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author tinhnv - Jan 19 2025
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StatisticFactory {

    public static Collection<? extends Statistic<?>> createObjectStatistics(int size) {
        List<Statistic<?>> statistics = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            statistics.add(new ObjectStatistic());
        }
        return statistics;
    }

    public static Collection<? extends Statistic<?>> createLongStatistics(int size) {
        List<Statistic<?>> statistics = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            statistics.add(new LongStatistic());
        }
        return statistics;
    }

    public static Collection<? extends Statistic<?>> createDoubleStatistics(int size) {
        List<Statistic<?>> statistics = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            statistics.add(new DoubleStatistic());
        }
        return statistics;
    }

    public static Collection<? extends Statistic<?>> createStringStatistics(int size) {
        List<Statistic<?>> statistics = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            statistics.add(new StringStatistic());
        }
        return statistics;
    }

    public static <T extends Statistic<?>> void fillStatistics(List<T> statistics, T statistic, int number) {
        for (int i = 0; i < number; i++) {
            statistics.add(statistic);
        }
    }

    public static void fillObjectStatistics(List<Statistic<?>> statistics, int number) {
        statistics.addAll(createObjectStatistics(number));
    }

    public static void fillLongStatistics(List<Statistic<?>> statistics, int number) {
        statistics.addAll(createLongStatistics(number));
    }

    public static void fillDoubleStatistics(List<Statistic<?>> statistics, int number) {
        statistics.addAll(createDoubleStatistics(number));
    }

    public static void fillStringStatistics(List<Statistic<?>> statistics, int number) {
        statistics.addAll(createStringStatistics(number));
    }

}
