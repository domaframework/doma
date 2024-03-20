package org.seasar.doma.jdbc.query;

/** Retrieves the type of the duplicate key when inserting a new entity. */
public enum DuplicateKeyType {
  UPDATE,
  IGNORE,
  EXCEPTION,
  ;
}
