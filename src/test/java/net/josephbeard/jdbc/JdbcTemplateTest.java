package net.josephbeard.jdbc;

import static net.josephbeard.jdbc.SQL.string;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class JdbcTemplateTest {

    @Mock
    private Connection connection;

    @Mock
    private ConnectionProvider connectionProvider;

    private JdbcTemplate subject;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);
        doReturn(connection).when(connectionProvider).getConnection();

        this.subject = new JdbcTemplate(connectionProvider);
    }

    @Test
    public void testApplyParameters() throws SQLException {

        String rowId = "12345";
        String company_name = "pepsico";
        String theNull = null;

        PreparedStatement statement = mock(PreparedStatement.class);
        ParameterMetaData metaData = mock(ParameterMetaData.class);

        when(statement.getParameterMetaData()).thenReturn(metaData);
        when(metaData.getParameterCount()).thenReturn(3);

        subject.applyParameters(statement, string(rowId), string(company_name), string(theNull));

        verify(statement, times(1)).setString(1, rowId);
        verify(statement, times(1)).setString(2, company_name);
        verify(statement, times(1)).setNull(3, Types.VARCHAR);

    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testApplyParametersInvalidParameterCount() throws SQLException {

        String rowId = "12345";
        String company_name = "pepsico";
        String theNull = null;

        PreparedStatement statement = mock(PreparedStatement.class);
        ParameterMetaData metaData = mock(ParameterMetaData.class);

        when(statement.getParameterMetaData()).thenReturn(metaData);
        when(metaData.getParameterCount()).thenReturn(4);

        subject.applyParameters(statement, string(rowId), string(company_name), string(theNull));
    }

    @Test
    public void testDoInTransaction_Commit() throws SQLException {
        final int expectedResult = 5;
        int result = subject.doInTransaction(c -> {
            verify(connection).setAutoCommit(false);
            return 5;
        });
        verify(connection).commit();
        assertThat("result", result, is(equalTo(expectedResult)));
    }

    @Test
    public void testDoInTransaction_Rollback() throws SQLException {
        final SQLException theException = new SQLException("Trigger a rollback");
        try {
            subject.doInTransaction(c -> {
                verify(connection).setAutoCommit(false);
                throw theException;
            });
            fail("Exception not thrown");
        } catch (SQLException ex) {
            verify(connection).rollback();
            assertThat(ex, is(sameInstance(theException)));
        }
    }

    @Test
    public void testApplyParameters_MetadataIsIgnoredWhenExplicitlySet() throws SQLException {
        String rowId = "12345";
        String company_name = "pepsico";
        String theNull = null;

        PreparedStatement statement = mock(PreparedStatement.class);

        this.subject = new JdbcTemplate(connectionProvider, true);
        this.subject.applyParameters(statement, string(rowId), string(company_name), string(theNull));

        verify(statement, never()).getParameterMetaData();

        verify(statement, times(1)).setString(1, rowId);
        verify(statement, times(1)).setString(2, company_name);
        verify(statement, times(1)).setNull(3, Types.VARCHAR);
    }

    @Test
    public void testApplyParameters_MetadataIsIgnoredWhenNotSupported() throws SQLException {
        String rowId = "12345";
        String company_name = "pepsico";
        String theNull = null;

        PreparedStatement statement = mock(PreparedStatement.class);

        when(statement.getParameterMetaData()).thenThrow(new SQLFeatureNotSupportedException());

        this.subject.applyParameters(statement, string(rowId), string(company_name), string(theNull));
        this.subject.applyParameters(statement, string(rowId), string(company_name), string(theNull));

        verify(statement, times(1)).getParameterMetaData();

        verify(statement, times(2)).setString(1, rowId);
        verify(statement, times(2)).setString(2, company_name);
        verify(statement, times(2)).setNull(3, Types.VARCHAR);
    }

    @Test
    public void testApplyParameters_MetadataIsIgnoredWhenBroken() throws SQLException {
        String rowId = "12345";
        String company_name = "pepsico";
        String theNull = null;

        PreparedStatement statement = mock(PreparedStatement.class);

        when(statement.getParameterMetaData()).thenReturn(null);

        this.subject.applyParameters(statement, string(rowId), string(company_name), string(theNull));
        this.subject.applyParameters(statement, string(rowId), string(company_name), string(theNull));

        verify(statement, times(1)).getParameterMetaData();

        verify(statement, times(2)).setString(1, rowId);
        verify(statement, times(2)).setString(2, company_name);
        verify(statement, times(2)).setNull(3, Types.VARCHAR);
    }

}
