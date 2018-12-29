package org.seasar.doma.jdbc.command;

public interface Command<RESULT> {

  RESULT execute();
}
