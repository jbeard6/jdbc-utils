package net.josephbeard.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A null {@link ParameterValue}.
 */
class NullValue implements ParameterValue {

    private final int sqlType;

    NullValue(int sqlType) {
        this.sqlType = sqlType;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setNull(parameterIndex, sqlType);
    }

    @Override
    public String toString() {
        return "(" + sqlType + ") NULL";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + sqlType;
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
        NullValue other = (NullValue) obj;
        if (sqlType != other.sqlType)
            return false;
        return true;
    }
}
