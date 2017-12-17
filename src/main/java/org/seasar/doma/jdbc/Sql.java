package org.seasar.doma.jdbc;

import java.util.List;

/**
 * An SQL.
 * 
 * <p>
 * The implementation class is not required to be thread safe.
 * 
 * @param <P>
 *            the parameter type
 */
public interface Sql<P extends SqlParameter> {

    /**
     * Returns the kind of the SQL.
     * 
     * @return the kind of the SQL
     */
    SqlKind getKind();

    /**
     * Returns the raw SQL string.
     * <p>
     * The bind variables are displayed as {@code ?}.
     * 
     * @return the raw SQL string
     */
    String getRawSql();

    /**
     * Returns the formatted SQL string.
     * <p>
     * The bind variables are replaced with the string representations of the
     * parameters.
     * 
     * @return the formatted SQL string
     */
    String getFormattedSql();

    /**
     * Returns the file path that contains this SQL.
     * 
     * @return the file path that contains this SQL、or {@code null} if this SQL
     *         is auto-generated
     */
    String getSqlFilePath();

    /**
     * Returns the parameter list.
     * 
     * @return the parameter list
     */
    List<P> getParameters();

    /**
     * Returns the type of the SQL log.
     * 
     * @return the type of the SQL log
     */
    SqlLogType getSqlLogType();
}
