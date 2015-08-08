package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A String (or similar) value.
 */
class StringValue implements ParameterValue {

    private final String value;

    StringValue(String value) {
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setString(parameterIndex, value);
    }
}
