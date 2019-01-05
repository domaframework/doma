package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.SqlProcessorAnnot;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;

public class SqlProcessorQueryMeta extends AbstractSqlFileQueryMeta {

  private SqlProcessorAnnot sqlProcessorAnnot;

  private String biFunctionParameterName;

  private BiFunctionCtType biFunctionCtType;

  protected SqlProcessorQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  @Override
  public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
    return visitor.visitSqlProcessorQueryMeta(this, p);
  }

  public SqlProcessorAnnot getSqlProcessorAnnot() {
    return sqlProcessorAnnot;
  }

  public void setSqlProcessorAnnot(SqlProcessorAnnot sqlProcessorAnnot) {
    this.sqlProcessorAnnot = sqlProcessorAnnot;
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
