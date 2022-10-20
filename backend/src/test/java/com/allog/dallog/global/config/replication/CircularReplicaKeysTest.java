package com.allog.dallog.global.config.replication;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CircularReplicaKeysTest {

    @DisplayName("다음 키를 반환한다.")
    @CsvSource(value = {"0,REPLICA_1", "1,REPLICA_2", "2,REPLICA_1", "3,REPLICA_2", "4,REPLICA_1"})
    @ParameterizedTest
    void 다음_키를_반환한다(final int loopCount, final DataSourceKey expected) {
        // given
        CircularReplicaKeys circularReplicaKeys = new CircularReplicaKeys();

        // when
        for (int i = 0; i < loopCount; i++) {
            circularReplicaKeys.next();
        }

        DataSourceKey actual = circularReplicaKeys.next();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
