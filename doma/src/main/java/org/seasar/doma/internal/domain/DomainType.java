package org.seasar.doma.internal.domain;

public interface DomainType<V, D> {

    D newDomain(V value);

    Class<D> getDomainClass();

    DomainWrapper<V, D> getWrapper(D domain);
}
