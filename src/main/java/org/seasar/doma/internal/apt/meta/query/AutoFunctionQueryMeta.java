package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.annot.FunctionAnnot;
import org.seasar.doma.internal.apt.meta.parameter.ResultParameterMeta;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoFunctionQueryMeta extends AutoModuleQueryMeta {

  private ResultParameterMeta resultParameterMeta;

  private FunctionAnnot functionAnnot;

  public AutoFunctionQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  public ResultParameterMeta getResultParameterMeta() {
    return resultParameterMeta;
  }

  public void setResultParameterMeta(ResultParameterMeta resultParameterMeta) {
    this.resultParameterMeta = resultParameterMeta;
  }

  public FunctionAnnot getFunctionAnnot() {
    return functionAnnot;
  }

  void setFunctionAnnot(FunctionAnnot functionAnnot) {
    this.functionAnnot = functionAnnot;
  }

  public String getCatalogName() {
    return functionAnnot.getCatalogValue();
  }

  public String getSchemaName() {
    return functionAnnot.getSchemaValue();
  }

  public String getFunctionName() {
    return functionAnnot.getNameValue();
  }

  public boolean isQuoteRequired() {
    return functionAnnot.getQuoteValue();
  }

  public int getQueryTimeout() {
    return functionAnnot.getQueryTimeoutValue();
  }

  public boolean getEnsureResultMapping() {
    return functionAnnot.getEnsureResultMappingValue();
  }

  @Override
  public MapKeyNamingType getMapKeyNamingType() {
    return functionAnnot.getMapKeyNamingValue();
  }

  public SqlLogType getSqlLogType() {
    return functionAnnot.getSqlLogValue();
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitAutoFunctionQueryMeta(this);
  }
}
