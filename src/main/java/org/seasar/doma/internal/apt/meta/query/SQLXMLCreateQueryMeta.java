package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.reflection.SQLXMLFactoryReflection;

/** @author nakamura-to */
public class SQLXMLCreateQueryMeta extends AbstractCreateQueryMeta {

  private SQLXMLFactoryReflection sqlxmlFactoryReflection;

  public SQLXMLCreateQueryMeta(ExecutableElement method) {
    super(method);
  }

  public SQLXMLFactoryReflection getSqlxmlFactoryReflection() {
    return sqlxmlFactoryReflection;
  }

  public void setSqlxmlFactoryReflection(SQLXMLFactoryReflection sqlxmlFactoryMirror) {
    this.sqlxmlFactoryReflection = sqlxmlFactoryMirror;
  }
}
