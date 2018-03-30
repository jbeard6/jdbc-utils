/**
 * 
 */
package net.josephbeard.jdbc.sql;

/**
 * Utility class for working with SQL statements.
 */
public abstract class SQL {

    private static final NoOffsetClause NO_OFFSET_CLAUSE = new NoOffsetClause();

    /**
     * Returns an {@link OffsetClause} that represents no offset.
     * 
     * @return no offset clause
     */
    public static OffsetClause noOffset() {
        return NO_OFFSET_CLAUSE;
    }

    private static final NoLimitClause NO_LIMIT_CLAUSE = new NoLimitClause();

    /**
     * Returns a {@link LimitClause} that represents no limit.
     * 
     * @return no limit clause
     */
    public static LimitClause noLimit() {
        return NO_LIMIT_CLAUSE;
    }

    private SQL() {
        assert false : "SQL should not be instantiated";
    }

}
