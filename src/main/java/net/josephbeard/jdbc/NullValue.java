package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A null {@link ParameterValue}.
 */
class NullValue implements ParameterValue {

    private final int sqlType;

    NullValue(int sqlType) {
        this.sqlType = sqlType;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setNull(parameterIndex, sqlType);
    }
}
