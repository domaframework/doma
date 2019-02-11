package org.seasar.doma.internal.apt.meta.dao;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.DaoAnnot;

public class ParentDaoMeta {

  private final DaoAnnot daoAnnot;

  private final TypeElement typeElement;

  ParentDaoMeta(DaoAnnot daoAnnot, TypeElement typeElement) {
    assertNotNull(daoAnnot, typeElement);
    this.daoAnnot = daoAnnot;
    this.typeElement = typeElement;
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public boolean hasUserDefinedConfig() {
    return daoAnnot.hasUserDefinedConfig();
  }
}
