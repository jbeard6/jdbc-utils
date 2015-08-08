package net.josephbeard.jdbc;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A {@link BigDecimal} {@link ParameterValue}.
 */
final class BigDecimalValue implements ParameterValue {

    private final BigDecimal value;

    BigDecimalValue(BigDecimal value) {
        assert value != null : "value is nulL!";
        this.value = value;
    }

    @Override
    public void applyValue(PreparedStatement statement, int parameterIndex) throws SQLException {
        statement.setBigDecimal(parameterIndex, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BigDecimalValue that = (BigDecimalValue) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "(BigDecimal)" + value;
    }
}
