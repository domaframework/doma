package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.jdbc.InParameter;

/**
 * @author nakamura-to
 * @since 2.3.0
 */
public interface SqlContext {

    <BASIC> void appendParameter(InParameter<BASIC> parameter);

    void appendSql(String sql);

    void cutBackSql(int length);
}
