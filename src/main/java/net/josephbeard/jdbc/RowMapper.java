package net.josephbeard.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;

/**
 * Maps a single row from a {@link ResultSet}.
 */
@FunctionalInterface
public interface RowMapper<T> {

    /**
     * Map the current row of the {@link ResultSet}.
     *
     * @param resultSet
     *            the result set
     * @param rowNumber
     *            the row number within the result set
     * @return the mapping result
     * @throws SQLException
     *             if an error occurs
     */
    T processRow(ResultSet resultSet, long rowNumber) throws SQLException;

    /**
     * Create a {@link RowConsumer} that first maps according to this
     * {@link RowMapper} and supplies the mapped value to the {@link Consumer}. For
     * example:
     * 
     * <pre>
     * RowMapper&lt;String&gt; rowMapper = (resultSet, rowNumber) -&gt; resultSet.getString("name");
     *
     * jdbcTemplate.forEach("SELECT name FROM people;",
     *      rowMapper.mappedRowConsumer(name -&gt; System.out.println("Hello, " + name));
     * </pre>
     *
     * @param consumer
     *            the mapped row consumer
     * @return the row consumer
     * @see JdbcTemplate#forEach(String, RowConsumer, ParameterValue...)
     */
    default RowConsumer consume(Consumer<? super T> consumer) {
        Validate.notNull(consumer, "The consumer must not be null");

        return (resultSet, rowNumber) -> {
            T row = processRow(resultSet, rowNumber);
            consumer.accept(row);
        };
    }

}
