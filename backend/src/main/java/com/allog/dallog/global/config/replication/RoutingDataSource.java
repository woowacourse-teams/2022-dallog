package com.allog.dallog.global.config.replication;

import static com.allog.dallog.global.config.replication.DataSourceKey.SOURCE;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private final CircularReplicaKeyList circularReplicaKeyList = new CircularReplicaKeyList();

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (isReadOnly) {
            return circularReplicaKeyList.next();
        }

        return SOURCE;
    }
}
