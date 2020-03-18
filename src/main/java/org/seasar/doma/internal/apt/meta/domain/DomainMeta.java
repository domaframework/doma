package org.seasar.doma.internal.apt.meta.domain;

import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

public interface DomainMeta extends TypeElementMeta {
  TypeMirror getType();

  TypeElement getTypeElement();

  List<String> getTypeVariables();

  List<String> getTypeParameters();

  BasicCtType getBasicCtType();

  TypeMirror getValueType();

  String getFactoryMethod();

  String getAccessorMethod();

  boolean getAcceptNull();

  boolean providesConstructor();

  boolean isParameterized();
}
