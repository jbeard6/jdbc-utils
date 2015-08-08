package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Representation of a value supplied to the {@link JdbcTemplate}.
 */
public interface ParameterValue {

    /**
     * Apply this {@link ParameterValue} to the supplied
     * {@link PreparedStatement} at the {@code parameterIndex}.
     *
     * @param statement      the statement
     * @param parameterIndex the index at which to apply the value
     * @throws SQLException if an error occurs
     */
    void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException;

}
