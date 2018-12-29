package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.mirror.ProcedureMirror;
import org.seasar.doma.message.Message;

/** @author taedium */
public class AutoProcedureQueryMetaFactory
    extends AutoModuleQueryMetaFactory<AutoProcedureQueryMeta> {

  public AutoProcedureQueryMetaFactory(ProcessingEnvironment env) {
    super(env);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    ProcedureMirror procedureMirror = ProcedureMirror.newInstance(method, env);
    if (procedureMirror == null) {
      return null;
    }
    AutoProcedureQueryMeta queryMeta = new AutoProcedureQueryMeta(method, daoMeta.getDaoElement());
    queryMeta.setQueryKind(QueryKind.AUTO_PROCEDURE);
    queryMeta.setProcedureMirror(procedureMirror);
    doTypeParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }

  @Override
  protected void doReturnType(
      AutoProcedureQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    QueryReturnMeta resultMeta = createReturnMeta(queryMeta);
    if (!resultMeta.isPrimitiveVoid()) {
      throw new AptException(
          Message.DOMA4064,
          env,
          resultMeta.getMethodElement(),
          new Object[] {
            queryMeta.getDaoElement().getQualifiedName(),
            queryMeta.getMethodElement().getSimpleName()
          });
    }
    queryMeta.setReturnMeta(resultMeta);
  }
}
