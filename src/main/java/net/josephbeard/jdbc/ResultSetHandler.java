package net.josephbeard.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Processes a {@link ResultSet}.
 */
public interface ResultSetHandler<T> {

    /**
     * Process the given {@link ResultSet}.  Implementations must not invoke
     * {@link ResultSet#close()} when used by a {@link JdbcTemplate}.
     *
     * @param resultSet the result set
     * @return the results of processing the result set
     * @throws SQLException if an error occurs
     */
    T processResultSet(ResultSet resultSet) throws SQLException;

}
