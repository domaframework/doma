package org.seasar.doma.internal.wrapper;

import org.seasar.doma.wrapper.Wrapper;

public interface Holder<V, C> {

    C get();

    void set(Object container);

    Wrapper<V> getWrapper();
}
