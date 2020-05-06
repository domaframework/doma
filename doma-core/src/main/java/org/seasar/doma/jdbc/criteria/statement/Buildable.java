package org.seasar.doma.jdbc.criteria.statement;

import org.seasar.doma.jdbc.Sql;

public interface Buildable {
  Sql<?> asSql();
}
