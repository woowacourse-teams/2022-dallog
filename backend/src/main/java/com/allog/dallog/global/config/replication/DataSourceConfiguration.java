package com.allog.dallog.global.config.replication;

import static com.allog.dallog.global.config.replication.DataSourceKey.KeyName.REPLICA_1_NAME;
import static com.allog.dallog.global.config.replication.DataSourceKey.KeyName.REPLICA_2_NAME;
import static com.allog.dallog.global.config.replication.DataSourceKey.KeyName.SOURCE_NAME;
import static com.allog.dallog.global.config.replication.DataSourceKey.REPLICA_1;
import static com.allog.dallog.global.config.replication.DataSourceKey.REPLICA_2;
import static com.allog.dallog.global.config.replication.DataSourceKey.SOURCE;

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

@Configuration
@Profile({"prod", "dev"})
public class DataSourceConfiguration {

    @Bean
    @Qualifier(SOURCE_NAME)
    @ConfigurationProperties(prefix = "spring.datasource.source")
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(REPLICA_1_NAME)
    @ConfigurationProperties(prefix = "spring.datasource.replica1")
    public DataSource replica1DataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(REPLICA_2_NAME)
    @ConfigurationProperties(prefix = "spring.datasource.replica2")
    public DataSource replica2DataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(SOURCE_NAME) DataSource sourceDataSource,
            @Qualifier(REPLICA_1_NAME) DataSource replica1DataSource,
            @Qualifier(REPLICA_2_NAME) DataSource replica2DataSource
    ) {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(SOURCE, sourceDataSource);
        dataSourceMap.put(REPLICA_1, replica1DataSource);
        dataSourceMap.put(REPLICA_2, replica2DataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(sourceDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource determinedDataSource = routingDataSource(sourceDataSource(), replica1DataSource(),
                replica2DataSource());
        return new LazyConnectionDataSourceProxy(determinedDataSource);
    }
}
