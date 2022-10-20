package com.allog.dallog.global.config.replication;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomReplicaKeys {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    private final List<DataSourceKey> dataSourceKeys;
    private final int size;

    public RandomReplicaKeys() {
        this.dataSourceKeys = List.copyOf(DataSourceKey.getReplicas());
        this.size = dataSourceKeys.size();
    }

    public DataSourceKey next() {
        int currentDataSourceIndex = random.nextInt(size);
        return dataSourceKeys.get(currentDataSourceIndex);
    }
}
