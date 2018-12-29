package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.cttype.WrapperCtType;

public class ExternalDomainMeta implements TypeElementMeta {

  protected final TypeElement typeElement;

  protected WrapperCtType wrapperCtType;

  protected String valueTypeName;

  protected TypeElement domainElement;

  public ExternalDomainMeta(TypeElement typeElement) {
    assertNotNull(typeElement);
    this.typeElement = typeElement;
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public WrapperCtType getWrapperCtType() {
    return wrapperCtType;
  }

  public void setWrapperCtType(WrapperCtType wrapperCtType) {
    this.wrapperCtType = wrapperCtType;
  }

  public String getValueTypeName() {
    return valueTypeName;
  }

  public void setValueTypeName(String valueTypeName) {
    this.valueTypeName = valueTypeName;
  }

  public TypeElement getDomainElement() {
    return domainElement;
  }

  public void setDomainElement(TypeElement domainElement) {
    this.domainElement = domainElement;
  }

  @Override
  public boolean isError() {
    return false;
  }
}
