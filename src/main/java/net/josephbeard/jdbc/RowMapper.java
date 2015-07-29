package net.josephbeard.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A callback for mapping rows from a {@link ResultSet}.
 */
public interface RowMapper<T> {

    T processRow(ResultSet resultSet, long rowNumber) throws SQLException;

}
