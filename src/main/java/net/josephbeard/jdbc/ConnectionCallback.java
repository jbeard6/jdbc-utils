package net.josephbeard.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Callback interface for behavior performed within a {@link Connection}.
 */
@FunctionalInterface
public interface ConnectionCallback<T> {

    /**
     * Execute any behavior within the open {@link Connection}. Implementations are
     * not permitted to {@link Connection#close()} the {@code connection}.
     *
     * @param connection
     *            an open connection
     */
    T execute(Connection connection) throws SQLException;

}
