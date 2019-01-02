package org.seasar.doma.internal.apt.meta.domain;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

public class ExternalDomainMeta implements TypeElementMeta {

  protected final TypeElement typeElement;

  private BasicCtType basicCtType;

  protected String valueTypeName;

  protected TypeElement domainElement;

  public ExternalDomainMeta(TypeElement typeElement) {
    assertNotNull(typeElement);
    this.typeElement = typeElement;
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  public void setBasicCtType(BasicCtType basicCtType) {
    this.basicCtType = basicCtType;
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
