package net.josephbeard.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A callback for performing operations with an open {@link Connection}.
 */
public interface ConnectionCallback<T> {

    T execute(Connection connection) throws SQLException;

}
