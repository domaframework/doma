package org.seasar.doma.jdbc;

/**
 * Defines the SQL kinds.
 */
public enum SqlKind {

    /** select */
    SELECT,

    /** insert */
    INSERT,

    /** update */
    UPDATE,

    /** delete */
    DELETE,

    /** batch insert */
    BATCH_INSERT,

    /** batch update */
    BATCH_UPDATE,

    /** batch delete */
    BATCH_DELETE,

    /** stored procedure */
    PROCEDURE,

    /** stored function */
    FUNCTION,

    /** script */
    SCRIPT,

    /** sql processor */
    SQL_PROCESSOR
}
