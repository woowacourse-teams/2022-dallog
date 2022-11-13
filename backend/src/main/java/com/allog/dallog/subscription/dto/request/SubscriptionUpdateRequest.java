package com.allog.dallog.subscription.dto.request;

import com.allog.dallog.subscription.domain.Color;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;

public class SubscriptionUpdateRequest {

    @NotBlank(message = "컬러 코드가 공백일 수 없습니다.")
    private String colorCode;
    private boolean checked;

    private SubscriptionUpdateRequest() {
    }

    public SubscriptionUpdateRequest(final Color color, final boolean checked) {
        this(color.getColorCode(), checked);
    }

    public SubscriptionUpdateRequest(final String colorCode, final boolean checked) {
        this.colorCode = colorCode;
        this.checked = checked;
    }

    public String getColorCode() {
        return colorCode;
    }

    @JsonIgnore
    public Color getColor() {
        return Color.from(colorCode);
    }

    public boolean isChecked() {
        return checked;
    }
}
