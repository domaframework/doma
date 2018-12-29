package org.seasar.doma.jdbc.query;

/** @author taedium */
public interface UpdateQuery extends ModifyQuery {

  void incrementVersion();
}
