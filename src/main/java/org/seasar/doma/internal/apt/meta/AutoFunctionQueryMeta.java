package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.mirror.FunctionMirror;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoFunctionQueryMeta extends AutoModuleQueryMeta {

  protected ResultParameterMeta resultParameterMeta;

  protected FunctionMirror functionMirror;

  public AutoFunctionQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  public ResultParameterMeta getResultParameterMeta() {
    return resultParameterMeta;
  }

  public void setResultParameterMeta(ResultParameterMeta resultParameterMeta) {
    this.resultParameterMeta = resultParameterMeta;
  }

  FunctionMirror getFunctionMirror() {
    return functionMirror;
  }

  void setFunctionMirror(FunctionMirror functionMirror) {
    this.functionMirror = functionMirror;
  }

  public String getCatalogName() {
    return functionMirror.getCatalogValue();
  }

  public String getSchemaName() {
    return functionMirror.getSchemaValue();
  }

  public String getFunctionName() {
    return functionMirror.getNameValue();
  }

  public boolean isQuoteRequired() {
    return functionMirror.getQuoteValue();
  }

  public int getQueryTimeout() {
    return functionMirror.getQueryTimeoutValue();
  }

  public boolean getEnsureResultMapping() {
    return functionMirror.getEnsureResultMappingValue();
  }

  @Override
  public MapKeyNamingType getMapKeyNamingType() {
    return functionMirror.getMapKeyNamingValue();
  }

  public SqlLogType getSqlLogType() {
    return functionMirror.getSqlLogValue();
  }

  @Override
  public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
    return visitor.visitAutoFunctionQueryMeta(this, p);
  }
}
