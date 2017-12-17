package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlLogFormatter;

/**
 * An object that converts JDBC data from SQL types to Java types, and vice
 * versa.
 * <p>
 * The implementation instance must be thread safe.
 * <p>
 * 
 * @param <T>
 *            the Java type
 */
public interface JdbcType<T> extends SqlLogFormatter<T> {

    /**
     * Retrieves the column value from the {@link ResultSet} object.
     * 
     * @param resultSet
     *            the result set
     * @param index
     *            the column index
     * @return the column value
     * @throws DomaNullPointerException
     *             if {@code resultSet} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code index} is {@literal 1} and below
     * @throws SQLException
     *             if {@code resultSet} throws {@link SQLException}
     */
    T getValue(ResultSet resultSet, int index) throws DomaNullPointerException, SQLException;

    /**
     * Sets the value to the {@link PreparedStatement} object.
     * 
     * @param preparedStatement
     *            the prepared statement
     * @param index
     *            the parameter index
     * @param value
     *            値
     * @throws DomaNullPointerException
     *             if {@code preparedStatement} is {@code null}
     * @throws DomaIllegalArgumentException
     *             {@code index} is {@literal 1} and below
     * @throws SQLException
     *             if {@code preparedStatement} throws {@link SQLException}
     */
    void setValue(PreparedStatement preparedStatement, int index, T value) throws SQLException;

    /**
     * Registers the OUT parameter to the {@link CallableStatement} object.
     * 
     * @param callableStatement
     *            the callable statement
     * @param index
     *            the parameter index
     * @throws DomaNullPointerException
     *             if {@code callableStatement} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code index} is {@literal 1} and below
     * @throws SQLException
     *             if {@code callableStatement} throws {@link SQLException}
     */
    void registerOutParameter(CallableStatement callableStatement, int index) throws SQLException;

    /**
     * Retrieves the value from the {@link CallableStatement} object.
     * 
     * @param callableStatement
     *            the callable statement
     * @param index
     *            the parameter index
     * @return the value
     * @throws DomaNullPointerException
     *             if {@code callableStatement} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code index} is {@literal 1} and below
     * @throws SQLException
     *             if {@code callableStatement} throws {@link SQLException}
     */
    T getValue(CallableStatement callableStatement, int index) throws SQLException;

}
