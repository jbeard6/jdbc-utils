/**
 * 
 */
package net.josephbeard.jdbc;

import java.sql.SQLException;

/**
 * A runnable that can throw a {@link SQLException}.
 */
@FunctionalInterface
public interface JdbcRunnable {

    /**
     * Execute this {@link JdbcRunnable}.
     *
     * @throws SQLException
     *             if an error occurs
     */
    void run() throws SQLException;

}
