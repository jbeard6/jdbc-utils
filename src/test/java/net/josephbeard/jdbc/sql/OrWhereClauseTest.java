package net.josephbeard.jdbc.sql;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.josephbeard.jdbc.JDBC;

public class OrWhereClauseTest {

    @Test
    public void zero_clauses() {
        OrWhereClause subject = new OrWhereClause();

        assertThat("clauses", subject.getClauses(), is(empty()));
        assertThat("condition", subject.getCondition(), is(equalTo("")));
        assertThat("parameters", subject.getParameters(), is(empty()));
        assertThat("sql", subject.toSql(), isEmptyString());

        WhereClause otherClause = new SimpleWhereClause("name = ?", JDBC.string("foo"));

        assertThat("and", subject.and(otherClause), is(sameInstance(otherClause)));

        assertThat("or", subject.or(otherClause), is(sameInstance(subject)));
        assertThat("or.clauses", subject.getClauses(), contains(otherClause));
    }

    @Test
    public void one_clause() {
        SimpleWhereClause clause1 = new SimpleWhereClause("name = ?", JDBC.string("foo"));

        OrWhereClause subject = new OrWhereClause(clause1);

        assertThat("clauses", subject.getClauses(), contains(clause1));
        assertThat("condition", subject.getCondition(), is(equalTo("(name = ?)")));
        assertThat("parameters", subject.getParameters(), contains(JDBC.string("foo")));
        assertThat("sql", subject.toSql(), is(equalTo(" WHERE (name = ?)")));

        WhereClause otherClause = new SimpleWhereClause("name = ?", JDBC.string("foo"));

        assertThat("and", subject.and(otherClause), is(instanceOf(AndWhereClause.class)));

        assertThat("or", subject.or(otherClause), is(sameInstance(subject)));
        assertThat("or.clauses", subject.getClauses(), contains(clause1, otherClause));
    }

    @Test
    public void two_clauses() {
        SimpleWhereClause clause1 = new SimpleWhereClause("name = ?", JDBC.string("foo"));
        SimpleWhereClause clause2 = new SimpleWhereClause("value = ?", JDBC.string("bar"));

        OrWhereClause subject = new OrWhereClause(clause1, clause2);

        assertThat("clauses", subject.getClauses(), contains(clause1, clause2));
        assertThat("condition", subject.getCondition(), is(equalTo("(name = ? OR value = ?)")));
        assertThat("parameters", subject.getParameters(), contains(JDBC.string("foo"), JDBC.string("bar")));
        assertThat("sql", subject.toSql(), is(equalTo(" WHERE (name = ? OR value = ?)")));

        WhereClause otherClause = new SimpleWhereClause("name = ?", JDBC.string("foo"));

        assertThat("and", subject.and(otherClause), is(instanceOf(AndWhereClause.class)));

        assertThat("or", subject.or(otherClause), is(sameInstance(subject)));
        assertThat("or.clauses", subject.getClauses(), contains(clause1, clause2, otherClause));
    }

    @Test
    public void three_clauses() {
        SimpleWhereClause clause1 = new SimpleWhereClause("name = ?", JDBC.string("foo"));
        SimpleWhereClause clause2 = new SimpleWhereClause("value = ?", JDBC.string("bar"));
        SimpleWhereClause clause3 = new SimpleWhereClause("qualifier = ?", JDBC.string("baz"));

        OrWhereClause subject = new OrWhereClause(clause1, clause2, clause3);

        assertThat("clauses", subject.getClauses(), contains(clause1, clause2, clause3));
        assertThat("condition", subject.getCondition(), is(equalTo("(name = ? OR value = ? OR qualifier = ?)")));
        assertThat("parameters", subject.getParameters(),
                contains(JDBC.string("foo"), JDBC.string("bar"), JDBC.string("baz")));
        assertThat("sql", subject.toSql(), is(equalTo(" WHERE (name = ? OR value = ? OR qualifier = ?)")));

        WhereClause otherClause = new SimpleWhereClause("name = ?", JDBC.string("foo"));

        assertThat("and", subject.and(otherClause), is(instanceOf(AndWhereClause.class)));

        assertThat("or", subject.or(otherClause), is(sameInstance(subject)));
        assertThat("or.clauses", subject.getClauses(), contains(clause1, clause2, clause3, otherClause));
    }

    @Test
    public void two_clauses_but_one_is_empty() {
        WhereClause clause1 = new SimpleWhereClause("name = ?", JDBC.string("foo"));
        WhereClause clause2 = new OrWhereClause();

        OrWhereClause subject = new OrWhereClause(clause1, clause2);

        assertThat("clauses", subject.getClauses(), contains(clause1, clause2));
        assertThat("condition", subject.getCondition(), is(equalTo("(name = ?)")));
        assertThat("parameters", subject.getParameters(), contains(JDBC.string("foo")));
        assertThat("sql", subject.toSql(), is(equalTo(" WHERE (name = ?)")));

        WhereClause otherClause = new SimpleWhereClause("value = ?", JDBC.string("bar"));

        assertThat("and", subject.and(otherClause), is(instanceOf(AndWhereClause.class)));

        assertThat("or", subject.or(otherClause), is(sameInstance(subject)));
        assertThat("or.clauses", subject.getClauses(), contains(clause1, clause2, otherClause));
        assertThat("or.condition", subject.getCondition(), is(equalTo("(name = ? OR value = ?)")));
        assertThat("or.parameters", subject.getParameters(), contains(JDBC.string("foo"), JDBC.string("bar")));
        assertThat("or.sql", subject.toSql(), is(equalTo(" WHERE (name = ? OR value = ?)")));
    }

}