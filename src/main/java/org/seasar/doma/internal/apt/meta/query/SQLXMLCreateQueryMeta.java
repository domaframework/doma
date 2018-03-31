package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.SQLXMLFactoryAnnot;

public class SQLXMLCreateQueryMeta extends AbstractCreateQueryMeta {

  private SQLXMLFactoryAnnot sqlxmlFactoryAnnot;

  public SQLXMLCreateQueryMeta(ExecutableElement method) {
    super(method);
  }

  public SQLXMLFactoryAnnot getSqlxmlFactoryAnnot() {
    return sqlxmlFactoryAnnot;
  }

  public void setSqlxmlFactoryAnnot(SQLXMLFactoryAnnot sqlxmlFactoryMirror) {
    this.sqlxmlFactoryAnnot = sqlxmlFactoryMirror;
  }
}
