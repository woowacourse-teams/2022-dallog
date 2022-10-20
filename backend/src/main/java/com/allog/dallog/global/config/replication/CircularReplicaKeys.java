package com.allog.dallog.global.config.replication;

import java.util.List;

public class CircularReplicaKeys {

    private static final int NEXT_INDEX = 1;

    private final List<DataSourceKey> dataSourceKeys;
    private final int size;

    private int cursor = 0;

    public CircularReplicaKeys() {
        this.dataSourceKeys = List.copyOf(DataSourceKey.getReplicas());
        this.size = dataSourceKeys.size();
    }

    public DataSourceKey next() {
        DataSourceKey dataSourceKey = dataSourceKeys.get(cursor);
        cursor = (cursor + NEXT_INDEX) % size;
        return dataSourceKey;
    }
}
