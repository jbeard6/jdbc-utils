package net.josephbeard.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A callback for performing operations in a transaction.
 */
public interface TransactionCallback<T> {

    T doInTransaction(Connection connection) throws SQLException;
}
