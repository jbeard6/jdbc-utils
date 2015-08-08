package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A Long {@link ParameterValue}.
 */
final class LongValue implements ParameterValue {

    private final Number value;

    LongValue(Number value) {
        assert value != null : "value is null!";
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setLong(parameterIndex, value.longValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        LongValue that = (LongValue) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "(Long)" + value;
    }
}
