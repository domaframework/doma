package org.seasar.doma.internal.apt.meta.query;

import java.sql.Clob;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ClobFactoryAnnot;

public class ClobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<ClobCreateQueryMeta> {

  public ClobCreateQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement, Clob.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    ClobFactoryAnnot clobFactoryAnnot = ctx.getAnnots().newClobFactoryAnnot(methodElement);
    if (clobFactoryAnnot == null) {
      return null;
    }
    ClobCreateQueryMeta queryMeta = new ClobCreateQueryMeta(methodElement);
    queryMeta.setClobFactoryAnnot(clobFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.CLOB_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
