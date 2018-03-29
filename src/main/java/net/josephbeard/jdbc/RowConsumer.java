/**
 * 
 */
package net.josephbeard.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Consume a single row from a {@link ResultSet}.
 */
@FunctionalInterface
public interface RowConsumer {

    /**
     * Consume the current row of the {@link ResultSet}.
     *
     * @param resultSet
     *            the result set
     * @param rowNumber
     *            the row number within the result set
     * @throws SQLException
     *             if an error occurs
     */
    void consumeRow(ResultSet resultSet, long rowNumber) throws SQLException;

}
