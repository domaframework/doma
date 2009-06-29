package org.seasar.doma.internal.jdbc.query;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;

/**
 * @author taedium
 * 
 */
public interface Query {

    Sql<?> getSql();

    String getClassName();

    String getMethodName();

    Config getConfig();

    int getQueryTimeout();
}
