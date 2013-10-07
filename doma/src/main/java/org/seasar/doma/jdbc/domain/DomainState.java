package org.seasar.doma.jdbc.domain;

import org.seasar.doma.wrapper.Wrapper;

public interface DomainState<V, D> {

    D get();

    void set(D domain);

    Wrapper<V> getWrapper();
}
