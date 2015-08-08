package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A boolean {@link ParameterValue}.
 */
class BooleanValue implements ParameterValue {

    private static final BooleanValue FALSE_VALUE = new BooleanValue(false);
    private static final BooleanValue TRUE_VALUE = new BooleanValue(true);

    static BooleanValue valueOf(boolean value) {
        return value ? TRUE_VALUE : FALSE_VALUE;
    }

    private final boolean value;

    BooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setBoolean(parameterIndex, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BooleanValue that = (BooleanValue) o;

        return value == that.value;

    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }

    @Override
    public String toString() {
        return "(Boolean)" + value;
    }
}
