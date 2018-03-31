package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * An Integer {@link ParameterValue}.
 */
final class IntegerValue implements ParameterValue {

    private final int value;

    IntegerValue(int value) {
        this.value = value;
    }

    IntegerValue(Number value) {
        assert value != null : "value is null!";
        this.value = value.intValue();
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setInt(parameterIndex, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IntegerValue other = (IntegerValue) obj;
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
        return "(Integer)" + value;
    }
}
