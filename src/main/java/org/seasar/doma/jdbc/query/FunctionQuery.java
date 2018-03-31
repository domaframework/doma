package org.seasar.doma.jdbc.query;

/**
 * An object used for building a stored function statement.
 *
 * @param <RESULT> the result type
 */
public interface FunctionQuery<RESULT> extends ModuleQuery {

  RESULT getResult();
}
