package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.annot.DeleteAnnot;
import org.seasar.doma.internal.apt.annot.InsertAnnot;
import org.seasar.doma.internal.apt.annot.ModifyAnnot;
import org.seasar.doma.internal.apt.annot.UpdateAnnot;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.message.Message;

public class SqlFileModifyQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlFileModifyQueryMeta> {

  public SqlFileModifyQueryMetaFactory(ProcessingEnvironment env) {
    super(env);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    SqlFileModifyQueryMeta queryMeta = createSqlFileModifyQueryMeta(method, daoMeta);
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    doSqlFiles(queryMeta, method, daoMeta, false, queryMeta.isPopulatable());
    return queryMeta;
  }

  protected SqlFileModifyQueryMeta createSqlFileModifyQueryMeta(
      ExecutableElement method, DaoMeta daoMeta) {
    SqlFileModifyQueryMeta queryMeta = new SqlFileModifyQueryMeta(method, daoMeta.getDaoElement());
    ModifyAnnot modifyAnnot = InsertAnnot.newInstance(method, env);
    if (modifyAnnot != null && modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_INSERT);
      return queryMeta;
    }
    modifyAnnot = UpdateAnnot.newInstance(method, env);
    if (modifyAnnot != null && modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_UPDATE);
      return queryMeta;
    }
    modifyAnnot = DeleteAnnot.newInstance(method, env);
    if (modifyAnnot != null && modifyAnnot.getSqlFileValue()) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_DELETE);
      return queryMeta;
    }
    return null;
  }

  @Override
  protected void doReturnType(
      SqlFileModifyQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    EntityCtType entityCtType = queryMeta.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isResult(entityCtType)) {
        throw new AptException(
            Message.DOMA4222,
            env,
            returnMeta.getMethodElement(),
            new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
      }
    } else {
      if (!returnMeta.isPrimitiveInt()) {
        throw new AptException(
            Message.DOMA4001,
            env,
            returnMeta.getMethodElement(),
            new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(
      final SqlFileModifyQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    for (VariableElement parameter : method.getParameters()) {
      final QueryParameterMeta parameterMeta = createParameterMeta(parameter, queryMeta);
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
