
package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * Defines the SQL log formats.
 */
public enum SqlLogType {

    /**
     * The raw SQL.
     * <p>
     * The bind variables are displayed as {@code ?}.
     */
    RAW,

    /**
     * The formatted SQL.
     * <p>
     * The bind variables are replaced with the string representations of the
     * parameters. The string representations is determined by the object that
     * is return from {@link Dialect#getSqlLogFormattingVisitor()}.
     */
    FORMATTED,

    /**
     * No output.
     */
    NONE
}
