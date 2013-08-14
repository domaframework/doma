package org.seasar.doma.it.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class LocationConverter implements DomainConverter<Location<?>, String> {

    @Override
    public String fromDomainToValue(Location<?> domain) {
        return domain.getValue();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Location<?> fromValueToDomain(String value) {
        return new Location(value);
    }

}
