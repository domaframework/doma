package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.MultiInsertAnnot;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoMultiInsertQueryMeta extends AbstractQueryMeta {

  private EntityCtType entityCtType;

  private String entityParameterName;

  private MultiInsertAnnot multiInsertAnnot;

  public AutoMultiInsertQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    super(daoElement, methodElement);
  }

  public EntityCtType getEntityCtType() {
    return entityCtType;
  }

  public void setEntityCtType(EntityCtType entityCtType) {
    this.entityCtType = entityCtType;
  }

  public String getEntityParameterName() {
    return entityParameterName;
  }

  public void setEntityParameterName(String entityParameterName) {
    this.entityParameterName = entityParameterName;
  }

  public MultiInsertAnnot getMultiInsertAnnot() {
    return multiInsertAnnot;
  }

  void setMultiInsertAnnot(MultiInsertAnnot multiInsertAnnot) {
    this.multiInsertAnnot = multiInsertAnnot;
  }

  public int getQueryTimeout() {
    return multiInsertAnnot.getQueryTimeoutValue();
  }

  public List<String> getInclude() {
    return multiInsertAnnot.getIncludeValue();
  }

  public List<String> getExclude() {
    return multiInsertAnnot.getExcludeValue();
  }

  public SqlLogType getSqlLogType() {
    return multiInsertAnnot.getSqlLogValue();
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitAutoMultiInsertQueryMeta(this);
  }
}
