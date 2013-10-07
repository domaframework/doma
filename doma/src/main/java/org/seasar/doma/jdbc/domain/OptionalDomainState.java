package org.seasar.doma.jdbc.domain;

import java.util.Optional;

import org.seasar.doma.wrapper.Wrapper;

public interface OptionalDomainState<V, D> {

    Optional<D> get();

    void set(Optional<D> domain);

    Wrapper<V> getWrapper();
}
