package org.seasar.doma.internal.apt.meta.dao;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.DaoAnnot;

public class ParentDaoMeta {

  private final DaoAnnot daoAnnot;

  private final TypeElement daoElement;

  public ParentDaoMeta(DaoAnnot daoAnnot, TypeElement daoElement) {
    assertNotNull(daoAnnot, daoElement);
    this.daoAnnot = daoAnnot;
    this.daoElement = daoElement;
  }

  public TypeMirror getDaoType() {
    return daoElement.asType();
  }

  public TypeElement getDaoElement() {
    return daoElement;
  }

  public boolean hasUserDefinedConfig() {
    return daoAnnot.hasUserDefinedConfig();
  }
}
