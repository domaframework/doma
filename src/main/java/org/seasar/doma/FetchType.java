package org.seasar.doma;

/**
 * Defines how to fetch rows from an SQL SELECT result set.
 */
public enum FetchType {

    /**
     * Fetches all rows immediately.
     * <p>
     * More memory usage and less Database connected time.
     */
    EAGER,

    /**
     * Fetches rows gradually.
     * <p>
     * Less memory usage and more Database connected time.
     */
    LAZY
}
