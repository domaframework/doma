package org.seasar.doma.jdbc.criteria.statement;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;

public interface Statement<RESULT> extends Buildable {

  RESULT execute(Config config);

  RESULT execute(Config config, SqlLogType sqlLogType);

  RESULT execute(Config config, SqlLogType sqlLogType, String comment);

  RESULT execute(Config config, String comment);

  RESULT execute(Config config, String comment, SqlLogType sqlLogType);
}
