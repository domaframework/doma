package org.seasar.doma.internal.domain;

import org.seasar.doma.wrapper.Wrapper;

public interface DomainType<V, D> {

    D getDomain();

    Class<D> getDomainClass();

    Wrapper<V> getWrapper();
}
