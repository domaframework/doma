package org.seasar.doma.internal.apt.meta.query;

import java.sql.SQLXML;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.Context;

public class SQLXMLCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<SQLXMLCreateQueryMeta> {

  public SQLXMLCreateQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement, SQLXML.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    var sqlxmlFactoryMirror = ctx.getAnnots().newSQLXMLFactoryAnnot(methodElement);
    if (sqlxmlFactoryMirror == null) {
      return null;
    }
    var queryMeta = new SQLXMLCreateQueryMeta(methodElement);
    queryMeta.setSqlxmlFactoryAnnot(sqlxmlFactoryMirror);
    queryMeta.setQueryKind(QueryKind.SQLXML_FACTORY);
    doAnnotation(queryMeta, sqlxmlFactoryMirror);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
