package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.ArrayFactoryAnnot;

public class ArrayCreateQueryMeta extends AbstractCreateQueryMeta {

  protected String elementsParameterName;

  protected ArrayFactoryAnnot arrayFactoryAnnot;

  public ArrayCreateQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
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
  public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
    return visitor.visitArrayCreateQueryMeta(this, p);
  }
}
