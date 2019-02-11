package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.LinkedHashMap;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.BatchModifyAnnot;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.validator.BatchSqlValidator;
import org.seasar.doma.internal.apt.validator.SqlValidator;
import org.seasar.doma.message.Message;

public class SqlFileBatchModifyQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlFileBatchModifyQueryMeta> {

  public SqlFileBatchModifyQueryMetaFactory(Context ctx) {
    super(ctx);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    SqlFileBatchModifyQueryMeta queryMeta = createSqlFileBatchModifyQueryMeta(method, daoMeta);
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    doSqlTemplate(queryMeta, method, daoMeta, false, queryMeta.isPopulatable());
    return queryMeta;
  }

  private SqlFileBatchModifyQueryMeta createSqlFileBatchModifyQueryMeta(
      ExecutableElement method, DaoMeta daoMeta) {
    SqlFileBatchModifyQueryMeta queryMeta =
        new SqlFileBatchModifyQueryMeta(method, daoMeta.getTypeElement());
    SqlAnnot sqlAnnot = ctx.getAnnotations().newSqlAnnot(method);
    queryMeta.setSqlAnnot(sqlAnnot);
    BatchModifyAnnot batchModifyAnnot = ctx.getAnnotations().newBatchInsertAnnot(method);
    if (batchModifyAnnot != null && usesSqlTemplate(sqlAnnot, batchModifyAnnot)) {
      queryMeta.setBatchModifyAnnot(batchModifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_INSERT);
      return queryMeta;
    }
    batchModifyAnnot = ctx.getAnnotations().newBatchUpdateAnnot(method);
    if (batchModifyAnnot != null && usesSqlTemplate(sqlAnnot, batchModifyAnnot)) {
      queryMeta.setBatchModifyAnnot(batchModifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_UPDATE);
      return queryMeta;
    }
    batchModifyAnnot = ctx.getAnnotations().newBatchDeleteAnnot(method);
    if (batchModifyAnnot != null && usesSqlTemplate(sqlAnnot, batchModifyAnnot)) {
      queryMeta.setBatchModifyAnnot(batchModifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_DELETE);
      return queryMeta;
    }
    return null;
  }

  private boolean usesSqlTemplate(SqlAnnot sqlAnnot, BatchModifyAnnot batchModifyAnnot) {
    return sqlAnnot != null || batchModifyAnnot.getSqlFileValue();
  }

  @Override
  protected void doReturnType(
      SqlFileBatchModifyQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    EntityCtType entityCtType = queryMeta.getEntityType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isBatchResult(entityCtType)) {
        throw new AptException(Message.DOMA4223, returnMeta.getMethodElement(), new Object[] {});
      }
    } else {
      if (!returnMeta.isPrimitiveIntArray()) {
        throw new AptException(Message.DOMA4040, returnMeta.getMethodElement(), new Object[] {});
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(
      final SqlFileBatchModifyQueryMeta queryMeta,
      final ExecutableElement method,
      DaoMeta daoMeta) {
    List<? extends VariableElement> parameters = method.getParameters();
    int size = parameters.size();
    if (size != 1) {
      throw new AptException(Message.DOMA4002, method, new Object[] {});
    }
    QueryParameterMeta parameterMeta = createParameterMeta(parameters.get(0), queryMeta);
    IterableCtType iterableCtType =
        parameterMeta
            .getCtType()
            .accept(
                new SimpleCtTypeVisitor<IterableCtType, Void, RuntimeException>() {

                  @Override
                  protected IterableCtType defaultAction(CtType type, Void p)
                      throws RuntimeException {
                    throw new AptException(Message.DOMA4042, method, new Object[] {});
                  }

                  @Override
                  public IterableCtType visitIterableCtType(IterableCtType ctType, Void p)
                      throws RuntimeException {
                    return ctType;
                  }
                },
                null);
    CtType elementCtType = iterableCtType.getElementCtType();
    queryMeta.setElementCtType(elementCtType);
    queryMeta.setElementsParameterName(parameterMeta.getName());
    elementCtType.accept(
        new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

          @Override
          public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
            queryMeta.setEntityType(ctType);
            return null;
          }
        },
        null);
    queryMeta.addParameterMeta(parameterMeta);
    if (parameterMeta.isBindable()) {
      queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
    }
  }

  @Override
  protected SqlValidator createSqlValidator(
      ExecutableElement method,
      LinkedHashMap<String, TypeMirror> parameterTypeMap,
      String sqlFilePath,
      boolean expandable,
      boolean populatable) {
    return new BatchSqlValidator(
        ctx, method, parameterTypeMap, sqlFilePath, expandable, populatable);
  }
}
