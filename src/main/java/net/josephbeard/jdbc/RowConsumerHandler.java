/**
 * 
 */
package net.josephbeard.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.Validate;

/**
 * A {@link ResultSetHandler} that invokes a {@link RowConsumer} on each row in
 * a {@link ResultSet}.
 */
public class RowConsumerHandler implements ResultSetHandler<Void> {

    private final RowConsumer consumer;

    public RowConsumerHandler(RowConsumer consumer) {
        Validate.notNull(consumer, "The consumer must not be null");
        this.consumer = consumer;
    }

    @Override
    public Void processResultSet(ResultSet resultSet) throws SQLException {
        Validate.notNull(resultSet, "The resultSet must not be null");

        for (long rowNumber = 0; resultSet.next(); rowNumber++) {
            consumer.consumeRow(resultSet, rowNumber);
        }

        return null;
    }
}
