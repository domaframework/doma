package org.seasar.doma.internal.apt.meta.dao;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.DaoAnnot;

public class ParentDaoMeta {

  protected final DaoAnnot daoAnnot;

  protected TypeMirror daoType;

  protected TypeElement daoElement;

  protected String name;

  public ParentDaoMeta(DaoAnnot daoAnnot) {
    assertNotNull(daoAnnot);
    this.daoAnnot = daoAnnot;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TypeMirror getDaoType() {
    return daoType;
  }

  public void setDaoType(TypeMirror daoType) {
    this.daoType = daoType;
  }

  public TypeElement getDaoElement() {
    return daoElement;
  }

  public void setDaoElement(TypeElement daoElement) {
    this.daoElement = daoElement;
  }

  public boolean hasUserDefinedConfig() {
    return daoAnnot.hasUserDefinedConfig();
  }

  DaoAnnot getDaoAnnot() {
    return daoAnnot;
  }

  public TypeMirror getConfigType() {
    return daoAnnot.getConfigValue();
  }
}
