package org.seasar.doma.jdbc.criteria.statement;

import org.seasar.doma.jdbc.SqlLogType;

public interface Statement<RESULT> extends Buildable {

  RESULT execute();

  RESULT execute(SqlLogType sqlLogType);

  RESULT execute(SqlLogType sqlLogType, String comment);

  RESULT execute(String comment);

  RESULT execute(String comment, SqlLogType sqlLogType);
}
