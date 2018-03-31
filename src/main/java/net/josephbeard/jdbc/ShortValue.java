package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A short {@link ParameterValue}.
 */
final class ShortValue implements ParameterValue {

    private final short value;

    ShortValue(short value) {
        this.value = value;
    }

    ShortValue(Number value) {
        assert value != null : "value is null!";
        this.value = value.shortValue();
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setShort(parameterIndex, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShortValue other = (ShortValue) obj;
        if (value != other.value)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
        return result;
    }

    @Override
    public String toString() {
        return "(Short)" + value;
    }
}
