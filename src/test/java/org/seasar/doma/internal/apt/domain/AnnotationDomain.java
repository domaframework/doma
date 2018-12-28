package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

@Domain(valueType = Integer.class, factoryMethod = "of")
public @interface AnnotationDomain {

  String getValue();
}
