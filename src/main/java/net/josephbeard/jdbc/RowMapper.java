package net.josephbeard.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a single row from a {@link ResultSet}.
 */
@FunctionalInterface
public interface RowMapper<T> {

    /**
     * Map the current row of the {@link ResultSet}.
     *
     * @param resultSet the result set
     * @param rowNumber the row number within the result set
     * @return the mapping result
     * @throws SQLException if an error occurs
     */
    T processRow(ResultSet resultSet, long rowNumber) throws SQLException;

}
