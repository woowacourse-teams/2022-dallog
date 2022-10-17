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
    private static final String REPLICA_2_SERVER = "REPLICA2";

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
    @Qualifier(REPLICA_2_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.replica2")
    public DataSource replica2DataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(SOURCE_SERVER) DataSource sourceDataSource,
            @Qualifier(REPLICA_SERVER) DataSource replicaDataSource,
            @Qualifier(REPLICA_2_SERVER) DataSource replica2DataSource
    ) {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(SOURCE_SERVER, sourceDataSource);
        dataSourceMap.put(REPLICA_SERVER, replicaDataSource);
        dataSourceMap.put(REPLICA_2_SERVER, replica2DataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(sourceDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource determinedDataSource = routingDataSource(sourceDataSource(), replicaDataSource(),
                replica2DataSource());
        return new LazyConnectionDataSourceProxy(determinedDataSource);
    }

    public static class RoutingDataSource extends AbstractRoutingDataSource {

        private static boolean replica2 = false;

        @Override
        protected Object determineCurrentLookupKey() {
            boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

            if (isReadOnly) {
                if (RoutingDataSource.replica2) {
                    System.out.println("레플2" + RoutingDataSource.replica2);
                    RoutingDataSource.replica2 = false;
                    return REPLICA_2_SERVER;
                }

                System.out.println("레플1" + RoutingDataSource.replica2);
                RoutingDataSource.replica2 = true;
                return REPLICA_SERVER;
            }

            System.out.println("소스");
            return SOURCE_SERVER;
        }
    }
}
