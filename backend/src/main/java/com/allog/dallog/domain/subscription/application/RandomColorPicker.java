package com.allog.dallog.domain.subscription.application;

import com.allog.dallog.domain.subscription.domain.Color;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomColorPicker implements ColorPicker {

    @Override
    public int pickNumber() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        Color[] colors = Color.values();
        return threadLocalRandom.nextInt(colors.length);
    }
}
