package org.seasar.doma.jdbc;

/** @author taedium */
public interface ResultParameter<RESULT> extends SqlParameter {

  RESULT getResult();
}
