package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * An array of bytes {@link ParameterValue}.
 */
final class BytesValue implements ParameterValue {

    private final byte[] elements;

    BytesValue(byte[] elements) {
        assert elements != null : "elements is null!";
        this.elements = elements;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setBytes(parameterIndex, elements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BytesValue that = (BytesValue) o;

        return Arrays.equals(elements, that.elements);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
        return "(byte[])" + Arrays.toString(elements);
    }
}
