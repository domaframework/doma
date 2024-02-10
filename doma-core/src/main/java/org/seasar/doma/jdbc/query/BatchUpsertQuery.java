package org.seasar.doma.jdbc.query;

import java.sql.Statement;

public interface BatchUpsertQuery extends BatchModifyQuery {

  boolean isBatchSupported();

  void generateId(Statement statement, int index);

  void setDuplicateKeyType(DuplicateKeyType duplicateKeyType);
}
