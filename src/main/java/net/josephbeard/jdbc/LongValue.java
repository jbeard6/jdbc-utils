package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A Long {@link ParameterValue}.
 */
final class LongValue implements ParameterValue {

    private final long value;

    LongValue(long value) {
        this.value = value;
    }

    LongValue(Number value) {
        assert value != null : "value is null!";
        this.value = value.longValue();
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setLong(parameterIndex, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LongValue other = (LongValue) obj;
        if (value != other.value)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "(Long)" + value;
    }
}
