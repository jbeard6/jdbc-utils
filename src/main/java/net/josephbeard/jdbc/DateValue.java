package net.josephbeard.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A {@link Date} {@link ParameterValue}.
 */
class DateValue implements ParameterValue {

    private final Date value;

    DateValue(Date value) {
        assert value != null : "value is null!";
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setDate(parameterIndex, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DateValue dateValue = (DateValue) o;

        return value.equals(dateValue.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "(Date)" + value;
    }
}
