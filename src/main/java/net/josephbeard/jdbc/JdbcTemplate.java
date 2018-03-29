package net.josephbeard.jdbc;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Templates for performing JDBC operations.
 */
public class JdbcTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    private final net.josephbeard.jdbc.ConnectionProvider connectionProvider;

    /**
     * Flag to bypass the potentially expensive operation of getting metadata from
     * the JDBC driver if it is known to be broken. This flag starts false but is
     * permanently set the first time that metadata is requested and it is found to
     * be unsupported.
     */
    private boolean ignoreMetadata;

    public JdbcTemplate(ConnectionProvider connectionProvider) {
        Validate.notNull(connectionProvider, "The connectionProvider must not be null");
        this.connectionProvider = connectionProvider;
        this.ignoreMetadata = false;
    }

    public JdbcTemplate(ConnectionProvider connectionProvider, boolean ignoreMetadata) {
        Validate.notNull(connectionProvider, "The connectionProvider must not be null");
        this.connectionProvider = connectionProvider;
        this.ignoreMetadata = ignoreMetadata;
    }

    /**
     * Execute some behavior with an injected {@link Connection}. The
     * {@link ConnectionCallback} will receive an open {@link Connection} which will
     * be closed upon completion.
     *
     * @param callback
     *            the behavior to be executed
     * @param <T>
     *            the type of result from the {@code callback}
     * @return the result of the {@code callback}
     * @throws SQLException
     *             if an error occurs
     */
    public <T> T withConnection(ConnectionCallback<T> callback) throws SQLException {
        Validate.notNull(callback, "The callback must not be null");

        try (Connection connection = connectionProvider.getConnection()) {
            return callback.execute(connection);
        }
    }

    /**
     * This method obtains a {@link Connection} and starts a transaction for the
     * {@link ConnectionCallback} behavior. The transaction will be committed if the
     * {@link ConnectionCallback} completes successfully, otherwise it will be
     * rolled back. The {@link ConnectionCallback} will receive an open
     * {@link Connection} which will be closed upon completion.
     *
     * @param callback
     *            the behavior to be executed in a transaction
     * @param <T>
     *            the type of result from the {@code callback}
     * @return the result of the {@code callback}
     * @throws SQLException
     *             if an error occurs
     */
    public <T> T doInTransaction(ConnectionCallback<T> callback) throws SQLException {
        Validate.notNull(callback, "The callback must not be null");

        return withConnection(connection -> {
            connection.setAutoCommit(false);
            LOGGER.debug("Started transaction on {}", connection);

            try {
                T result = callback.execute(connection);

                connection.commit();
                LOGGER.debug("Committed transaction on {}", connection);

                return result;
            } catch (Throwable t) {
                connection.rollback();
                LOGGER.debug("Rolled back transaction on {}", connection);
                throw t;
            } finally {
                connection.setAutoCommit(true);
            }
        });
    }

    /**
     * Execute the {@code sql} query with the specified {@code params} and return
     * the results of the {@link RowMapper}.
     *
     * @param sql
     *            the SQL statement
     * @param rowMapper
     *            the callback for mapping the query {@link ResultSet} rows
     * @param params
     *            the parameters to the {@code sql} query (optional)
     * @param <T>
     *            the type of result of the {@code rowMapper} callback
     * @return list of results from the {@code rowMapper} callback
     * @throws SQLException
     *             if an error occurs
     */
    public <T> List<T> select(String sql, RowMapper<T> rowMapper, ParameterValue... params) throws SQLException {
        return withConnection(
                connection -> select(connection, sql, resultSet -> mapRows(resultSet, rowMapper), params));
    }

    /**
     * Execute the {@code sql} query on the {@link Connection}, with the specified
     * {@code params} and return the results of the {@link RowMapper}.
     * <p>
     * The {@link Connection} will not be closed by this method.
     *
     * @param connection
     *            the connection on which to execute the query
     * @param sql
     *            the SQL statement
     * @param rowMapper
     *            the callback for mapping the query {@link ResultSet} rows
     * @param params
     *            the parameters to the {@code sql} query (optional)
     * @param <T>
     *            the type of result of the {@code rowMapper} callback
     * @return list of results from the {@code rowMapper} callback
     * @throws SQLException
     *             if an error occurs
     */
    public <T> List<T> select(Connection connection, String sql, RowMapper<T> rowMapper, ParameterValue... params)
            throws SQLException {
        return select(connection, sql, new RowMapperResultSetHandler<>(rowMapper), params);
    }

    /**
     * Execute the {@code sql} query with the specified {@code params} and return
     * the result of the {@link ResultSetHandler}.
     *
     * @param sql
     *            the SQL statement
     * @param resultSetHandler
     *            the callback for handling the query {@link ResultSet}
     * @param params
     *            the parameters to the {@code sql} query (optional)
     * @param <T>
     *            the type of result of the {@code resultSetHandler} callback
     * @return the result of the {@code resultSetHandler} callback
     * @throws SQLException
     *             if an error occurs
     */
    public <T> T select(String sql, ResultSetHandler<T> resultSetHandler, ParameterValue... params)
            throws SQLException {
        Validate.notBlank(sql, "The sql must not be blank");
        Validate.notNull(resultSetHandler, "The resultSetHandler must not be null");

        return withConnection(connection -> select(connection, sql, resultSetHandler, params));
    }

    /**
     * Execute the {@code sql} query on the {@link Connection}, with the specified
     * {@code params} and return the result of the {@link ResultSetHandler}.
     * <p>
     * The {@link Connection} will not be closed by this method.
     *
     * @param connection
     *            the connection on which to execute the query
     * @param sql
     *            the SQL statement
     * @param resultSetHandler
     *            the callback for handling the query {@link ResultSet}
     * @param params
     *            the parameters to the {@code sql} query (optional)
     * @param <T>
     *            the type of result of the {@code resultSetHandler} callback
     * @return the result of the {@code resultSetHandler} callback
     * @throws SQLException
     *             if an error occurs
     */
    public <T> T select(Connection connection, String sql, ResultSetHandler<T> resultSetHandler,
            ParameterValue... params) throws SQLException {
        Validate.notNull(connection, "The connection must not be null");
        Validate.notBlank(sql, "The sql must not be blank");
        Validate.notNull(resultSetHandler, "The resultSetHandler must not be null");

        try (PreparedStatement st = prepareStatement(connection, sql, params)) {
            return query(st, resultSetHandler);
        }
    }

    /**
     * Execute the {@code sql} query with the specified {@code params} and return
     * the result of the {@link RowMapper}. The {@code sql} query is expected to
     * return, at most, one row.
     *
     * @param sql
     *            the SQL statement
     * @param rowMapper
     *            the callback for mapping the query {@link ResultSet} row
     * @param params
     *            the parameters to the {@code sql} query (optional)
     * @param <T>
     *            the type of result of the {@code rowMapper} callback
     * @return optional of the mapped value of the {@link ResultSet} row, or
     *         {@link Optional#empty()} if the query returned no results
     * @throws SQLException
     *             if an error occurs or more than one row was returned
     */
    public <T> Optional<T> selectOne(String sql, RowMapper<T> rowMapper, ParameterValue... params) throws SQLException {
        return withConnection(connection -> selectOne(connection, sql, rowMapper, params));
    }

    /**
     * Execute the {@code sql} query on the {@link Connection}, with the specified
     * {@code params} and return the result of the {@link RowMapper}. The
     * {@code sql} query is expected to return, at most, one row.
     * <p>
     * The {@link Connection} will not be closed by this method.
     *
     * @param connection
     *            the connection on which to execute the query
     * @param sql
     *            the SQL statement
     * @param rowMapper
     *            the callback for mapping the query {@link ResultSet} row
     * @param params
     *            the parameters to the {@code sql} query (optional)
     * @param <T>
     *            the type of result of the {@code rowMapper} callback
     * @return optional of the mapped value of the {@link ResultSet} row, or
     *         {@link Optional#empty()} if the query returned no results
     * @throws SQLException
     *             if an error occurs or more than one row was returned
     */
    public <T> Optional<T> selectOne(Connection connection, String sql, RowMapper<T> rowMapper,
            ParameterValue... params) throws SQLException {
        Validate.notNull(connection, "The connection must not be null");
        Validate.notBlank(sql, "The sql must not be blank");
        Validate.notNull(rowMapper, "The rowMapper must not be null");

        try (PreparedStatement st = prepareStatement(connection, sql, params)) {
            return queryForOne(st, rowMapper);
        }
    }

    /**
     * Execute an INSERT {@code sql} statement with the specified {@code params}.
     *
     * @param sql
     *            the sql statement
     * @param params
     *            the parameters to the {@code sql} query (optional)
     * @return the number of rows affected
     * @throws SQLException
     *             if an error occurs
     */
    public int insert(String sql, ParameterValue... params) throws SQLException {
        return withConnection(connection -> insert(connection, sql, params));
    }

    /**
     * Execute an INSERT {@code sql} statement on the {@link Connection} with the
     * specified {@code params}.
     * <p>
     * The {@link Connection} will not be closed by this method.
     *
     * @param connection
     *            an open connection
     * @param sql
     *            the sql statement
     * @param params
     *            the parameters to the {@code sql} query (optional)
     * @return the number of rows affected
     * @throws SQLException
     *             if an error occurs
     */
    public int insert(Connection connection, String sql, ParameterValue... params) throws SQLException {
        Validate.notNull(connection, "The connection must not be null");
        Validate.notBlank(sql, "The sql must not be blank");

        try (PreparedStatement st = prepareStatement(connection, sql, params)) {
            return st.executeUpdate();
        }
    }

    /**
     * Create a {@link PreparedStatement} to execute the specified {@code sql}
     * statement with the specified {@code params}.
     * <p>
     * The {@link Connection} will not be closed by this method.
     *
     * @param connection
     *            an open connection
     * @param sql
     *            the sql statement
     * @param params
     *            the parameters to the {@code sql} statement (optional)
     * @return the prepared statement with all parameters applied
     * @throws SQLException
     *             if an error occurs while preparing the statement
     */
    public PreparedStatement prepareStatement(Connection connection, String sql, ParameterValue... params)
            throws SQLException {
        Validate.notNull(connection, "The connection must not be null");
        Validate.notBlank(sql, "The sql must not be blank");
        Validate.notNull(params, "The params must not be null");

        PreparedStatement statement = connection.prepareStatement(sql);
        LOGGER.debug("Prepared statement for {}", sql);

        try {
            applyParameters(statement, params);
            LOGGER.debug("Applied parameters to {}: {}", sql, params);
        } catch (Throwable t) {
            try {
                statement.close();
                throw t;
            } catch (SQLException ex) {
                ex.initCause(t);
                throw ex;
            }
        }

        return statement;
    }

    /**
     * Obtain the {@link ParameterMetaData} for the {@link PreparedStatement} if the
     * JDBC driver supports it.
     *
     * @param statement
     *            the statement
     * @return the parameter metadata, if supported; <code>null</code> otherwise
     * @throws SQLException
     *             if an error occurs retrieving the metadata
     */
    private ParameterMetaData getMetadata(PreparedStatement statement) throws SQLException {
        assert statement != null : "statement is null!";

        if (ignoreMetadata) {
            return null;
        }

        try {
            ParameterMetaData md = statement.getParameterMetaData();
            if (md == null) { // Broken implementations will return null instead of throwing an exception
                LOGGER.warn("Parameter Metadata Feature is not supported.");
                ignoreMetadata = true;
            }
            return md;
        } catch (SQLFeatureNotSupportedException ex) {
            LOGGER.warn("Parameter Metadata Feature is not supported.", ex);
            ignoreMetadata = true;
            return null;
        }
    }

    /**
     * Apply the {@code params} to the {@link PreparedStatement}.
     *
     * @param statement
     *            the statement
     * @param params
     *            the parameters to apply
     * @throws SQLException
     */
    public void applyParameters(PreparedStatement statement, ParameterValue... params) throws SQLException {
        Validate.notNull(statement, "The statement must not be null");

        ParameterMetaData md = getMetadata(statement);
        if (md != null) {
            int expectedParameters = md.getParameterCount();
            int providedParameters = params == null ? 0 : params.length;
            Validate.isTrue(expectedParameters == providedParameters, "Expected {} parameters but received {}.",
                    expectedParameters, providedParameters);
        }

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                final int parameterIndex = i + 1;

                ParameterValue value = params[i];
                if (value == null) {
                    // Most drivers seem to react well to using VARCHAR for null values if the type
                    // is unknown
                    statement.setNull(parameterIndex, md == null ? Types.VARCHAR : md.getParameterType(parameterIndex));
                } else {
                    value.applyValue(statement, parameterIndex);
                }
            }
        }
    }

    /**
     * Execute a {@link PreparedStatement} for a query (that is, it returns a
     * {@link ResultSet}).
     *
     * @param statement
     *            the statement to execute
     * @param resultSetHandler
     *            the callback to handle the query {@link ResultSet}
     * @param <T>
     *            the type of result of the {@code resultSetHandler} callback
     * @return the result of the {@code resultSetHandler} callback
     * @throws SQLException
     *             if an error occurs
     */
    public <T> T query(PreparedStatement statement, ResultSetHandler<T> resultSetHandler) throws SQLException {
        Validate.notNull(statement, "The statement must not be null");
        Validate.notNull(resultSetHandler, "The resultSetHandler must not be null");
        try (ResultSet resultSet = statement.executeQuery()) {

            return resultSetHandler.processResultSet(resultSet);
        }
    }

    /**
     * Execute a {@link PreparedStatement} for a query (that is, it returns a
     * {@link ResultSet}) and map the results using the supplied {@link RowMapper}.
     *
     * @param statement
     *            the statement to execute
     * @param rowMapper
     *            the callback for mapping the query {@link ResultSet} rows
     * @param <T>
     *            the type of result of the {@code rowMapper} callback
     * @return list of results from the {@code rowMapper} callback
     * @throws SQLException
     *             if an error occurs
     */
    public <T> List<T> query(PreparedStatement statement, RowMapper<T> rowMapper) throws SQLException {
        return query(statement, (resultSet -> mapRows(resultSet, rowMapper)));
    }

    /**
     * Execute a {@link PreparedStatement} for a query (that is, it returns a
     * {@link ResultSet}) that is expected to return (at most) one row. The result
     * is mapped using the supplied {@link RowMapper}.
     *
     * @param statement
     *            the statement to execute
     * @param rowMapper
     *            the callback for mapping the query {@link ResultSet} row
     * @param <T>
     *            the type of result of the {@code rowMapper} callback
     * @return optional of the mapped value of the {@link ResultSet} row, or
     *         {@link Optional#empty()} if the query returned no results
     * @throws SQLException
     *             if an error occurs or more than one row was returned
     */
    public <T> Optional<T> queryForOne(PreparedStatement statement, RowMapper<T> rowMapper) throws SQLException {
        Validate.notNull(statement, "The statement must not be null");
        Validate.notNull(rowMapper, "The rowMapper must not be null");

        try (ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.next()) {
                return Optional.empty();
            }

            T result = rowMapper.processRow(resultSet, 1L);
            if (resultSet.next()) {
                throw new SQLException("Multiple results returned when one expected");
            }
            return Optional.ofNullable(result);
        }
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
    public <T> List<T> mapRows(ResultSet resultSet, RowMapper<T> rowMapper) throws SQLException {
        Validate.notNull(resultSet, "The resultSet must not be null");
        Validate.notNull(rowMapper, "The rowMapper must not be null");

        List<T> results = new LinkedList<>();
        for (long rowNumber = 0; resultSet.next(); rowNumber++) {
            results.add(rowMapper.processRow(resultSet, rowNumber));
        }
        return results;
    }

}
