package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AbstractAnnot;
import org.seasar.doma.internal.apt.io.FileHelper;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.message.Message;

public class StaticScriptQueryMetaFactory extends AbstractQueryMetaFactory<StaticScriptQueryMeta> {

  private final FileHelper fileHelper;

  public StaticScriptQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
    this.fileHelper = new FileHelper(ctx, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    var queryMeta = createSqlFileScriptQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doAnnotation(queryMeta, queryMeta.getScriptAnnot());
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    doScript(queryMeta);
    return queryMeta;
  }

  private StaticScriptQueryMeta createSqlFileScriptQueryMeta() {
    var queryMeta = new StaticScriptQueryMeta(methodElement);
    var scriptAnnot = ctx.getAnnots().newScriptAnnot(methodElement);
    if (scriptAnnot == null) {
      return null;
    }
    queryMeta.setScriptAnnot(scriptAnnot);
    queryMeta.setQueryKind(QueryKind.SQLFILE_SCRIPT);
    return queryMeta;
  }

  @Override
  protected void doAnnotation(StaticScriptQueryMeta queryMeta, AbstractAnnot targetAnnot) {
    if (sqlAnnot == null) {
      return;
    }
    if (!sqlAnnot.getValueValue().isEmpty() && sqlAnnot.getUseFileValue()) {
      throw new AptException(Message.DOMA4441, methodElement, sqlAnnot.getAnnotationMirror());
    }
  }

  @Override
  protected void doReturnType(StaticScriptQueryMeta queryMeta) {
    var returnMeta = createReturnMeta();
    if (!returnMeta.isPrimitiveVoid()) {
      throw new AptException(Message.DOMA4172, returnMeta.getMethodElement());
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(StaticScriptQueryMeta queryMeta) {
    if (!methodElement.getParameters().isEmpty()) {
      throw new AptException(Message.DOMA4173, methodElement);
    }
  }

  protected void doScript(StaticScriptQueryMeta queryMeta) {
    if (sqlAnnot == null || sqlAnnot.getUseFileValue()) {
      var filePath =
          ScriptFileUtil.buildPath(
              getDaoElement().getQualifiedName().toString(), queryMeta.getName());
      var file = fileHelper.getFile(filePath);
      var siblingFiles = fileHelper.getSiblingFiles(file);
      var methodName = queryMeta.getName();
      for (var siblingFile : siblingFiles) {
        if (ScriptFileUtil.isScriptFile(siblingFile, methodName)) {
          var fileName = siblingFile.getName();
          queryMeta.addFileName(fileName);
        }
      }
    }
  }
}
