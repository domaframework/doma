package org.seasar.doma.internal.jdbc.query;


/**
 * @author taedium
 * 
 */
public interface FunctionQuery<R> extends ModuleQuery {

    R getResult();
}
