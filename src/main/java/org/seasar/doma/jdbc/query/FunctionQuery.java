package org.seasar.doma.jdbc.query;

public interface FunctionQuery<RESULT> extends ModuleQuery {

  RESULT getResult();
}
