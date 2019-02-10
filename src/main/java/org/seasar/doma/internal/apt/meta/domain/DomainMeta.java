package org.seasar.doma.internal.apt.meta.domain;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.DomainAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.WrapperCtType;
import org.seasar.doma.internal.apt.def.TypeParametersDef;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

public class DomainMeta implements TypeElementMeta {

  private final TypeElement typeElement;

  private final TypeMirror type;

  private TypeParametersDef typeParametersDef;

  private BasicCtType basicCtType;

  private WrapperCtType wrapperCtType;

  private DomainAnnot domainAnnot;

  public DomainMeta(TypeElement typeElement, TypeMirror type) {
    assertNotNull(typeElement, type);
    this.typeElement = typeElement;
    this.type = type;
  }

  public TypeMirror getType() {
    return type;
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public void setTypeParametersDef(TypeParametersDef typeParametersDef) {
    this.typeParametersDef = typeParametersDef;
  }

  public List<String> getTypeVariables() {
    return typeParametersDef.getTypeVariables();
  }

  public List<String> getTypeParameters() {
    return typeParametersDef.getTypeParameters();
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  public void setBasicCtType(BasicCtType basicCtType) {
    this.basicCtType = basicCtType;
  }

  public WrapperCtType getWrapperCtType() {
    return wrapperCtType;
  }

  public void setWrapperCtType(WrapperCtType wrapperCtType) {
    this.wrapperCtType = wrapperCtType;
  }

  public TypeMirror getValueType() {
    return domainAnnot.getValueTypeValue();
  }

  public String getFactoryMethod() {
    return domainAnnot.getFactoryMethodValue();
  }

  public String getAccessorMethod() {
    return domainAnnot.getAccessorMethodValue();
  }

  public boolean getAcceptNull() {
    return domainAnnot.getAcceptNullValue();
  }

  DomainAnnot getDomainAnnot() {
    return domainAnnot;
  }

  void setDomainAnnot(DomainAnnot domainAnnot) {
    this.domainAnnot = domainAnnot;
  }

  public boolean providesConstructor() {
    return "new".equals(domainAnnot.getFactoryMethodValue());
  }

  public boolean isParameterized() {
    return !typeElement.getTypeParameters().isEmpty();
  }

  @Override
  public boolean isError() {
    return false;
  }
}
