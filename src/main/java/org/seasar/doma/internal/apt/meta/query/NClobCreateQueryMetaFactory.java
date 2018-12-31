package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.NClob;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.NClobFactoryAnnot;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;

public class NClobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<NClobCreateQueryMeta> {

  public NClobCreateQueryMetaFactory(ProcessingEnvironment env) {
    super(env, NClob.class);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    NClobFactoryAnnot nClobFactoryAnnot = NClobFactoryAnnot.newInstance(method, env);
    if (nClobFactoryAnnot == null) {
      return null;
    }
    NClobCreateQueryMeta queryMeta = new NClobCreateQueryMeta(method, daoMeta.getDaoElement());
    queryMeta.setNClobFactoryMirror(nClobFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.NCLOB_FACTORY);
    doTypeParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }
}
