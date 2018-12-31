package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Clob;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.ClobFactoryAnnot;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;

public class ClobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<ClobCreateQueryMeta> {

  public ClobCreateQueryMetaFactory(ProcessingEnvironment env) {
    super(env, Clob.class);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    ClobFactoryAnnot clobFactoryAnnot = ClobFactoryAnnot.newInstance(method, env);
    if (clobFactoryAnnot == null) {
      return null;
    }
    ClobCreateQueryMeta queryMeta = new ClobCreateQueryMeta(method, daoMeta.getDaoElement());
    queryMeta.setClobFactoryAnnot(clobFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.CLOB_FACTORY);
    doTypeParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }
}
