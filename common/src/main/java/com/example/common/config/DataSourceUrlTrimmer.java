package com.example.common.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class DataSourceUrlTrimmer {

    private final DataSourceProperties dataSourceProperties;

    public DataSourceUrlTrimmer(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @PostConstruct
    public void trimJdbcUrl() {
        String rawUrl = dataSourceProperties.getUrl();
        String cleanUrl = rawUrl.replaceAll("\\s+", "");
        dataSourceProperties.setUrl(cleanUrl);
    }
}
