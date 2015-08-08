/**
 *
 */
package net.josephbeard.jdbc.postgres;

import net.josephbeard.jdbc.ParameterValue;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A representation of the PostgreSQL {@link PGobject} type.
 */
class PGobjectValue implements ParameterValue {

    private final PGobject value;

    PGobjectValue(PGobject value) {
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

        PGobjectValue that = (PGobjectValue) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "(PGobject)" + value;
    }
}
