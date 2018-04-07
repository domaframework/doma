package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.BatchModifyAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.expr.ExpressionValidator;
import org.seasar.doma.internal.apt.sql.BatchSqlValidator;
import org.seasar.doma.internal.apt.sql.SqlTemplate;
import org.seasar.doma.internal.apt.sql.SqlValidator;
import org.seasar.doma.message.Message;

public class SqlTemplateBatchModifyQueryMetaFactory
    extends AbstractSqlTemplateQueryMetaFactory<SqlTemplateBatchModifyQueryMeta> {

  public SqlTemplateBatchModifyQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    var queryMeta = createSqlFileBatchModifyQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doAnnotation(queryMeta, queryMeta.getBatchModifyAnnot());
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    doSqlTemplate(queryMeta);
    return queryMeta;
  }

  private SqlTemplateBatchModifyQueryMeta createSqlFileBatchModifyQueryMeta() {
    var queryMeta = new SqlTemplateBatchModifyQueryMeta(methodElement);
    BatchModifyAnnot batchModifyAnnot = ctx.getAnnots().newBatchInsertAnnot(methodElement);
    if (batchModifyAnnot != null && usesSqlFile()) {
      queryMeta.setBatchModifyAnnot(batchModifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_INSERT);
      return queryMeta;
    }
    batchModifyAnnot = ctx.getAnnots().newBatchUpdateAnnot(methodElement);
    if (batchModifyAnnot != null && usesSqlFile()) {
      queryMeta.setBatchModifyAnnot(batchModifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_UPDATE);
      return queryMeta;
    }
    batchModifyAnnot = ctx.getAnnots().newBatchDeleteAnnot(methodElement);
    if (batchModifyAnnot != null && usesSqlFile()) {
      queryMeta.setBatchModifyAnnot(batchModifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_DELETE);
      return queryMeta;
    }
    return null;
  }

  @Override
  protected void doReturnType(SqlTemplateBatchModifyQueryMeta queryMeta) {
    var returnMeta = createReturnMeta();
    var entityCtType = queryMeta.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isBatchResult(entityCtType)) {
        throw new AptException(Message.DOMA4223, returnMeta.getMethodElement());
      }
    } else {
      if (!returnMeta.isPrimitiveIntArray()) {
        throw new AptException(Message.DOMA4040, returnMeta.getMethodElement());
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(final SqlTemplateBatchModifyQueryMeta queryMeta) {
    var parameters = methodElement.getParameters();
    var size = parameters.size();
    if (size != 1) {
      throw new AptException(Message.DOMA4002, methodElement);
    }
    var parameterMeta = createParameterMeta(parameters.get(0));
    var iterableCtType =
        parameterMeta
            .getCtType()
            .accept(
                new SimpleCtTypeVisitor<IterableCtType, Void, RuntimeException>() {

                  @Override
                  protected IterableCtType defaultAction(CtType type, Void p)
                      throws RuntimeException {
                    throw new AptException(Message.DOMA4042, methodElement);
                  }

                  @Override
                  public IterableCtType visitIterableCtType(IterableCtType ctType, Void p)
                      throws RuntimeException {
                    return ctType;
                  }
                },
                null);
    var elementCtType = iterableCtType.getElementCtType();
    queryMeta.setElementCtType(elementCtType);
    queryMeta.setElementsParameterName(parameterMeta.getName());
    elementCtType.accept(
        new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

          @Override
          public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
            queryMeta.setEntityCtType(ctType);
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
      SqlTemplate sqlTemplate,
      ExpressionValidator expressionValidator,
      boolean expandable,
      boolean populatable) {
    return new BatchSqlValidator(
        ctx, methodElement, sqlTemplate, expressionValidator, expandable, populatable);
  }
}
