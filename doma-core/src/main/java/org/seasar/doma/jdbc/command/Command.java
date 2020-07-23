package org.seasar.doma.jdbc.command;

import org.seasar.doma.jdbc.query.Query;

public interface Command<RESULT> {

  Query getQuery();

  RESULT execute();
}
