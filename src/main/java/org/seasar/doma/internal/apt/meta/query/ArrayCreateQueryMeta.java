package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.reflection.ArrayFactoryReflection;

public class ArrayCreateQueryMeta extends AbstractCreateQueryMeta {

  private String elementsParameterName;

  private ArrayFactoryReflection arrayFactoryReflection;

  public ArrayCreateQueryMeta(ExecutableElement method) {
    super(method);
  }

  public String getParameterName() {
    return elementsParameterName;
  }

  public void setElementsParameterName(String elementsParameterName) {
    this.elementsParameterName = elementsParameterName;
  }

  void setArrayFactoryReflection(ArrayFactoryReflection arrayFactoryReflection) {
    this.arrayFactoryReflection = arrayFactoryReflection;
  }

  public String getArrayTypeName() {
    return arrayFactoryReflection.getTypeNameValue();
  }

  @Override
  public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
    visitor.visitArrayCreateQueryMeta(this, p);
  }
}
