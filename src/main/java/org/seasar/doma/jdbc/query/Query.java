package org.seasar.doma.jdbc.query;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;

public interface Query {

  Sql<?> getSql();

  String getClassName();

  String getMethodName();

  Method getMethod();

  Config getConfig();

  int getQueryTimeout();

  void prepare();

  void complete();

  String comment(String sql);
}
