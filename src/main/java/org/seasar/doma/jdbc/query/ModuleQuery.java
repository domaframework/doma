package org.seasar.doma.jdbc.query;

import org.seasar.doma.jdbc.CallableSql;
import org.seasar.doma.jdbc.SqlLogType;

public interface ModuleQuery extends Query {

  @Override
  CallableSql getSql();

  String getQualifiedName();

  SqlLogType getSqlLogType();
}
