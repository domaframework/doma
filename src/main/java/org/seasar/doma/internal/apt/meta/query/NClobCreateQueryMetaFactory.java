package org.seasar.doma.internal.apt.meta.query;

import java.sql.NClob;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.reflection.NClobFactoryReflection;

public class NClobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<NClobCreateQueryMeta> {

  public NClobCreateQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement, NClob.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    NClobFactoryReflection nClobFactoryReflection =
        ctx.getReflections().newNClobFactoryReflection(methodElement);
    if (nClobFactoryReflection == null) {
      return null;
    }
    NClobCreateQueryMeta queryMeta = new NClobCreateQueryMeta(methodElement);
    queryMeta.setNClobFactoryReflection(nClobFactoryReflection);
    queryMeta.setQueryKind(QueryKind.NCLOB_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
