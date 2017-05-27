package org.seasar.doma.internal.apt.holder;

import org.seasar.doma.Holder;

@Holder(valueType = Integer.class, factoryMethod = "of")
public @interface AnnotationHolder {

    String getValue();
}
