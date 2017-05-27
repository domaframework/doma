package org.seasar.doma.internal.apt.holder;

import java.util.UUID;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.jdbc.holder.HolderConverter;

@ExternalHolder
public class UUIDConverter implements HolderConverter<UUID, byte[]> {

    @Override
    public byte[] fromHolderToValue(UUID holder) {
        return null;
    }

    @Override
    public UUID fromValueToHolder(byte[] value) {
        return null;
    }

}
