package io.github.vatisteve.tkbc.db.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author tinhnv - Jan 19 2025
 */
public interface ThongKeResponse {
    class ThongKeResponseImpl<I extends Serializable> extends ArrayList<ThongKeDto<I>> implements ThongKeResponse {
        private static final long serialVersionUID = 1L;
        public static <I extends Serializable> ThongKeResponseImpl<I> of(Collection<ThongKeDto<I>> statisticData) {
            ThongKeResponseImpl<I> tk = new ThongKeResponseImpl<>();
            tk.addAll(statisticData);
            return tk;
        }
    }
}
