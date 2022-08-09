package com.allog.dallog.domain.subscription.domain;

import com.allog.dallog.domain.subscription.exception.InvalidSubscriptionException;
import java.util.Arrays;

public enum Color {

    COLOR_1("#FABEC0"),
    COLOR_2("#F85C70"),
    COLOR_3("#F37970"),
    COLOR_4("#E43D40"),
    COLOR_5("#F9D876"),
    COLOR_6("#FBE39D"),
    COLOR_7("#FEDA15"),
    COLOR_8("#CBAE11"),
    COLOR_9("#3D550C"),
    COLOR_10("#81B622"),
    COLOR_11("#ECF87F"),
    COLOR_12("#59981A"),
    COLOR_13("#145DA0"),
    COLOR_14("#0C2D48"),
    COLOR_15("#2E8BC0"),
    COLOR_16("#B1D4E0"),
    COLOR_17("#BEAFC2"),
    COLOR_18("#695E93"),
    COLOR_19("#8155BA"),
    COLOR_20("#E43480"),
    COLOR_21("#EAD4C0"),
    COLOR_22("#BBC4C2"),
    COLOR_23("#464033"),
    COLOR_24("#7E7C73");

    private final String colorCode;

    Color(final String colorCode) {
        this.colorCode = colorCode;
    }

    public static Color pickAny(ColorPickerStrategy strategy) {
        return Color.values()[strategy.pickNumber()];
    }

    public static Color from(final String colorCode) {
        return Arrays.stream(Color.values())
                .filter(color -> color.getColorCode().equals(colorCode))
                .findFirst()
                .orElseThrow(() -> new InvalidSubscriptionException("(" + colorCode + ")는 사용할 수 없는 색상입니다."));
    }

    public String getColorCode() {
        return colorCode;
    }
}
