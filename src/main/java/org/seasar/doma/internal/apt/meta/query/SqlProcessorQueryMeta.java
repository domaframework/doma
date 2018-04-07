package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.SqlProcessorAnnot;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;

public class SqlProcessorQueryMeta extends AbstractSqlTemplateQueryMeta {

  private SqlProcessorAnnot sqlProcessorAnnot;

  private String biFunctionParameterName;

  private BiFunctionCtType biFunctionCtType;

  protected SqlProcessorQueryMeta(ExecutableElement method) {
    super(method);
  }

  @Override
  public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
    visitor.visitSqlProcessorQueryMeta(this, p);
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
