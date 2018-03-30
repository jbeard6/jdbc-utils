/**
 * 
 */
package net.josephbeard.jdbc.sql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;

import net.josephbeard.jdbc.ParameterValue;

/**
 * A simple {@link WhereClause}.
 */
public final class SimpleWhereClause implements WhereClause {

    private final String condition;

    private final List<ParameterValue> parameters;

    public SimpleWhereClause(String condition) {
        Validate.notBlank(condition, "The condition must not be blank");
        this.condition = condition;
        this.parameters = Collections.emptyList();
    }

    public SimpleWhereClause(String condition, ParameterValue... parameters) {
        Validate.notBlank(condition, "The condition must not be blank");
        Validate.noNullElements(parameters, "The parameters must not be null");
        this.condition = condition;
        this.parameters = Arrays.asList(parameters);
    }

    public SimpleWhereClause(String condition, List<ParameterValue> parameters) {
        Validate.notBlank(condition, "The condition must not be blank");
        Validate.noNullElements(parameters, "The parameters must not be null");
        this.condition = condition;
        this.parameters = parameters;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public List<ParameterValue> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "SimpleWhereClause{" + "condition='" + getCondition() + '\'' + ", parameters=" + getParameters() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SimpleWhereClause that = (SimpleWhereClause) o;

        if (condition != null ? !condition.equals(that.condition) : that.condition != null)
            return false;
        return parameters != null ? parameters.equals(that.parameters) : that.parameters == null;

    }

    @Override
    public int hashCode() {
        int result = condition != null ? condition.hashCode() : 0;
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }

}
