package io.github.vatisteve.tkbc.db.constants;

import io.github.vatisteve.tkbc.db.generic.EnumResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author tinhnv
 * @since Dec 27, 2023
 */
@Getter
@RequiredArgsConstructor
public enum StatisticType implements EnumResponse<String, String> {

    OPTION              ("Tùy chọn"),
    MONTH               ("Theo tháng"),
    QUARTER             ("Theo quý"),
    YEAR                ("Theo năm"),
    FIRST_SIX_MONTH     ("Sáu tháng đầu năm "),
    FIRST_NINE_MONTH    ("Chín tháng đầu năm");

    private final String label;

    @Override
    public String getValue() {
        return name();
    }
}
