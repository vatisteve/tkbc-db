package io.github.vatisteve.tkbc.db.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author tinhnv - Jan 19 2025
 */
public interface StatisticResponseWrapper {

    class StatisticResponseWrapperImpl<I extends Serializable, T extends StatisticDto<I>> extends ArrayList<T> implements StatisticResponseWrapper {

        private static final long serialVersionUID = 1L;

        public static <I extends Serializable, T extends StatisticDto<I>> StatisticResponseWrapperImpl<I, T> of(Collection<T> statisticData) {
            StatisticResponseWrapperImpl<I, T> tk = new StatisticResponseWrapperImpl<>();
            tk.addAll(statisticData);
            return tk;
        }

    }

    static <I extends Serializable, T extends StatisticDto<I>, R extends StatisticResponseWrapperImpl<I, T>> Collector<T, ?, R> collectFor(Supplier<R> supplier) {
        return Collectors.collectingAndThen(Collectors.toList(), data -> {
            R result = supplier.get();
            result.addAll(data);
            return result;
        });
    }

}
