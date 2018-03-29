/**
 * 
 */
package net.josephbeard.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.Validate;

/**
 * Utility class for {@link ResultSet} processing.
 */
public final class ResultSets {

    public static ResultSetHandler<Void> consumer(RowConsumer consumer) {
        return new RowConsumerHandler(consumer);
    }

    /**
     * Map the rows in the {@link ResultSet} using the {@link RowMapper}.
     * <p>
     * The {@link ResultSet} will not be closed by this method.
     *
     * @param resultSet
     *            the result set to map
     * @param rowMapper
     *            the row mapper
     * @param <T>
     *            the type of result of the {@code rowMapper} callback
     * @return list of results from the {@code rowMapper} callback
     * @throws SQLException
     *             if an error occurs
     */
    public static <T> List<T> mapRows(ResultSet resultSet, RowMapper<T> rowMapper) throws SQLException {
        Validate.notNull(resultSet, "The resultSet must not be null");
        Validate.notNull(rowMapper, "The rowMapper must not be null");

        List<T> results = new LinkedList<>();
        for (long rowNumber = 0; resultSet.next(); rowNumber++) {
            results.add(rowMapper.processRow(resultSet, rowNumber));
        }
        return results;
    }

    public static void forEach(ResultSet resultSet, RowConsumer consumer) throws SQLException {
        Validate.notNull(resultSet, "The resultSet must not be null");
        Validate.notNull(consumer, "The consumer must not be null");

        for (long rowNumber = 0; resultSet.next(); rowNumber++) {
            consumer.consumeRow(resultSet, rowNumber);
        }
    }

    /**
     * Create a {@link JdbcFunction} to return the {@link ResultSetMetaData} for the
     * {@link ResultSet}.
     *
     * @param resultSet
     *            the result set
     * @return the metadata function
     * @throws SQLException
     *             if an error occurs retrieving the metadata
     */
    public static JdbcFunction<ResultSet, ResultSetMetaData> retrieveMetadataFunction(ResultSet resultSet)
            throws SQLException {
        assert resultSet != null : "resultSet is null!";

        // Only retrieve the metadata once
        ResultSetMetaData rsMeta = resultSet.getMetaData();

        return rs -> {
            if (resultSet != rs) {
                throw new IllegalStateException("Unexpected ResultSet");
            }

            return rsMeta;
        };
    }

    private ResultSets() {
        // ResultSets is a utility class and should not be instantiated
    }
}
