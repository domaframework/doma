package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.wrapper.Wrapper;

public interface OutParameter<V> extends CallableSqlParameter {

    Wrapper<V> getWrapper();

    void update();

}
