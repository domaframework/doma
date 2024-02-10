package org.seasar.doma.jdbc.query;

import java.sql.Statement;

public interface UpsertQuery extends ModifyQuery {

  void generateId(Statement statement);

  void setDuplicateKeyType(DuplicateKeyType duplicateKeyType);
}
