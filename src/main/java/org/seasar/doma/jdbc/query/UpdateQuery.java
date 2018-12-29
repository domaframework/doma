package org.seasar.doma.jdbc.query;

public interface UpdateQuery extends ModifyQuery {

  void incrementVersion();
}
