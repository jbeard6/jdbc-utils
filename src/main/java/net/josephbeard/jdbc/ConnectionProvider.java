package net.josephbeard.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An abstraction for retrieving a {@link Connection}.
 */
public interface ConnectionProvider extends AutoCloseable {

    /**
     * Obtain an open {@link Connection}.
     *
     * @return an open connection
     * @throws SQLException
     *             if an error occurs obtaining a connection
     */
    Connection getConnection() throws SQLException;

    /**
     * Close this {@link ConnectionProvider}.
     *
     * @throws SQLException
     *             if an error occurs while closing
     */
    @Override
    default void close() throws SQLException {
        // Override to clean up
    }

}
