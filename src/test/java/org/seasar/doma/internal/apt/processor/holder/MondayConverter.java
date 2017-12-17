package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.jdbc.holder.HolderConverter;

/**
 * @author taedium
 * 
 */
@ExternalHolder
public class MondayConverter implements HolderConverter<Monday, String> {

    @Override
    public String fromHolderToValue(Monday holder) {
        return null;
    }

    @Override
    public Monday fromValueToHolder(String value) {
        return null;
    }

}
