package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.ArrayFactoryAnnot;

public class ArrayCreateQueryMeta extends AbstractCreateQueryMeta {

  private String elementsParameterName;

  private ArrayFactoryAnnot arrayFactoryAnnot;

  public ArrayCreateQueryMeta(ExecutableElement method) {
    super(method);
  }

  public String getParameterName() {
    return elementsParameterName;
  }

  public void setElementsParameterName(String elementsParameterName) {
    this.elementsParameterName = elementsParameterName;
  }

  void setArrayFactoryAnnot(ArrayFactoryAnnot arrayFactoryAnnot) {
    this.arrayFactoryAnnot = arrayFactoryAnnot;
  }

  public String getArrayTypeName() {
    return arrayFactoryAnnot.getTypeNameValue();
  }

  @Override
  public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
    visitor.visitArrayCreateQueryMeta(this, p);
  }
}
