package org.seasar.doma.internal.jdbc.query;

import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.SelectOptions;

/**
 * @author taedium
 * 
 */
public interface SelectQuery extends Query {

    PreparedSql getSql();

    SelectOptions getOptions();

    int getFetchSize();

    int getMaxRows();

}
