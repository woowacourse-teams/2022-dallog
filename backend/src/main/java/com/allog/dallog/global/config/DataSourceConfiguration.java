package com.allog.dallog.global.config;

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Configuration
@Profile({"prod", "dev"})
public class DataSourceConfiguration {

    private static final String SOURCE_SERVER = "SOURCE";
    private static final String REPLICA_SERVER = "REPLICA";

    @Bean
    @Qualifier(SOURCE_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.source")
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(REPLICA_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.replica")
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(SOURCE_SERVER) DataSource sourceDataSource,
            @Qualifier(REPLICA_SERVER) DataSource replicaDataSource
    ) {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("source", sourceDataSource);
        dataSourceMap.put("replica", replicaDataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(sourceDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource determinedDataSource = routingDataSource(sourceDataSource(), replicaDataSource());
        return new LazyConnectionDataSourceProxy(determinedDataSource);
    }

    public static class RoutingDataSource extends AbstractRoutingDataSource {

        @Override
        protected Object determineCurrentLookupKey() {
            boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

            if (isReadOnly) {
                return "replica";
            }

            return "source";
        }
    }
}
