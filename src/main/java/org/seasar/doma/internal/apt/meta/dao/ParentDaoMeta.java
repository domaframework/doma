package org.seasar.doma.internal.apt.meta.dao;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.mirror.DaoMirror;

public class ParentDaoMeta {

  protected final DaoMirror daoMirror;

  protected TypeMirror daoType;

  protected TypeElement daoElement;

  protected String name;

  public ParentDaoMeta(DaoMirror daoMirror) {
    assertNotNull(daoMirror);
    this.daoMirror = daoMirror;
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
    return daoMirror.hasUserDefinedConfig();
  }

  DaoMirror getDaoMirror() {
    return daoMirror;
  }

  public TypeMirror getConfigType() {
    return daoMirror.getConfigValue();
  }
}
