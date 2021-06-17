package br.com.multidatasources.multidatasources.config.datasource;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public abstract class AbstractDataSourceConfiguration {

    public abstract String getPoolName();

    public abstract int getMaximunPoolSize();

    public abstract boolean getAutoCommit();

    public HikariDataSource definePoolDataSourceConnection(DataSource dataSource) {
        return new HikariDataSource(hikariConfig(dataSource));
    }

    private HikariConfig hikariConfig(DataSource dataSource) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName(getPoolName());
        hikariConfig.setMaximumPoolSize(getMaximunPoolSize());
        hikariConfig.setDataSource(dataSource);
        hikariConfig.setAutoCommit(getAutoCommit());

        return hikariConfig;
    }

}
