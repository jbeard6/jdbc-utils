/**
 * 
 */
package net.josephbeard.jdbc.sql;

import static java.util.stream.Collectors.joining;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import net.josephbeard.jdbc.ParameterValue;

/**
 * A SQL {@code OR} of other {@link WhereClause}s.
 */
public class OrWhereClause implements WhereClause {

    private final List<WhereClause> clauses;

    public OrWhereClause() {
        this.clauses = new LinkedList<>();
    }

    public OrWhereClause(WhereClause... clauses) {
        Validate.noNullElements(clauses, "The clauses must not be null");
        this.clauses = new LinkedList<>();
        Stream.of(clauses).forEach(this.clauses::add);
    }

    public OrWhereClause(List<WhereClause> clauses) {
        Validate.noNullElements(clauses, "The clauses must not be null");
        this.clauses = new LinkedList<>(clauses);
    }

    // Visible for testing
    List<WhereClause> getClauses() {
        return clauses;
    }

    @Override
    public String getCondition() {

        if (this.clauses.isEmpty()) {
            return "";
        }

        return "(" + clauses.stream().map(WhereClause::getCondition).filter(StringUtils::isNotBlank)
                .collect(joining(" OR ")) + ")";
    }

    @Override
    public List<ParameterValue> getParameters() {
        return clauses.stream().map(WhereClause::getParameters).flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public WhereClause and(WhereClause whereClause) {
        Validate.notNull(whereClause, "The whereClause must not be null");

        if (this.clauses.isEmpty()) {
            // Special case for if there are no nested clauses
            return whereClause;
        }

        return new AndWhereClause(this, whereClause);
    }

    @Override
    public WhereClause or(WhereClause whereClause) {
        Validate.notNull(whereClause, "The whereClause must not be null");

        this.clauses.add(whereClause);
        return this;
    }

    @Override
    public String toSql() {
        if (this.clauses.isEmpty()) {
            // Special case for if there are no nested clauses
            return "";
        }

        return " WHERE " + getCondition();
    }

    @Override
    public String toString() {
        return "OrWhereClause{" + "condition='" + getCondition() + '\'' + ", parameters=" + getParameters() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OrWhereClause that = (OrWhereClause) o;

        return clauses != null ? clauses.equals(that.clauses) : that.clauses == null;
    }

    @Override
    public int hashCode() {
        return clauses != null ? clauses.hashCode() : 0;
    }
}
