package org.seasar.doma.internal.apt.meta.query;

import java.io.File;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ScriptAnnot;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.message.Message;

public class SqlFileScriptQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlFileScriptQueryMeta> {

  public SqlFileScriptQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    SqlFileScriptQueryMeta queryMeta = createSqlFileScriptQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    doSqlTemplate(queryMeta, false, false);
    return queryMeta;
  }

  private SqlFileScriptQueryMeta createSqlFileScriptQueryMeta() {
    SqlFileScriptQueryMeta queryMeta = new SqlFileScriptQueryMeta(daoElement, methodElement);
    ScriptAnnot scriptAnnot = ctx.getAnnotations().newScriptAnnot(methodElement);
    if (scriptAnnot == null) {
      return null;
    }
    queryMeta.setScriptAnnot(scriptAnnot);
    queryMeta.setQueryKind(QueryKind.SQLFILE_SCRIPT);
    SqlAnnot sqlAnnot = ctx.getAnnotations().newSqlAnnot(methodElement);
    queryMeta.setSqlAnnot(sqlAnnot);
    return queryMeta;
  }

  @Override
  protected void doReturnType(SqlFileScriptQueryMeta queryMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    if (!returnMeta.isPrimitiveVoid()) {
      throw new AptException(Message.DOMA4172, methodElement, new Object[] {});
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(SqlFileScriptQueryMeta queryMeta) {
    if (!methodElement.getParameters().isEmpty()) {
      throw new AptException(Message.DOMA4173, methodElement, new Object[] {});
    }
  }

  @Override
  void doSqlTemplate(SqlFileScriptQueryMeta queryMeta, boolean expandable, boolean populatable) {
    SqlAnnot sqlAnnot = queryMeta.getSqlAnnot();
    if (sqlAnnot != null) {
      return;
    }
    String filePath = queryMeta.getPath();
    File file = getFile(filePath);
    File[] siblingFiles = getSiblingFiles(file);
    String methodName = queryMeta.getName();
    for (File siblingFile : siblingFiles) {
      if (ScriptFileUtil.isScriptFile(siblingFile, methodName)) {
        String fileName = siblingFile.getName();
        queryMeta.addFileName(fileName);
      }
    }
  }
}
