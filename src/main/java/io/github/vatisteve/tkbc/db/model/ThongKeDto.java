package io.github.vatisteve.tkbc.db.model;

import io.github.vatisteve.tkbc.db.generic.ModelInfo;
import io.github.vatisteve.tkbc.db.generic.Statistic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tinhnv
 * @since Jan 11, 2024
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"model","statistics"})
public class ThongKeDto<I extends Serializable> {

    private final ModelInfo<I> model;
    private final List<Statistic<?>> statistics;
    private List<ThongKeDto<I>> children = new ArrayList<>();

    public ThongKeDto(ModelInfo<I> model) {
        this.model = model;
        this.statistics = new ArrayList<>();
    }

    public void addStatistics(Statistic<?> statistic) {
        this.statistics.add(statistic);
    }

    public void addChild(ThongKeDto<I> child) {
        children.add(child);
        sum(this.statistics, child.statistics);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Statistic<?>> void sum(List<T> parentStatistics, List<T> childStatistics) {
        if (parentStatistics.isEmpty()) {
            parentStatistics.addAll(childStatistics);
        } else {
            for (int i = 0; i < childStatistics.size(); i++) {
                parentStatistics.get(i).sumNext((Statistic) childStatistics.get(i));
            }
        }
    }

    public static <I extends Serializable> ThongKeDto<I> of(ModelInfo<I> model, List<ThongKeDto<I>> children) {
        ThongKeDto<I> result = new ThongKeDto<>(model);
        children.forEach(c -> sum(result.statistics, c.statistics));
        return result;
    }

}
