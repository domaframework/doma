package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.SQLXMLFactoryAnnot;

public class SQLXMLCreateQueryMeta extends AbstractCreateQueryMeta {

  private SQLXMLFactoryAnnot sqlxmlFactoryAnnot;

  public SQLXMLCreateQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  public SQLXMLFactoryAnnot getSqlxmlFactoryAnnot() {
    return sqlxmlFactoryAnnot;
  }

  void setSqlxmlFactoryAnnot(SQLXMLFactoryAnnot sqlxmlFactoryAnnot) {
    this.sqlxmlFactoryAnnot = sqlxmlFactoryAnnot;
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitSQLXMLCreateQueryMeta(this);
  }
}
