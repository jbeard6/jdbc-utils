package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A short {@link ParameterValue}.
 */
final class ShortValue implements ParameterValue {

    private final Number value;

    ShortValue(Number value) {
        assert value != null : "value is null!";
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setShort(parameterIndex, value.shortValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ShortValue that = (ShortValue) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "(Short)" + value;
    }
}
