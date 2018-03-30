/**
 * 
 */
package net.josephbeard.jdbc.postgres;

import java.util.Collections;
import java.util.List;

import net.josephbeard.jdbc.JDBC;
import net.josephbeard.jdbc.ParameterValue;
import net.josephbeard.jdbc.sql.LimitClause;

/**
 * A {@link LimitClause} for PostgreSQL.
 */
class PostgreSQLLimitClause implements LimitClause {

    private final long value;

    private ParameterValue parameter;

    PostgreSQLLimitClause(long value) {
        assert value >= 0L : "value is negative!";
        this.value = value;
        this.parameter = JDBC.longInt(value);
    }

    public String toSql() {
        return " OFFSET ?";
    }

    @Override
    public List<ParameterValue> getParameters() {
        return Collections.singletonList(parameter);
    }

    @Override
    public String toString() {
        return " OFFSET " + value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (value ^ (value >>> 32));
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
        PostgreSQLLimitClause other = (PostgreSQLLimitClause) obj;
        if (value != other.value)
            return false;
        return true;
    }

}
