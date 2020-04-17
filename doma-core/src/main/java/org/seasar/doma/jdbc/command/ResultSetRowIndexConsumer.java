package org.seasar.doma.jdbc.command;

@FunctionalInterface
public interface ResultSetRowIndexConsumer {

  void accept(Long index, Boolean next);
}
