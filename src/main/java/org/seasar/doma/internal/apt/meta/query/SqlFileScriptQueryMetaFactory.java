package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.message.Message;

public class SqlFileScriptQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlFileScriptQueryMeta> {

  public SqlFileScriptQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    var queryMeta = createSqlFileScriptQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    doSqlFiles(queryMeta, false, false);
    return queryMeta;
  }

  private SqlFileScriptQueryMeta createSqlFileScriptQueryMeta() {
    var queryMeta = new SqlFileScriptQueryMeta(methodElement);
    var scriptAnnot = ctx.getAnnots().newScriptAnnot(methodElement);
    if (scriptAnnot == null) {
      return null;
    }
    queryMeta.setScriptAnnot(scriptAnnot);
    queryMeta.setQueryKind(QueryKind.SQLFILE_SCRIPT);
    return queryMeta;
  }

  @Override
  protected void doReturnType(SqlFileScriptQueryMeta queryMeta) {
    var returnMeta = createReturnMeta();
    if (!returnMeta.isPrimitiveVoid()) {
      throw new AptException(Message.DOMA4172, returnMeta.getMethodElement());
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(SqlFileScriptQueryMeta queryMeta) {
    if (!methodElement.getParameters().isEmpty()) {
      throw new AptException(Message.DOMA4173, methodElement);
    }
  }

  @Override
  protected void doSqlFiles(
      SqlFileScriptQueryMeta queryMeta, boolean expandable, boolean populatable) {
    var filePath =
        ScriptFileUtil.buildPath(
            getDaoElement().getQualifiedName().toString(), queryMeta.getName());
    var file = getFile(queryMeta, filePath);
    var siblingFiles = getSiblingFiles(queryMeta, file);
    var methodName = queryMeta.getName();
    for (var siblingFile : siblingFiles) {
      if (ScriptFileUtil.isScriptFile(siblingFile, methodName)) {
        var fileName = siblingFile.getName();
        queryMeta.addFileName(fileName);
      }
    }
  }
}
