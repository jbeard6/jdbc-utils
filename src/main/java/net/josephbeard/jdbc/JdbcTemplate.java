package net.josephbeard.jdbc;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Templates for performing JDBC operations.
 */
public class JdbcTemplate {

    private final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        Validate.notNull(dataSource, "The dataSource must not be null");
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public <T> T withConnection(ConnectionCallback<T> callback) throws SQLException {
        Validate.notNull(callback, "The callback must not be null");

        try (Connection connection = dataSource.getConnection()) {
            return callback.execute(connection);
        }
    }

    public <T> T withTransaction(ConnectionCallback<T> callback) throws SQLException {
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
            }
        });
    }

    public <T> List<T> select(String sql, RowMapper<T> rowMapper, Object... params) throws SQLException {
        Validate.notBlank(sql, "The sql must not be blank");
        Validate.notNull(rowMapper, "The rowMapper must not be null");

        return withConnection(connection -> {
            try (PreparedStatement st = prepareStatement(connection, sql, params);) {
                return executeQuery(st, rowMapper);
            }
        });
    }

    public PreparedStatement prepareStatement(Connection connection, String sql, Object... params) throws SQLException {
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

    // TODO This method is currently broken
    public void applyParameters(PreparedStatement statement, Object... params) throws SQLException {
        Validate.notNull(statement, "The statement must not be null");

        try {
            ParameterMetaData md = statement.getParameterMetaData();
            if (md == null) {
                LOGGER.warn("Statement returned null ParameterMetaData!");
            } else {
                int expectedParameters = md.getParameterCount();
                int providedParameters = params == null ? 0 : params.length;
                Validate.isTrue(expectedParameters == providedParameters, "Expected {} parameters but received {}.", expectedParameters, providedParameters);
            }

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    final int parameterIndex = i + 1;

                    Object value = params[i];
                    if (value == null) {
                        statement.setNull(parameterIndex, md == null ? Types.VARCHAR : md.getParameterType(parameterIndex));
                    } else {
                        statement.setObject(parameterIndex, value);
                    }
                }
            }
        } catch (SQLFeatureNotSupportedException ex) {
            LOGGER.warn("ParameterMetaData is not supported.", ex);
        }
    }

    private <T> List<T> executeQuery(PreparedStatement st, RowMapper<T> rowMapper) throws SQLException {
        try (ResultSet resultSet = st.executeQuery();) {
            return mapRows(resultSet, rowMapper);
        }
    }

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
