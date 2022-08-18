package com.allog.dallog.domain.subscription.domain;

import com.allog.dallog.domain.subscription.exception.InvalidSubscriptionException;
import java.util.Arrays;

public enum Color {

    COLOR_1("#AD1457"),
    COLOR_2("#D81B60"),
    COLOR_3("#D50000"),
    COLOR_4("#E67C73"),
    COLOR_5("#F4511E"),
    COLOR_6("#EF6C00"),
    COLOR_7("#F09300"),
    COLOR_8("#F6BF26"),
    COLOR_9("#E4C441"),
    COLOR_10("#C0CA33"),
    COLOR_11("#7CB342"),
    COLOR_12("#33B679"),
    COLOR_13("#0B8043"),
    COLOR_14("#009688"),
    COLOR_15("#039BE5"),
    COLOR_16("#4285F4"),
    COLOR_17("#3F51B5"),
    COLOR_18("#7986CB"),
    COLOR_19("#B39DDB"),
    COLOR_20("#9E69AF"),
    COLOR_21("#8E24AA"),
    COLOR_22("#795548"),
    COLOR_23("#616161"),
    COLOR_24("#A79B8E");

    private final String colorCode;

    Color(final String colorCode) {
        this.colorCode = colorCode;
    }

    public static Color pickAny(ColorPickerStrategy strategy) {
        return Color.values()[strategy.pickNumber()];
    }

    public static Color from(final String colorCode) {
        return Arrays.stream(Color.values())
                .filter(color -> color.getColorCode().equals(colorCode.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new InvalidSubscriptionException("(" + colorCode + ")는 사용할 수 없는 색상입니다."));
    }

    public String getColorCode() {
        return colorCode;
    }
}
