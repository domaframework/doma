package org.seasar.doma.internal.apt.sql;

import java.io.File;
import java.util.function.Consumer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.io.FileHelper;
import org.seasar.doma.internal.apt.meta.query.AbstractSqlTemplateQueryMeta;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.message.Message;

public class SqlFileLoader extends SqlTemplateLoader {

  private final FileHelper fileHelper;

  public SqlFileLoader(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement, null, null);
    this.fileHelper = new FileHelper(ctx, methodElement);
  }

  @Override
  public void execute(AbstractSqlTemplateQueryMeta queryMeta, Consumer<SqlTemplate> consumer) {
    var filePath =
        SqlFileUtil.buildPath(getDaoElement().getQualifiedName().toString(), queryMeta.getName());
    var file = fileHelper.getFile(filePath);
    var siblingFiles = fileHelper.getSiblingFiles(file);
    var dirPath = SqlFileUtil.buildPath(getDaoElement().getQualifiedName().toString());
    var methodName = queryMeta.getName();
    for (var siblingFile : siblingFiles) {
      if (SqlFileUtil.isSqlFile(siblingFile, methodName)) {
        var fileName = siblingFile.getName();
        var sqlFilePath = dirPath + "/" + fileName;
        var sql = loadSql(siblingFile, sqlFilePath);
        validateSql(sql, sqlFilePath);
        var sqlNode = createSqlNode(sql, sqlFilePath);
        var sqlTemplate = new SqlTemplate(ctx, methodElement, null, null, sqlFilePath, sqlNode);
        consumer.accept(sqlTemplate);
        queryMeta.addFileName(fileName);
      }
    }
  }

  private String loadSql(File file, String filePath) {
    try {
      return IOUtil.readAsString(file);
    } catch (WrapException e) {
      var cause = e.getCause();
      throw new AptException(
          Message.DOMA4068, methodElement, cause, new Object[] {filePath, cause});
    }
  }

  private TypeElement getDaoElement() {
    var element = methodElement.getEnclosingElement();
    var typeElement = ctx.getElements().toTypeElement(element);
    if (typeElement == null) {
      throw new AptIllegalStateException(methodElement.toString());
    }
    return typeElement;
  }
}
