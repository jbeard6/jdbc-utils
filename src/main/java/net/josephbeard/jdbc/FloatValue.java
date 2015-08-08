package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A Float {@link ParameterValue}.
 */
final class FloatValue implements ParameterValue {

    private final Number value;

    FloatValue(Number value) {
        assert value != null : "value is nulL!";
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setFloat(parameterIndex, value.floatValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FloatValue that = (FloatValue) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "(Float)" + value;
    }
}
