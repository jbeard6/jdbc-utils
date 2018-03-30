/**
 * 
 */
package net.josephbeard.jdbc.sql;

/**
 * A {@link LimitClause} that represents no Limit.
 */
class NoLimitClause implements LimitClause {

    NoLimitClause() {
        // Internal construction only
    }

    @Override
    public String toSql() {
        return ""; // No Limit!
    }

}
