/**
 * 
 */
package net.josephbeard.jdbc.sql;

import java.util.Collections;
import java.util.List;

import net.josephbeard.jdbc.ParameterValue;

/**
 * A SQL {@code LIMIT} clause.
 */
public interface LimitClause {

    /**
     * Express this {@link LimitClause} as SQL.
     *
     * @return the SQL form of this limit clause
     */
    String toSql();

    /**
     * Return any required {@link ParameterValue}s for this {@link LimitClause}.
     *
     * @return the parameters
     */
    default List<ParameterValue> getParameters() {
        return Collections.emptyList();
    }

}
