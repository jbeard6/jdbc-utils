# JDBC Utils
This project contains utilities to simplify JDBC data access code.


## JdbcTemplate
The `JdbcTemplate` dramatically simplifies common JDBC usage and enforces the correct usage of the API.  For example, performing a query to obtain a value can be done like this:

    int value = jdbcTemplate.selectOne("SELECT value FROM table WHERE id = ?;",
        (rs, rowNum) -> rs.getInt("value"), JDBC.string("0101101011")).get();

This example makes use of the `RowMapper` interface, by which a row from a `ResultSet` can be converted to any arbitrary value as desired (such as mapping into an object).

The `ResultSetHandler` interface can be used if processing the `ResultSet` as a whole.

### Query Parameters
The `JdbcTemplate` avoids SQL injection by recommending the use of query parameters and the `ParameterValue` interface.  `ParameterValue` implementations are aware of how to apply themselves to a `PreparedStatement`.  Implementations are provided in the `JDBC` utility class for many of the JDBC standard types.

Database-specifc extensions are also supported.  The `net.josephbeard.jdbc.postgres` package includes several that are available when using a PostgreSQL JDBC driver.


### Transaction Support
Multiple calls can be performed in the same transaction.

    jdbcTemplate.doInTransaction(c -> {
        jdbcTemplate.insert(c, "INSERT INTO t1 (id, value) VALUES (?,?);", JDBC.string("0101101011"), JDBC.integer(5));
        jdbcTemplate.insert(c, "INSERT INTO t2 (id, value) VALUES (?,?);", JDBC.string("0101101011"), PostgreSQL.json("[1, \"2\", 3.14]"));
        
        throw new SQLException("Transaction will be automatically rolled back!");
    });

This example also demonstrates the use of JDBC extensions (in this case, support for the PostgreSQL JSON datatype).