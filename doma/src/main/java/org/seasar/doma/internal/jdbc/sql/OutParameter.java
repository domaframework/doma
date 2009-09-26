package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.wrapper.Wrapper;

public interface OutParameter extends CallableSqlParameter {

    Wrapper<?> getWrapper();

    void updateReference();

}
