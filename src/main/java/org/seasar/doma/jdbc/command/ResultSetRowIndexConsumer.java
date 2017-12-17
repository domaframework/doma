package org.seasar.doma.jdbc.command;

/**
 * This interface implementation checks the index of a raw in the result set.
 */
@FunctionalInterface
public interface ResultSetRowIndexConsumer {

    void accept(Long index, Boolean next);
}
