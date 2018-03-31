package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.FetchType;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.cttype.CollectorCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.FunctionCtType;
import org.seasar.doma.internal.apt.cttype.SelectOptionsCtType;
import org.seasar.doma.internal.apt.reflection.SelectReflection;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlFileSelectQueryMeta extends AbstractSqlFileQueryMeta {

  private SelectReflection selectReflection;

  private String functionParameterName;

  private FunctionCtType functionCtType;

  private String collectorParameterName;

  private CollectorCtType collectorCtType;

  private String selectOptionsParameterName;

  private SelectOptionsCtType selectOptionsCtType;

  private EntityCtType entityCtType;

  private boolean resultStream;

  public SqlFileSelectQueryMeta(ExecutableElement method) {
    super(method);
  }

  public String getFunctionParameterName() {
    return functionParameterName;
  }

  public void setFunctionParameterName(String functionParameterName) {
    this.functionParameterName = functionParameterName;
  }

  public FunctionCtType getFunctionCtType() {
    return functionCtType;
  }

  public void setFunctionCtType(FunctionCtType functionCtType) {
    this.functionCtType = functionCtType;
  }

  public String getCollectorParameterName() {
    return collectorParameterName;
  }

  public void setCollectorParameterName(String collectorParameterName) {
    this.collectorParameterName = collectorParameterName;
  }

  public CollectorCtType getCollectorCtType() {
    return collectorCtType;
  }

  public void setCollectorCtType(CollectorCtType collectorCtType) {
    this.collectorCtType = collectorCtType;
  }

  public String getSelectOptionsParameterName() {
    return selectOptionsParameterName;
  }

  public void setSelectOptionsParameterName(String selectOptionsParameterName) {
    this.selectOptionsParameterName = selectOptionsParameterName;
  }

  public SelectOptionsCtType getSelectOptionsCtType() {
    return selectOptionsCtType;
  }

  public void setSelectOptionsCtType(SelectOptionsCtType selectOptionsCtType) {
    this.selectOptionsCtType = selectOptionsCtType;
  }

  public EntityCtType getEntityCtType() {
    return entityCtType;
  }

  public void setEntityCtType(EntityCtType entityCtType) {
    this.entityCtType = entityCtType;
  }

  public void setSelectReflection(SelectReflection selectReflection) {
    this.selectReflection = selectReflection;
  }

  public SelectReflection getSelectReflection() {
    return selectReflection;
  }

  public int getFetchSize() {
    return selectReflection.getFetchSizeValue();
  }

  public int getMaxRows() {
    return selectReflection.getMaxRowsValue();
  }

  public int getQueryTimeout() {
    return selectReflection.getQueryTimeoutValue();
  }

  public SelectType getSelectStrategyType() {
    return selectReflection.getStrategyValue();
  }

  public FetchType getFetchType() {
    return selectReflection.getFetchValue();
  }

  public boolean getEnsureResult() {
    return selectReflection.getEnsureResultValue();
  }

  public boolean getEnsureResultMapping() {
    return selectReflection.getEnsureResultMappingValue();
  }

  public MapKeyNamingType getMapKeyNamingType() {
    return selectReflection.getMapKeyNamingValue();
  }

  public SqlLogType getSqlLogType() {
    return selectReflection.getSqlLogValue();
  }

  public boolean isExpandable() {
    return entityCtType != null;
  }

  public boolean isResultStream() {
    return resultStream;
  }

  public void setResultStream(boolean resultStream) {
    this.resultStream = resultStream;
  }

  @Override
  public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
    visitor.visitSqlFileSelectQueryMeta(this, p);
  }
}
