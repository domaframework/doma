package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

@Holder(valueType = int.class)
class PackagePrivateHolder {

    private final int value;

    PackagePrivateHolder(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }

}
