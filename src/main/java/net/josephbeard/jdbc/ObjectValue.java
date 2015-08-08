package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * An object {@link ParameterValue}.
 */
class ObjectValue implements ParameterValue {

    private final Object value;

    ObjectValue(Object value) {
        assert value != null : "value is null!";
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setObject(parameterIndex, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ObjectValue that = (ObjectValue) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "(Object)" + value;
    }
}
