package net.josephbeard.jdbc;

import org.apache.commons.lang3.Validate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * A {@link ResultSetHandler} that invokes a {@link RowMapper} to return a {@link List} of mapped rows.
 */
public class RowMapperResultSetHandler<T> implements ResultSetHandler<List<T>> {

    private final RowMapper<T> rowMapper;

    public RowMapperResultSetHandler(RowMapper<T> rowMapper) {
        Validate.notNull(rowMapper, "The rowMapper must not be null");
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> processResultSet(ResultSet resultSet) throws SQLException {
        Validate.notNull(resultSet, "The resultSet must not be null");

        List<T> results = new LinkedList<>();
        for (long rowNumber = 0; resultSet.next(); rowNumber++) {
            results.add(rowMapper.processRow(resultSet, rowNumber));
        }
        return results;
    }
}
