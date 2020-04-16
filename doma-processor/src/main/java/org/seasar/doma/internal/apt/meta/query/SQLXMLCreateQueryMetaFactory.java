package org.seasar.doma.internal.apt.meta.query;

import java.sql.SQLXML;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.SQLXMLFactoryAnnot;

public class SQLXMLCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<SQLXMLCreateQueryMeta> {

  public SQLXMLCreateQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement, SQLXML.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    SQLXMLFactoryAnnot sqlxmlFactoryAnnot =
        ctx.getAnnotations().newSQLXMLFactoryAnnot(methodElement);
    if (sqlxmlFactoryAnnot == null) {
      return null;
    }
    SQLXMLCreateQueryMeta queryMeta = new SQLXMLCreateQueryMeta(daoElement, methodElement);
    queryMeta.setSqlxmlFactoryAnnot(sqlxmlFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.SQLXML_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
