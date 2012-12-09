package org.seasar.doma.it.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class AgeConverter implements DomainConverter<Age, Integer> {

    @Override
    public Integer fromDomainToValue(Age domain) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Age fromValueToDomain(Integer value) {
        // TODO Auto-generated method stub
        return null;
    }

}
