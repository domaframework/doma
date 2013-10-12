package org.seasar.doma.internal.wrapper;

import org.seasar.doma.wrapper.Wrapper;

public interface Holder<BASIC, CONTAINER> {

    CONTAINER get();

    CONTAINER getDefault();

    void set(Object container);

    Wrapper<BASIC> getWrapper();
}
