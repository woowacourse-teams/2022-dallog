package com.allog.dallog.domain.composition.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.common.config.ExternalApiConfig;
import com.allog.dallog.domain.member.application.MemberAfterEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(ExternalApiConfig.class)
class MemberSaveAfterEventTest {

    @Autowired
    private MemberAfterEvent memberSaveAfterEvent;

    @DisplayName("잘못된 매개변수를 전달 받는 경우 예외를 던진다.")
    @Test
    void 잘못된_매개변수를_전달_받는_경우_예외를_던진다() {
        // given
        String name = "매트";

        // when & then
        assertThatThrownBy(() -> memberSaveAfterEvent.process(name))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
