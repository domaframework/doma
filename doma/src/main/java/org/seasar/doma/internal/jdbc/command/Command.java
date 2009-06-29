package org.seasar.doma.internal.jdbc.command;

import org.seasar.doma.internal.jdbc.query.Query;

/**
 * @author taedium
 * 
 */
public interface Command<R, Q extends Query> {

    R execute();

}
