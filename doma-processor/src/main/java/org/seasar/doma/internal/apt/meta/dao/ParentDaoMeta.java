package org.seasar.doma.internal.apt.meta.dao;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.DaoAnnot;

public class ParentDaoMeta {

  private final DaoAnnot daoAnnot;

  private final TypeElement typeElement;

  private final List<ExecutableElement> methods;

  ParentDaoMeta(DaoAnnot daoAnnot, TypeElement typeElement, List<ExecutableElement> methods) {
    assertNotNull(daoAnnot, typeElement);
    this.daoAnnot = daoAnnot;
    this.typeElement = typeElement;
    this.methods = methods;
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public List<ExecutableElement> getMethods() {
    return methods;
  }

  public boolean hasUserDefinedConfig() {
    return daoAnnot.hasUserDefinedConfig();
  }
}
