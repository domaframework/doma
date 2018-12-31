package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.mirror.DeleteMirror;
import org.seasar.doma.internal.apt.mirror.InsertMirror;
import org.seasar.doma.internal.apt.mirror.ModifyMirror;
import org.seasar.doma.internal.apt.mirror.UpdateMirror;
import org.seasar.doma.message.Message;

public class AutoModifyQueryMetaFactory extends AbstractQueryMetaFactory<AutoModifyQueryMeta> {

  public AutoModifyQueryMetaFactory(ProcessingEnvironment env) {
    super(env);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    AutoModifyQueryMeta queryMeta = createAutoModifyQueryMeta(method, daoMeta);
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }

  protected AutoModifyQueryMeta createAutoModifyQueryMeta(
      ExecutableElement method, DaoMeta daoMeta) {
    AutoModifyQueryMeta queryMeta = new AutoModifyQueryMeta(method, daoMeta.getDaoElement());
    ModifyMirror modifyMirror = InsertMirror.newInstance(method, env);
    if (modifyMirror != null && !modifyMirror.getSqlFileValue()) {
      queryMeta.setModifyMirror(modifyMirror);
      queryMeta.setQueryKind(QueryKind.AUTO_INSERT);
      return queryMeta;
    }
    modifyMirror = UpdateMirror.newInstance(method, env);
    if (modifyMirror != null && !modifyMirror.getSqlFileValue()) {
      queryMeta.setModifyMirror(modifyMirror);
      queryMeta.setQueryKind(QueryKind.AUTO_UPDATE);
      return queryMeta;
    }
    modifyMirror = DeleteMirror.newInstance(method, env);
    if (modifyMirror != null && !modifyMirror.getSqlFileValue()) {
      queryMeta.setModifyMirror(modifyMirror);
      queryMeta.setQueryKind(QueryKind.AUTO_DELETE);
      return queryMeta;
    }
    return null;
  }

  @Override
  protected void doReturnType(
      AutoModifyQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
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
      AutoModifyQueryMeta queryMeta, final ExecutableElement method, DaoMeta daoMeta) {
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
    EntityCtType entityCtType =
        parameterMeta
            .getCtType()
            .accept(
                new SimpleCtTypeVisitor<EntityCtType, Void, RuntimeException>() {

                  @Override
                  protected EntityCtType defaultAction(CtType type, Void p)
                      throws RuntimeException {
                    throw new AptException(
                        Message.DOMA4003,
                        env,
                        parameterMeta.getElement(),
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
    queryMeta.setEntityParameterName(parameterMeta.getName());
    queryMeta.addParameterMeta(parameterMeta);
    if (parameterMeta.isBindable()) {
      queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
    }
    ModifyMirror modifyMirror = queryMeta.getModifyMirror();
    validateEntityPropertyNames(
        entityCtType.getTypeMirror(),
        method,
        modifyMirror.getAnnotationMirror(),
        modifyMirror.getInclude(),
        modifyMirror.getExclude());
  }
}
