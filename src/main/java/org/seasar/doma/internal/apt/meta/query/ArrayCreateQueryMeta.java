package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.ArrayFactoryAnnot;

public class ArrayCreateQueryMeta extends AbstractCreateQueryMeta {

  private String elementsParameterName;

  private ArrayFactoryAnnot arrayFactoryAnnot;

  public ArrayCreateQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  public String getParameterName() {
    return elementsParameterName;
  }

  public void setElementsParameterName(String elementsParameterName) {
    this.elementsParameterName = elementsParameterName;
  }

  public ArrayFactoryAnnot getArrayFactoryAnnot() {
    return arrayFactoryAnnot;
  }

  void setArrayFactoryAnnot(ArrayFactoryAnnot arrayFactoryAnnot) {
    this.arrayFactoryAnnot = arrayFactoryAnnot;
  }

  public String getArrayTypeName() {
    return arrayFactoryAnnot.getTypeNameValue();
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitArrayCreateQueryMeta(this);
  }
}
