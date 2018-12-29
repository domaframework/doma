package org.seasar.doma.jdbc.query;

/**
 * @author taedium
 * @param <RESULT> 結果
 */
public interface FunctionQuery<RESULT> extends ModuleQuery {

  RESULT getResult();
}
