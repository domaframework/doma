package org.seasar.doma.internal.domain;

public interface DomainTypeFactory<D> {

    DomainType<D> createDomainType();

    DomainType<D> createDomainType(D domain);
}
