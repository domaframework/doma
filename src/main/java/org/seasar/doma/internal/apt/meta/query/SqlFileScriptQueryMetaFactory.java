package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.File;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ScriptAnnot;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.message.Message;

public class SqlFileScriptQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlFileScriptQueryMeta> {

  public SqlFileScriptQueryMetaFactory(Context ctx) {
    super(ctx);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    SqlFileScriptQueryMeta queryMeta = createSqlFileScriptQueryMeta(method, daoMeta);
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    doSqlFiles(queryMeta, method, daoMeta, false, false);
    return queryMeta;
  }

  protected SqlFileScriptQueryMeta createSqlFileScriptQueryMeta(
      ExecutableElement method, DaoMeta daoMeta) {
    SqlFileScriptQueryMeta queryMeta = new SqlFileScriptQueryMeta(method, daoMeta.getDaoElement());
    ScriptAnnot scriptAnnot = ScriptAnnot.newInstance(method, ctx);
    if (scriptAnnot == null) {
      return null;
    }
    queryMeta.setScriptAnnot(scriptAnnot);
    queryMeta.setQueryKind(QueryKind.SQLFILE_SCRIPT);
    return queryMeta;
  }

  @Override
  protected void doReturnType(
      SqlFileScriptQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    if (!returnMeta.isPrimitiveVoid()) {
      throw new AptException(
          Message.DOMA4172,
          returnMeta.getMethodElement(),
          new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(
      SqlFileScriptQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    if (!method.getParameters().isEmpty()) {
      throw new AptException(
          Message.DOMA4173,
          method,
          new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
    }
  }

  @Override
  protected void doSqlFiles(
      SqlFileScriptQueryMeta queryMeta,
      ExecutableElement method,
      DaoMeta daoMeta,
      boolean expandable,
      boolean populatable) {
    String filePath =
        ScriptFileUtil.buildPath(
            daoMeta.getDaoElement().getQualifiedName().toString(), queryMeta.getName());
    File file = getFile(queryMeta, method, filePath);
    File[] siblingfiles = getSiblingFiles(queryMeta, method, file);
    String methodName = queryMeta.getName();
    for (File siblingfile : siblingfiles) {
      if (ScriptFileUtil.isScriptFile(siblingfile, methodName)) {
        String fileName = siblingfile.getName();
        queryMeta.addFileName(fileName);
      }
    }
  }
}
