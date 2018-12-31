package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Clob;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.mirror.ClobFactoryMirror;

public class ClobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<ClobCreateQueryMeta> {

  public ClobCreateQueryMetaFactory(ProcessingEnvironment env) {
    super(env, Clob.class);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    ClobFactoryMirror clobFactoryMirror = ClobFactoryMirror.newInstance(method, env);
    if (clobFactoryMirror == null) {
      return null;
    }
    ClobCreateQueryMeta queryMeta = new ClobCreateQueryMeta(method, daoMeta.getDaoElement());
    queryMeta.setClobFactoryMirror(clobFactoryMirror);
    queryMeta.setQueryKind(QueryKind.CLOB_FACTORY);
    doTypeParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }
}
