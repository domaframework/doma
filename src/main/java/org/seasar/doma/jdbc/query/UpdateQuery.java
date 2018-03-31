package org.seasar.doma.jdbc.query;

/** An object used for building an SQL UPDATE statement. */
public interface UpdateQuery extends ModifyQuery {

  void incrementVersion();
}
