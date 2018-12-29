package org.seasar.doma.jdbc.query;

/** @author taedium */
public interface BatchUpdateQuery extends BatchModifyQuery {

  void incrementVersions();
}
