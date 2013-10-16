package org.seasar.doma.internal.wrapper;

import org.seasar.doma.wrapper.Wrapper;

public interface Holder<BASIC, CONTAINER> {

    Class<BASIC> getBasicClass();

    Class<?> getDomainClass();

    boolean isOptionalHolder();

    CONTAINER cast(Object value);

    CONTAINER get();

    CONTAINER getDefault();

    void set(CONTAINER container);

    Wrapper<BASIC> getWrapper();

}
