package com.staticnur.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "datasource.acq.clients")
public class AcqDatabaseConfig extends HikariConfig{

    @Bean
    @LiquibaseDataSource
    public DataSource acqDataSource() {
        return new HikariDataSource(this);
    }

    @Bean
    public SpringLiquibase liquibase(DataSource acqDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(acqDataSource);
        liquibase.setChangeLog("classpath:db.changelog/changelog.xml");
        return liquibase;
    }
}

