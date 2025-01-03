/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.LinkedHashMap;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
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

  AbstractSqlFileQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement);
  }

  void doSqlTemplate(M queryMeta, boolean expandable, boolean populatable) {
    if (!ctx.getOptions().getSqlValidation()) {
      return;
    }
    SqlAnnot sqlAnnot = queryMeta.getSqlAnnot();
    if (sqlAnnot == null) {
      processSqlFiles(queryMeta, expandable, populatable);
    } else {
      // experimental logic
      String sql = sqlAnnot.getValueValue();
      String path = queryMeta.getPath();
      if (sql.isEmpty() || StringUtil.isWhitespace(sql)) {
        throw new AptException(Message.DOMA4020, methodElement, new Object[] {path});
      }
      SqlNode sqlNode = createSqlNode(path, sql);
      SqlValidator validator =
          createSqlValidator(
              queryMeta.getBindableParameterTypeMap(), path, expandable, populatable);
      validator.validate(sqlNode);
    }
  }

  private void processSqlFiles(M queryMeta, boolean expandable, boolean populatable) {
    String filePath = queryMeta.getPath();
    File file = getFile(filePath);
    File[] siblingFiles = getSiblingFiles(file);
    String dirPath = SqlFileUtil.buildPath(daoElement.getQualifiedName().toString());
    String methodName = queryMeta.getName();
    for (File siblingFile : siblingFiles) {
      if (SqlFileUtil.isSqlFile(siblingFile, methodName)) {
        String fileName = siblingFile.getName();
        String sqlFilePath = dirPath + "/" + fileName;
        String sql = getSql(siblingFile, sqlFilePath);
        if (sql.isEmpty() || StringUtil.isWhitespace(sql)) {
          throw new AptException(Message.DOMA4020, methodElement, new Object[] {sqlFilePath});
        }
        SqlNode sqlNode = createSqlNode(sqlFilePath, sql);
        SqlValidator validator =
            createSqlValidator(
                queryMeta.getBindableParameterTypeMap(), sqlFilePath, expandable, populatable);
        validator.validate(sqlNode);
        queryMeta.addFileName(fileName);
      }
    }
  }

  File getFile(String filePath) {
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

  private File getCanonicalFile(File file) {
    try {
      return file.getCanonicalFile();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  File[] getSiblingFiles(File file) {
    File dir = getDir(file);
    File[] files = dir.listFiles();
    if (files == null) {
      throw new AptException(Message.DOMA4144, methodElement, new Object[] {dir.getAbsolutePath()});
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

  private FileObject getFileObject(String path) {
    try {
      return ctx.getResources().getResource(path);
    } catch (IOException e) {
      throw new AptException(Message.DOMA4143, methodElement, e, new Object[] {path, e});
    }
  }

  private String getSql(File file, String filePath) {
    try {
      return IOUtil.readAsString(file);
    } catch (WrapException e) {
      Throwable cause = e.getCause();
      throw new AptException(
          Message.DOMA4068, methodElement, cause, new Object[] {filePath, cause});
    }
  }

  private SqlNode createSqlNode(String path, String sql) {
    try {
      SqlParser sqlParser = new SqlParser(sql);
      return sqlParser.parse();
    } catch (JdbcException e) {
      throw new AptException(Message.DOMA4069, methodElement, e, new Object[] {path, e});
    }
  }

  SqlValidator createSqlValidator(
      LinkedHashMap<String, TypeMirror> parameterTypeMap,
      String sqlFilePath,
      boolean expandable,
      boolean populatable) {
    return new SqlValidator(
        ctx, methodElement, parameterTypeMap, sqlFilePath, expandable, populatable);
  }
}
