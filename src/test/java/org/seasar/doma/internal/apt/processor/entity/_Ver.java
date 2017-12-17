package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

/**
 * @author taedium
 * 
 */
public class _Ver extends AbstractHolderDesc<Integer, Ver> {

    private _Ver() {
        super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
    }

    @Override
    public Ver newHolder(Integer value) {
        return null;
    }

    @Override
    public Integer getBasicValue(Ver holder) {
        return null;
    }

    @Override
    public Class<Integer> getBasicClass() {
        return null;
    }

    @Override
    public Class<Ver> getHolderClass() {
        return null;
    }

    public static _Ver getSingletonInternal() {
        return null;
    }

}
