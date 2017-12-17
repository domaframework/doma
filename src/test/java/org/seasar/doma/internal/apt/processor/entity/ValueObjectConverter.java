package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.internal.apt.processor.entity.ValueObjectConverter.ValueObject;
import org.seasar.doma.jdbc.holder.HolderConverter;

/**
 * @author taedium
 * 
 */
@ExternalHolder
public class ValueObjectConverter implements HolderConverter<ValueObject, String> {

    @Override
    public String fromHolderToValue(ValueObject holder) {
        return null;
    }

    @Override
    public ValueObject fromValueToHolder(String value) {
        return null;
    }

    public static class ValueObject {
    }
}
