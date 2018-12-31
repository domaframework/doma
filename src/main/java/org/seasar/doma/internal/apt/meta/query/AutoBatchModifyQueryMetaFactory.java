package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.mirror.BatchDeleteMirror;
import org.seasar.doma.internal.apt.mirror.BatchInsertMirror;
import org.seasar.doma.internal.apt.mirror.BatchModifyMirror;
import org.seasar.doma.internal.apt.mirror.BatchUpdateMirror;
import org.seasar.doma.message.Message;

public class AutoBatchModifyQueryMetaFactory
    extends AbstractQueryMetaFactory<AutoBatchModifyQueryMeta> {

  public AutoBatchModifyQueryMetaFactory(ProcessingEnvironment env) {
    super(env);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    AutoBatchModifyQueryMeta queryMeta = createAutoBatchModifyQueryMeta(method, daoMeta);
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }

  protected AutoBatchModifyQueryMeta createAutoBatchModifyQueryMeta(
      ExecutableElement method, DaoMeta daoMeta) {
    AutoBatchModifyQueryMeta queryMeta =
        new AutoBatchModifyQueryMeta(method, daoMeta.getDaoElement());
    BatchModifyMirror batchModifyMirror = BatchInsertMirror.newInstance(method, env);
    if (batchModifyMirror != null && !batchModifyMirror.getSqlFileValue()) {
      queryMeta.setBatchModifyMirror(batchModifyMirror);
      queryMeta.setQueryKind(QueryKind.AUTO_BATCH_INSERT);
      return queryMeta;
    }
    batchModifyMirror = BatchUpdateMirror.newInstance(method, env);
    if (batchModifyMirror != null && !batchModifyMirror.getSqlFileValue()) {
      queryMeta.setBatchModifyMirror(batchModifyMirror);
      queryMeta.setQueryKind(QueryKind.AUTO_BATCH_UPDATE);
      return queryMeta;
    }
    batchModifyMirror = BatchDeleteMirror.newInstance(method, env);
    if (batchModifyMirror != null && !batchModifyMirror.getSqlFileValue()) {
      queryMeta.setBatchModifyMirror(batchModifyMirror);
      queryMeta.setQueryKind(QueryKind.AUTO_BATCH_DELETE);
      return queryMeta;
    }
    return null;
  }

  @Override
  protected void doReturnType(
      AutoBatchModifyQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    EntityCtType entityCtType = queryMeta.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isBatchResult(entityCtType)) {
        throw new AptException(
            Message.DOMA4223,
            env,
            returnMeta.getMethodElement(),
            new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
      }
    } else {
      if (!returnMeta.isPrimitiveIntArray()) {
        throw new AptException(
            Message.DOMA4040,
            env,
            returnMeta.getMethodElement(),
            new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(
      AutoBatchModifyQueryMeta queryMeta, final ExecutableElement method, final DaoMeta daoMeta) {
    List<? extends VariableElement> parameters = method.getParameters();
    int size = parameters.size();
    if (size != 1) {
      throw new AptException(
          Message.DOMA4002,
          env,
          method,
          new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
    }
    final QueryParameterMeta parameterMeta = createParameterMeta(parameters.get(0), queryMeta);
    IterableCtType iterableCtType =
        parameterMeta
            .getCtType()
            .accept(
                new SimpleCtTypeVisitor<IterableCtType, Void, RuntimeException>() {

                  @Override
                  protected IterableCtType defaultAction(CtType ctType, Void p)
                      throws RuntimeException {
                    throw new AptException(
                        Message.DOMA4042,
                        env,
                        method,
                        new Object[] {
                          daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()
                        });
                  }

                  @Override
                  public IterableCtType visitIterableCtType(IterableCtType ctType, Void p)
                      throws RuntimeException {
                    return ctType;
                  }
                },
                null);
    EntityCtType entityCtType =
        iterableCtType
            .getElementCtType()
            .accept(
                new SimpleCtTypeVisitor<EntityCtType, Void, RuntimeException>() {

                  @Override
                  protected EntityCtType defaultAction(CtType ctType, Void p)
                      throws RuntimeException {
                    throw new AptException(
                        Message.DOMA4043,
                        env,
                        method,
                        new Object[] {
                          daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()
                        });
                  }

                  @Override
                  public EntityCtType visitEntityCtType(EntityCtType ctType, Void p)
                      throws RuntimeException {
                    return ctType;
                  }
                },
                null);
    queryMeta.setEntityCtType(entityCtType);
    queryMeta.setEntitiesParameterName(parameterMeta.getName());
    queryMeta.addParameterMeta(parameterMeta);
    if (parameterMeta.isBindable()) {
      queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
    }
    BatchModifyMirror batchModifyMirror = queryMeta.getBatchModifyMirror();
    validateEntityPropertyNames(
        entityCtType.getTypeMirror(),
        method,
        batchModifyMirror.getAnnotationMirror(),
        batchModifyMirror.getInclude(),
        batchModifyMirror.getExclude());
  }
}
