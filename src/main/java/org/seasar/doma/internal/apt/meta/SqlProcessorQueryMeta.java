package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;
import org.seasar.doma.internal.apt.mirror.SqlProcessorMirror;

public class SqlProcessorQueryMeta extends AbstractSqlFileQueryMeta {

  protected SqlProcessorMirror sqlProcessorMirror;

  protected String biFunctionParameterName;

  protected BiFunctionCtType biFunctionCtType;

  protected SqlProcessorQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  @Override
  public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
    return visitor.visitSqlProcessorQueryMeta(this, p);
  }

  public SqlProcessorMirror getSqlProcessorMirror() {
    return sqlProcessorMirror;
  }

  public void setSqlProcessorMirror(SqlProcessorMirror sqlProcessorMirror) {
    this.sqlProcessorMirror = sqlProcessorMirror;
  }

  public String getBiFunctionParameterName() {
    return biFunctionParameterName;
  }

  public void setBiFunctionParameterName(String biFunctionParameterName) {
    this.biFunctionParameterName = biFunctionParameterName;
  }

  public BiFunctionCtType getBiFunctionCtType() {
    return biFunctionCtType;
  }

  public void setBiFunctionCtType(BiFunctionCtType biFunctionCtType) {
    this.biFunctionCtType = biFunctionCtType;
  }
}
