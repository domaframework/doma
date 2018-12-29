package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.TypeElement;

/** @author taedium */
public interface TypeElementMetaFactory<M extends TypeElementMeta> {

  M createTypeElementMeta(TypeElement typeElement);
}
