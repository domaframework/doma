package org.seasar.doma.internal.apt.meta.query;

import java.sql.Clob;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ClobFactoryAnnot;

public class ClobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<ClobCreateQueryMeta> {

  public ClobCreateQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement, Clob.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    ClobFactoryAnnot clobFactoryAnnot = ctx.getAnnotations().newClobFactoryAnnot(methodElement);
    if (clobFactoryAnnot == null) {
      return null;
    }
    ClobCreateQueryMeta queryMeta = new ClobCreateQueryMeta(daoElement, methodElement);
    queryMeta.setClobFactoryAnnot(clobFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.CLOB_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
