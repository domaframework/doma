package org.seasar.doma.jdbc;

/** A result parameter. */
public interface ResultParameter<RESULT> extends SqlParameter {

  RESULT getResult();
}
