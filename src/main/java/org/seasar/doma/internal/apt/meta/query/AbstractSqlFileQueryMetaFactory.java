package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.LinkedHashMap;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.validator.SqlValidator;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

/** @author taedium */
public abstract class AbstractSqlFileQueryMetaFactory<M extends AbstractSqlFileQueryMeta>
    extends AbstractQueryMetaFactory<M> {

  protected AbstractSqlFileQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
  }

  protected void doSqlFiles(M queryMeta, boolean expandable, boolean populatable) {
    if (!ctx.getOptions().getSqlValidation()) {
      return;
    }
    String filePath =
        SqlFileUtil.buildPath(getDaoElement().getQualifiedName().toString(), queryMeta.getName());
    File file = getFile(queryMeta, filePath);
    File[] siblingFiles = getSiblingFiles(queryMeta, file);
    String dirPath = SqlFileUtil.buildPath(getDaoElement().getQualifiedName().toString());
    String methodName = queryMeta.getName();
    for (File siblingFile : siblingFiles) {
      if (SqlFileUtil.isSqlFile(siblingFile, methodName)) {
        String fileName = siblingFile.getName();
        String sqlFilePath = dirPath + "/" + fileName;
        String sql = getSql(siblingFile, sqlFilePath);
        if (sql.isEmpty() || StringUtil.isWhitespace(sql)) {
          throw new AptException(Message.DOMA4020, methodElement, new Object[] {sqlFilePath});
        }
        SqlNode sqlNode = createSqlNode(queryMeta, sqlFilePath, sql);
        SqlValidator validator =
            createSqlValidator(
                queryMeta.getBindableParameterTypeMap(), sqlFilePath, expandable, populatable);
        validator.validate(sqlNode);
        queryMeta.addFileName(fileName);
      }
    }
  }

  protected File getFile(M queryMeta, String filePath) {
    FileObject fileObject = getFileObject(filePath);
    URI uri = fileObject.toUri();
    if (!uri.isAbsolute()) {
      uri = new File(".").toURI().resolve(uri);
    }
    File file = getCanonicalFile(new File(uri));
    if (!file.exists()) {
      throw new AptException(
          Message.DOMA4019, methodElement, new Object[] {filePath, file.getAbsolutePath()});
    }
    if (file.isDirectory()) {
      throw new AptException(
          Message.DOMA4021, methodElement, new Object[] {filePath, file.getAbsolutePath()});
    }
    if (!IOUtil.endsWith(file, filePath)) {
      throw new AptException(
          Message.DOMA4309, methodElement, new Object[] {filePath, file.getAbsolutePath()});
    }
    return file;
  }

  protected File getCanonicalFile(File file) {
    try {
      return file.getCanonicalFile();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  protected File[] getSiblingFiles(M queryMeta, File file) {
    File dir = getDir(file);
    File[] files = dir.listFiles();
    if (files == null) {
      throw new AptException(Message.DOMA4144, methodElement, new Object[] {dir.getAbsolutePath()});
    }
    return files;
  }

  protected File getDir(File sqlFile) {
    File dir = sqlFile.getParentFile();
    if (dir == null) {
      assertUnreachable();
    }
    return dir;
  }

  protected FileObject getFileObject(String path) {
    try {
      return ctx.getResources().getResource(path);
    } catch (IOException e) {
      throw new AptException(Message.DOMA4143, methodElement, e, new Object[] {path, e});
    }
  }

  protected String getSql(File file, String filePath) {
    try {
      return IOUtil.readAsString(file);
    } catch (WrapException e) {
      Throwable cause = e.getCause();
      throw new AptException(
          Message.DOMA4068, methodElement, cause, new Object[] {filePath, cause});
    }
  }

  protected SqlNode createSqlNode(M queryMeta, String path, String sql) {
    try {
      SqlParser sqlParser = new SqlParser(sql);
      return sqlParser.parse();
    } catch (JdbcException e) {
      throw new AptException(Message.DOMA4069, methodElement, e, new Object[] {path, e});
    }
  }

  protected SqlValidator createSqlValidator(
      LinkedHashMap<String, TypeMirror> parameterTypeMap,
      String sqlFilePath,
      boolean expandable,
      boolean populatable) {
    return new SqlValidator(
        ctx, methodElement, parameterTypeMap, sqlFilePath, expandable, populatable);
  }
}
