package org.seasar.doma.internal.apt.meta.query;

import java.sql.SQLXML;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.reflection.SQLXMLFactoryReflection;

public class SQLXMLCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<SQLXMLCreateQueryMeta> {

  public SQLXMLCreateQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement, SQLXML.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    SQLXMLFactoryReflection sqlxmlFactoryMirror =
        ctx.getReflections().newSQLXMLFactoryReflection(methodElement);
    if (sqlxmlFactoryMirror == null) {
      return null;
    }
    SQLXMLCreateQueryMeta queryMeta = new SQLXMLCreateQueryMeta(methodElement);
    queryMeta.setSqlxmlFactoryReflection(sqlxmlFactoryMirror);
    queryMeta.setQueryKind(QueryKind.SQLXML_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
