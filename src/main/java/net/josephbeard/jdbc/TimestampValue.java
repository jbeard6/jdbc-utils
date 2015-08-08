package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * A {@link Timestamp} {@link ParameterValue}.
 */
class TimestampValue implements ParameterValue {

    private final Timestamp value;

    TimestampValue(Timestamp value) {
        assert value != null : "value is null!";
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setTimestamp(parameterIndex, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TimestampValue timestampValue = (TimestampValue) o;

        return value.equals(timestampValue.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "(Timestamp)" + value;
    }
}
