package org.seasar.doma.jdbc.query;

public interface UpsertQuery extends InsertQuery {
  void setDuplicateKeyType(DuplicateKeyType duplicateKeyType);
}
