package net.josephbeard.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An abstraction for retrieving a {@link Connection}.
 */
public interface ConnectionProvider {

    /**
     * Obtain an open {@link Connection}.
     *
     * @return an open connection
     * @throws SQLException if an error occurs obtaining a connection
     */
    Connection getConnection() throws SQLException;

}
