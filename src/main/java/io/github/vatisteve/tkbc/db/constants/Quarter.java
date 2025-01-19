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
public enum Quarter implements EnumResponse<String, String> {

    I   ("Quý I"),
    II  ("Quý II"),
    III ("Quý III"),
    IV  ("Quý IV");

    private final String label;

    @Override
    public String getValue() {
        return name();
    }

}
