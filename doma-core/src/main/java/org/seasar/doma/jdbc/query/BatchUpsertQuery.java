package org.seasar.doma.jdbc.query;

public interface BatchUpsertQuery extends BatchInsertQuery {

  void setDuplicateKeyType(DuplicateKeyType duplicateKeyType);
}
