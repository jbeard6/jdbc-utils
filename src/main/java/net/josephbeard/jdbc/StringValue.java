package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A String (or similar) value.
 */
class StringValue implements ParameterValue {

    private final String value;

    StringValue(String value) {
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setString(parameterIndex, value);
    }

    @Override
    public String toString() {
        return "(String) " + value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StringValue other = (StringValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
