package org.seasar.doma.jdbc;

/**
 * Defines the kinds of pessimistic locking.
 */
public enum SelectForUpdateType {

    /** indicate to get a lock in the normal way */
    NORMAL,

    /** indicates to get a lock without waiting */
    NOWAIT,

    /** indicates to get a lock with waiting */
    WAIT
}
