/**
 * 
 */
package net.josephbeard.jdbc.sql;

import java.util.Collections;
import java.util.List;

import net.josephbeard.jdbc.ParameterValue;

/**
 * A SQL {@code OFFSET} clause.
 */
public interface OffsetClause {

    /**
     * Express this {@link OffsetClause} as SQL.
     *
     * @return the SQL form of this offset clause
     */
    String toSql();

    /**
     * Return any required {@link ParameterValue}s for this {@link OffsetClause}.
     *
     * @return the parameters
     */
    default List<ParameterValue> getParameters() {
        return Collections.emptyList();
    }

}
