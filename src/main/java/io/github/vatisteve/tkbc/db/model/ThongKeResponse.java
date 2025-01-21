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
public interface ThongKeResponse {

    class ThongKeResponseImpl<I extends Serializable, T extends ThongKeDto<I>> extends ArrayList<T> implements ThongKeResponse {

        private static final long serialVersionUID = 1L;

        public static <I extends Serializable, T extends ThongKeDto<I>> ThongKeResponseImpl<I, T> of(Collection<T> statisticData) {
            ThongKeResponseImpl<I, T> tk = new ThongKeResponseImpl<>();
            tk.addAll(statisticData);
            return tk;
        }

    }

    static <I extends Serializable, T extends ThongKeDto<I>, R extends ThongKeResponseImpl<I, T>> Collector<T, ?, R> collectFor(Supplier<R> supplier) {
        return Collectors.collectingAndThen(Collectors.toList(), data -> {
            R result = supplier.get();
            result.addAll(data);
            return result;
        });
    }

}
