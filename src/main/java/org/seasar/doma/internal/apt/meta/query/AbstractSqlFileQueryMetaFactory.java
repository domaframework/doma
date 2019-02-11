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
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.validator.SqlValidator;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

abstract class AbstractSqlFileQueryMetaFactory<M extends AbstractSqlFileQueryMeta>
    extends AbstractQueryMetaFactory<M> {

  AbstractSqlFileQueryMetaFactory(Context ctx) {
    super(ctx);
  }

  void doSqlTemplate(
      M queryMeta,
      ExecutableElement method,
      DaoMeta daoMeta,
      boolean expandable,
      boolean populatable) {
    if (!ctx.getOptions().getSqlValidation()) {
      return;
    }
    SqlAnnot sqlAnnot = queryMeta.getSqlAnnot();
    if (sqlAnnot == null) {
      processSqlFiles(queryMeta, method, daoMeta, expandable, populatable);
    } else {
      // experimental logic
      String sql = sqlAnnot.getValueValue();
      String path = queryMeta.getPath();
      if (sql.isEmpty() || StringUtil.isWhitespace(sql)) {
        throw new AptException(Message.DOMA4020, method, new Object[] {path});
      }
      SqlNode sqlNode = createSqlNode(queryMeta, method, daoMeta, path, sql);
      SqlValidator validator =
          createSqlValidator(
              method, queryMeta.getBindableParameterTypeMap(), path, expandable, populatable);
      validator.validate(sqlNode);
    }
  }

  private void processSqlFiles(
      M queryMeta,
      ExecutableElement method,
      DaoMeta daoMeta,
      boolean expandable,
      boolean populatable) {
    String filePath = queryMeta.getPath();
    File file = getFile(queryMeta, method, filePath);
    File[] siblingfiles = getSiblingFiles(queryMeta, method, file);
    String dirPath = SqlFileUtil.buildPath(daoMeta.getTypeElement().getQualifiedName().toString());
    String methodName = queryMeta.getName();
    for (File siblingfile : siblingfiles) {
      if (SqlFileUtil.isSqlFile(siblingfile, methodName)) {
        String fileName = siblingfile.getName();
        String sqlFilePath = dirPath + "/" + fileName;
        String sql = getSql(method, siblingfile, sqlFilePath);
        if (sql.isEmpty() || StringUtil.isWhitespace(sql)) {
          throw new AptException(Message.DOMA4020, method, new Object[] {sqlFilePath});
        }
        SqlNode sqlNode = createSqlNode(queryMeta, method, daoMeta, sqlFilePath, sql);
        SqlValidator validator =
            createSqlValidator(
                method,
                queryMeta.getBindableParameterTypeMap(),
                sqlFilePath,
                expandable,
                populatable);
        validator.validate(sqlNode);
        queryMeta.addFileName(fileName);
      }
    }
  }

  File getFile(M queryMeta, ExecutableElement method, String filePath) {
    FileObject fileObject = getFileObject(filePath, method);
    URI uri = fileObject.toUri();
    if (!uri.isAbsolute()) {
      uri = new File(".").toURI().resolve(uri);
    }
    File file = getCanonicalFile(new File(uri));
    if (!file.exists()) {
      throw new AptException(
          Message.DOMA4019, method, new Object[] {filePath, file.getAbsolutePath()});
    }
    if (file.isDirectory()) {
      throw new AptException(
          Message.DOMA4021, method, new Object[] {filePath, file.getAbsolutePath()});
    }
    if (!IOUtil.endsWith(file, filePath)) {
      throw new AptException(
          Message.DOMA4309, method, new Object[] {filePath, file.getAbsolutePath()});
    }
    return file;
  }

  private File getCanonicalFile(File file) {
    try {
      return file.getCanonicalFile();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  File[] getSiblingFiles(M queryMeta, ExecutableElement method, File file) {
    File dir = getDir(file);
    File[] files = dir.listFiles();
    if (files == null) {
      throw new AptException(Message.DOMA4144, method, new Object[] {dir.getAbsolutePath()});
    }
    return files;
  }

  private File getDir(File sqlFile) {
    File dir = sqlFile.getParentFile();
    if (dir == null) {
      assertUnreachable();
    }
    return dir;
  }

  private FileObject getFileObject(String path, ExecutableElement method) {
    try {
      return ctx.getResources().getResource(path);
    } catch (IOException e) {
      throw new AptException(Message.DOMA4143, method, e, new Object[] {path, e});
    }
  }

  private String getSql(ExecutableElement method, File file, String filePath) {
    try {
      return IOUtil.readAsString(file);
    } catch (WrapException e) {
      Throwable cause = e.getCause();
      throw new AptException(Message.DOMA4068, method, cause, new Object[] {filePath, cause});
    }
  }

  private SqlNode createSqlNode(
      M queryMeta, ExecutableElement method, DaoMeta daoMeta, String path, String sql) {
    try {
      SqlParser sqlParser = new SqlParser(sql);
      return sqlParser.parse();
    } catch (JdbcException e) {
      throw new AptException(Message.DOMA4069, method, e, new Object[] {path, e});
    }
  }

  SqlValidator createSqlValidator(
      ExecutableElement method,
      LinkedHashMap<String, TypeMirror> parameterTypeMap,
      String sqlFilePath,
      boolean expandable,
      boolean populatable) {
    return new SqlValidator(ctx, method, parameterTypeMap, sqlFilePath, expandable, populatable);
  }
}
