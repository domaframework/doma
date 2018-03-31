package org.seasar.doma.internal.apt.meta.query;

import java.sql.NClob;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.Context;

public class NClobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<NClobCreateQueryMeta> {

  public NClobCreateQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement, NClob.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    var nClobFactoryAnnot = ctx.getAnnots().newNClobFactoryAnnot(methodElement);
    if (nClobFactoryAnnot == null) {
      return null;
    }
    var queryMeta = new NClobCreateQueryMeta(methodElement);
    queryMeta.setNClobFactoryAnnot(nClobFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.NCLOB_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
