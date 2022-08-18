package com.allog.dallog.domain.auth.domain;

import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.domain.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OAuthTokenTest {

    @DisplayName("OAuth token을 생성한다.")
    @Test
    void OAuth_token을_생성한다() {
        // given
        Member 매트 = 매트();
        String refreshToken = "adasaegsfadasdasfgfgrgredksgdffa";

        // when & then
        assertDoesNotThrow(() -> new OAuthToken(매트, refreshToken));
    }

    @DisplayName("refresh token을 교체한다.")
    @Test
    void refresh_token을_교체한다() {
        // given
        Member 매트 = 매트();
        String refreshToken = "adasaegsfadasdasfgfgrgredksgdffa";
        OAuthToken oAuthToken = new OAuthToken(매트, refreshToken);

        String updatedRefreshToken = "dfgsbnskjglnafgkajfnakfjgngejlkrqgn";

        // when
        oAuthToken.change(updatedRefreshToken);

        // then
        assertThat(oAuthToken.getRefreshToken()).isEqualTo(updatedRefreshToken);
    }
}
