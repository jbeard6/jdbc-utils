package net.josephbeard.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;

/**
 * Adapter for a {@link DataSource} to a {@link ConnectionProvider}.
 */
public final class DataSourceConnectionProvider implements ConnectionProvider {

    private final DataSource dataSource;

    public DataSourceConnectionProvider(DataSource dataSource) {
        Validate.notNull(dataSource, "The dataSource must not be null");
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
