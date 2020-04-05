package org.seasar.doma.jdbc.query;

public interface BatchUpdateQuery extends BatchModifyQuery {

  void incrementVersions();
}
