package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ModifyAnnot;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.message.Message;

public class SqlFileModifyQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlFileModifyQueryMeta> {

  public SqlFileModifyQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    SqlFileModifyQueryMeta queryMeta = createSqlFileModifyQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    doSqlFiles(queryMeta, false, queryMeta.isPopulatable());
    return queryMeta;
  }

  private SqlFileModifyQueryMeta createSqlFileModifyQueryMeta() {
    SqlFileModifyQueryMeta queryMeta = new SqlFileModifyQueryMeta(methodElement);
    ModifyAnnot modifyAnnot = ctx.getAnnots().newInsertAnnot(methodElement);
    if (modifyAnnot != null && modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_INSERT);
      return queryMeta;
    }
    modifyAnnot = ctx.getAnnots().newUpdateAnnot(methodElement);
    if (modifyAnnot != null && modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_UPDATE);
      return queryMeta;
    }
    modifyAnnot = ctx.getAnnots().newDeleteAnnot(methodElement);
    if (modifyAnnot != null && modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_DELETE);
      return queryMeta;
    }
    return null;
  }

  @Override
  protected void doReturnType(SqlFileModifyQueryMeta queryMeta) {
    QueryReturnMeta returnMeta = createReturnMeta();
    EntityCtType entityCtType = queryMeta.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isResult(entityCtType)) {
        throw new AptException(Message.DOMA4222, returnMeta.getMethodElement());
      }
    } else {
      if (!returnMeta.isPrimitiveInt()) {
        throw new AptException(Message.DOMA4001, returnMeta.getMethodElement());
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(final SqlFileModifyQueryMeta queryMeta) {
    for (VariableElement parameter : methodElement.getParameters()) {
      final QueryParameterMeta parameterMeta = createParameterMeta(parameter);
      queryMeta.addParameterMeta(parameterMeta);
      if (parameterMeta.isBindable()) {
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
      if (queryMeta.getEntityCtType() != null) {
        continue;
      }
      parameterMeta
          .getCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
                  queryMeta.setEntityCtType(ctType);
                  queryMeta.setEntityParameterName(parameterMeta.getName());
                  return null;
                }
              },
              null);
    }
  }
}
