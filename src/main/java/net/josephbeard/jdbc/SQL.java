package net.josephbeard.jdbc;

import org.apache.commons.lang3.Validate;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;

/**
 * Utility class for SQL.
 */
public abstract class SQL {

    public static ParameterValue nullValue(int sqlType) {
        return new NullValue(sqlType);
    }

    public static ParameterValue bit(Boolean value) {
        if (value == null) {
            return new NullValue(Types.BIT);
        }
        return BooleanValue.valueOf(value);
    }

    public static ParameterValue string(String value) {
        if (value == null) {
            return new NullValue(Types.VARCHAR);
        }
        return new StringValue(value);
    }

    public static ParameterValue string(Object value) {
        if (value == null) {
            return new NullValue(Types.VARCHAR);
        }
        return new StringValue(value.toString());
    }

    public static ParameterValue shortInt(short value) {
        return new ShortValue(value);
    }

    public static ParameterValue shortInt(Number value) {
        if (value == null) {
            return new NullValue(Types.SMALLINT);
        }
        return new ShortValue(value);
    }

    public static ParameterValue integer(int value) {
        return new IntegerValue(value);
    }

    public static ParameterValue integer(Number value) {
        if (value == null) {
            return new NullValue(Types.INTEGER);
        }
        return new IntegerValue(value);
    }

    public static ParameterValue longInt(long value) {
        return new LongValue(value);
    }

    public static ParameterValue longInt(Number value) {
        if (value == null) {
            return new NullValue(Types.BIGINT);
        }
        return new LongValue(value);
    }

    public static ParameterValue doubleValue(double value) {
        return new DoubleValue(value);
    }

    public static ParameterValue doubleValue(Number value) {
        if (value == null) {
            return new NullValue(Types.DOUBLE);
        }
        return new DoubleValue(value);
    }

    public static ParameterValue array(String typeName, Object... elements) {
        Validate.notBlank(typeName, "The typeName must not be blank");
        Validate.notNull(elements, "The elements must not be null");
        return new ArrayValue(typeName, elements);
    }

    public static ParameterValue bytes(byte... elements) {
        if (elements == null) {
            return new NullValue(Types.BINARY);
        }
        return new BytesValue(elements);
    }

    public static ParameterValue date(java.sql.Date value) {
        if (value == null) {
            return new NullValue(Types.DATE);
        }
        return new DateValue(value);
    }

    public static ParameterValue date(java.util.Date value) {
        if (value == null) {
            return new NullValue(Types.DATE);
        }
        return new DateValue(new java.sql.Date(value.getTime()));
    }

    public static ParameterValue time(java.sql.Time value) {
        if (value == null) {
            return new NullValue(Types.TIME);
        }
        return new TimeValue(value);
    }

    public static ParameterValue timestamp(Instant value) {
        if (value == null) {
            return new NullValue(Types.TIMESTAMP_WITH_TIMEZONE);
        }
        return new TimestampValue(Timestamp.from(value));
    }

    public static ParameterValue object(Object value) {
        if (value == null) {
            return new NullValue(Types.JAVA_OBJECT);
        }
        return new ObjectValue(value);
    }

    private SQL() {
        assert false : "SQL should not be instantiated";
    }

}
