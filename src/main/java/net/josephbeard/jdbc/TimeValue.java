package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

/**
 * A {@link Time} {@link ParameterValue}.
 */
class TimeValue implements ParameterValue {

    private final Time value;

    TimeValue(Time value) {
        assert value != null : "value is null!";
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setTime(parameterIndex, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TimeValue timeValue = (TimeValue) o;

        return value.equals(timeValue.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "(Time)" + value;
    }
}
