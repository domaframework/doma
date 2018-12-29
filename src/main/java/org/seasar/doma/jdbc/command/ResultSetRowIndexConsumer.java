package org.seasar.doma.jdbc.command;

/** @author nakamura-to */
@FunctionalInterface
public interface ResultSetRowIndexConsumer {

  void accept(Long index, Boolean next);
}
