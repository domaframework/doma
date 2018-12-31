package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.SQLXML;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.SQLXMLFactoryAnnot;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;

public class SQLXMLCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<SQLXMLCreateQueryMeta> {

  public SQLXMLCreateQueryMetaFactory(ProcessingEnvironment env) {
    super(env, SQLXML.class);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    SQLXMLFactoryAnnot sqlxmlFactoryAnnot = SQLXMLFactoryAnnot.newInstance(method, env);
    if (sqlxmlFactoryAnnot == null) {
      return null;
    }
    SQLXMLCreateQueryMeta queryMeta = new SQLXMLCreateQueryMeta(method, daoMeta.getDaoElement());
    queryMeta.setSqlxmlFactoryAnnot(sqlxmlFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.SQLXML_FACTORY);
    doTypeParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }
}
