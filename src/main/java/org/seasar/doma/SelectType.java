package org.seasar.doma;

import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Defines strategies for handling an object that is mapped to a result set.
 */
public enum SelectType {

    /**
     * The object is returned from a method.
     */
    RETURN,

    /**
     * The object is handled by using {@link Stream}.
     */
    STREAM,

    /**
     * The object is handled by using {@link Collector}.
     */
    COLLECT;
}
