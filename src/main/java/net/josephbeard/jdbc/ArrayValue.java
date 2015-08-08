package net.josephbeard.jdbc;

import org.apache.commons.lang3.StringUtils;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * An {@link Array} {@link ParameterValue}.
 */
final class ArrayValue implements ParameterValue {

    private final String typeName;

    private final Object[] elements;

    ArrayValue(String typeName, Object[] elements) {
        assert StringUtils.isNotBlank(typeName) : "typeName is blank!";
        assert elements != null : "elements is null!";
        this.typeName = typeName;
        this.elements = elements;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        Array array = statement.getConnection().createArrayOf(typeName, elements);
        statement.setArray(parameterIndex, array);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ArrayValue that = (ArrayValue) o;

        if (!typeName.equals(that.typeName))
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(elements, that.elements);

    }

    @Override
    public int hashCode() {
        int result = typeName.hashCode();
        result = 31 * result + Arrays.hashCode(elements);
        return result;
    }

    @Override
    public String toString() {
        return "(" + typeName + "[])" + Arrays.toString(elements);
    }
}
