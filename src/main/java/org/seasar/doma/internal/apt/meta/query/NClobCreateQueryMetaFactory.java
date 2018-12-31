package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.NClob;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.mirror.NClobFactoryMirror;

public class NClobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<NClobCreateQueryMeta> {

  public NClobCreateQueryMetaFactory(ProcessingEnvironment env) {
    super(env, NClob.class);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    NClobFactoryMirror nClobFactoryMirror = NClobFactoryMirror.newInstance(method, env);
    if (nClobFactoryMirror == null) {
      return null;
    }
    NClobCreateQueryMeta queryMeta = new NClobCreateQueryMeta(method, daoMeta.getDaoElement());
    queryMeta.setNClobFactoryMirror(nClobFactoryMirror);
    queryMeta.setQueryKind(QueryKind.NCLOB_FACTORY);
    doTypeParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }
}
