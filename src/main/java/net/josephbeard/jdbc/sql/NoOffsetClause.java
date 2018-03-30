/**
 * 
 */
package net.josephbeard.jdbc.sql;

/**
 * An {@link OffsetClause} that represents no offset.
 */
class NoOffsetClause implements OffsetClause {

    NoOffsetClause() {
        // Internal construction only
    }

    @Override
    public String toSql() {
        return ""; // No offset
    }

}
