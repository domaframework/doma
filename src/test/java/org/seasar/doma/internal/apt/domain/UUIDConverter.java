package org.seasar.doma.internal.apt.domain;

import java.util.UUID;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class UUIDConverter implements DomainConverter<UUID, byte[]> {

    @Override
    public byte[] fromDomainToValue(UUID domain) {
        return null;
    }

    @Override
    public UUID fromValueToDomain(byte[] value) {
        return null;
    }

}
