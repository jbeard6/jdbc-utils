/**
 * 
 */
package net.josephbeard.jdbc.sql;

import java.util.List;

import org.apache.commons.lang3.Validate;

import net.josephbeard.jdbc.ParameterValue;

/**
 * A SQL {@code WHERE} clause.
 */
public interface WhereClause {

    /**
     * Return the SQL condition.
     *
     * @return the SQL condition
     */
    String getCondition();

    /**
     * Return any required {@link ParameterValue}s for this {@link WhereClause}.
     *
     * @return the parameters
     */
    List<ParameterValue> getParameters();

    /**
     * Return this {@link WhereClause} as SQL.
     *
     * @return this WHERE clause as SQL
     */
    default String toSql() {
        return " WHERE " + getCondition();
    }

    /**
     * {@code AND} this {@link WhereClause} with the {@code other} one.
     *
     * @param other the other {@link WhereClause}
     * @return an {@code AND} clause
     * @throws NullPointerException if the {@code other} clause is {@code null}
     */
    default WhereClause and(WhereClause other) {
        Validate.notNull(other, "The other must not be null");
        return new AndWhereClause(this, other);
    }

    /**
     * {@code OR} this {@link WhereClause} with the {@code other} one.
     *
     * @param other the other {@link WhereClause}
     * @return an {@code OR} clause
     * @throws NullPointerException if the {@code other} clause is {@code null}
     */
    default WhereClause or(WhereClause other) {
        Validate.notNull(other, "The other must not be null");
        return new OrWhereClause(this, other);
    }

}
