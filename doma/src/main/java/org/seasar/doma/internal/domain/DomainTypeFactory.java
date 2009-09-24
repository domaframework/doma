package org.seasar.doma.internal.domain;

public interface DomainTypeFactory<V, D> {

    DomainType<V, D> createDomainType();

}
