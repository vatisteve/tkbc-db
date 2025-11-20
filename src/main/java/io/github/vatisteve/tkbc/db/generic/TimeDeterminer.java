package io.github.vatisteve.tkbc.db.generic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.temporal.Temporal;

/**
 * Simple holder for a time range (from/to) used when resolving statistical periods.
 * @author tinhnv - Jan 19 2025
 */
@Data
@AllArgsConstructor
@Builder
public class TimeDeterminer<T extends Temporal> {
    private T from;
    private T to;
}
