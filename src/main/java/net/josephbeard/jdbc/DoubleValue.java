package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A Double {@link ParameterValue}.
 */
final class DoubleValue implements ParameterValue {

    private final Number value;

    DoubleValue(Number value) {
        assert value != null : "value is nulL!";
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setDouble(parameterIndex, value.doubleValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DoubleValue that = (DoubleValue) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "(Double)" + value;
    }
}
