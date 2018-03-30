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

    @Override
    public void close() throws SQLException {
        if (dataSource instanceof AutoCloseable) {
            try {
                ((AutoCloseable) dataSource).close();
            } catch (SQLException e) {
                // No wrapping required, and this is most likely
                throw e;
            } catch (Exception e) {
                // Wrap with generic SQLException to meet contract
                throw new SQLException("Failed to close DataSource", e);
            }
        }
    }
}
