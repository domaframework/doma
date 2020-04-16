package org.seasar.doma.internal.apt.meta.query;

import java.sql.NClob;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.NClobFactoryAnnot;

public class NClobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<NClobCreateQueryMeta> {

  public NClobCreateQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement, NClob.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    NClobFactoryAnnot nClobFactoryAnnot = ctx.getAnnotations().newNClobFactoryAnnot(methodElement);
    if (nClobFactoryAnnot == null) {
      return null;
    }
    NClobCreateQueryMeta queryMeta = new NClobCreateQueryMeta(daoElement, methodElement);
    queryMeta.setNClobFactoryMirror(nClobFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.NCLOB_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
