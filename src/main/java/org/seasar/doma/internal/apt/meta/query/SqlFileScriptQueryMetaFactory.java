package org.seasar.doma.internal.apt.meta.query;

import java.io.File;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.reflection.ScriptReflection;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.message.Message;

/** @author taedium */
public class SqlFileScriptQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlFileScriptQueryMeta> {

  public SqlFileScriptQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
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
    doSqlFiles(queryMeta, false, false);
    return queryMeta;
  }

  private SqlFileScriptQueryMeta createSqlFileScriptQueryMeta() {
    SqlFileScriptQueryMeta queryMeta = new SqlFileScriptQueryMeta(methodElement);
    ScriptReflection scriptReflection = ctx.getReflections().newScriptReflection(methodElement);
    if (scriptReflection == null) {
      return null;
    }
    queryMeta.setScriptReflection(scriptReflection);
    queryMeta.setQueryKind(QueryKind.SQLFILE_SCRIPT);
    return queryMeta;
  }

  @Override
  protected void doReturnType(SqlFileScriptQueryMeta queryMeta) {
    QueryReturnMeta returnMeta = createReturnMeta();
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
    String filePath =
        ScriptFileUtil.buildPath(
            getDaoElement().getQualifiedName().toString(), queryMeta.getName());
    File file = getFile(queryMeta, filePath);
    File[] siblingFiles = getSiblingFiles(queryMeta, file);
    String methodName = queryMeta.getName();
    for (File siblingFile : siblingFiles) {
      if (ScriptFileUtil.isScriptFile(siblingFile, methodName)) {
        String fileName = siblingFile.getName();
        queryMeta.addFileName(fileName);
      }
    }
  }
}
