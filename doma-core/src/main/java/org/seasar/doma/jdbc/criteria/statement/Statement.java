package org.seasar.doma.jdbc.criteria.statement;

public interface Statement<RESULT> extends Buildable {

  RESULT execute();
}
