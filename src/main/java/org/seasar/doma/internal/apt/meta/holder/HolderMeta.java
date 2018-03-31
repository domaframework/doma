package org.seasar.doma.internal.apt.meta.holder;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.HolderAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

public class HolderMeta implements TypeElementMeta {

  private final TypeElement holderElement;

  private final TypeMirror type;

  private final boolean parameterized;

  private final BasicCtType basicCtType;

  private final HolderAnnot holderAnnot;

  public HolderMeta(
      TypeElement typeElement, TypeMirror type, HolderAnnot holderAnnot, BasicCtType basicCtType) {
    assertNotNull(typeElement, type, holderAnnot, basicCtType);
    this.holderElement = typeElement;
    this.type = type;
    this.parameterized = !typeElement.getTypeParameters().isEmpty();
    this.holderAnnot = holderAnnot;
    this.basicCtType = basicCtType;
  }

  public TypeMirror getType() {
    return type;
  }

  public TypeElement getHolderElement() {
    return holderElement;
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  public TypeMirror getValueType() {
    return holderAnnot.getValueTypeValue();
  }

  public String getFactoryMethod() {
    return holderAnnot.getFactoryMethodValue();
  }

  public String getAccessorMethod() {
    return holderAnnot.getAccessorMethodValue();
  }

  public boolean getAcceptNull() {
    return holderAnnot.getAcceptNullValue();
  }

  public HolderAnnot getHolderAnnot() {
    return holderAnnot;
  }

  public boolean providesConstructor() {
    return "new".equals(holderAnnot.getFactoryMethodValue());
  }

  public boolean isParameterized() {
    return parameterized;
  }
}
