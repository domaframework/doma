package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ProcedureAnnot;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.message.Message;

public class AutoProcedureQueryMetaFactory
    extends AutoModuleQueryMetaFactory<AutoProcedureQueryMeta> {

  public AutoProcedureQueryMetaFactory(Context ctx) {
    super(ctx);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    ProcedureAnnot procedureAnnot = ProcedureAnnot.newInstance(method, ctx);
    if (procedureAnnot == null) {
      return null;
    }
    AutoProcedureQueryMeta queryMeta = new AutoProcedureQueryMeta(method, daoMeta.getDaoElement());
    queryMeta.setQueryKind(QueryKind.AUTO_PROCEDURE);
    queryMeta.setProcedureAnnot(procedureAnnot);
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
          ctx.getEnv(),
          resultMeta.getMethodElement(),
          new Object[] {
            queryMeta.getDaoElement().getQualifiedName(),
            queryMeta.getMethodElement().getSimpleName()
          });
    }
    queryMeta.setReturnMeta(resultMeta);
  }
}
