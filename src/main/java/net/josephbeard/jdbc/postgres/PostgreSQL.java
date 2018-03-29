package net.josephbeard.jdbc.postgres;

import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

import org.postgresql.util.PGobject;

import net.josephbeard.jdbc.ParameterValue;
import net.josephbeard.jdbc.SQL;

/**
 * Utility class for working with PostgreSQL.
 */
public abstract class PostgreSQL {

    /**
     * The PostgreSQL object type for a UUID value.
     */
    public static final String UUID_TYPE = "uuid";

    /**
     * The PostgreSQL object type for a JSON value.
     */
    public static final String JSON_TYPE = "json";

    /**
     * The PostgreSQL object type for a JSONB value.
     */
    public static final String JSONB_TYPE = "jsonb";

    public static ParameterValue uuid(UUID value) throws SQLException {
        if (value == null) {
            return SQL.nullValue(Types.JAVA_OBJECT); // TODO The PostgreSQL driver probably has something better
        }

        PGobject object = new PGobject();
        object.setType(UUID_TYPE);
        object.setValue(value.toString());
        return new PGobjectValue(object);
    }

    public static ParameterValue json(String value) throws SQLException {
        if (value == null) {
            return SQL.nullValue(Types.JAVA_OBJECT); // TODO The PostgreSQL driver probably has something better
        }
        PGobject object = new PGobject();
        object.setType(JSON_TYPE);
        object.setValue(value); // TODO Validate well-formed JSON?
        return new PGobjectValue(object);
    }

    public static ParameterValue jsonb(String value) throws SQLException {
        if (value == null) {
            return SQL.nullValue(Types.JAVA_OBJECT); // TODO The PostgreSQL driver probably has something better
        }
        PGobject object = new PGobject();
        object.setType(JSONB_TYPE);
        object.setValue(value); // TODO Validate well-formed JSON?
        return new PGobjectValue(object);
    }

    private PostgreSQL() {
        assert false : "PostgreSQL should not be instantiated.";
    }

}
