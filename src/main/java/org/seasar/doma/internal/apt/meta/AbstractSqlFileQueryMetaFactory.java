/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.meta;

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
import org.seasar.doma.internal.apt.SqlValidator;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public abstract class AbstractSqlFileQueryMetaFactory<M extends AbstractSqlFileQueryMeta>
        extends AbstractQueryMetaFactory<M> {

    protected AbstractSqlFileQueryMetaFactory(Context ctx) {
        super(ctx);
    }

    protected void doSqlFiles(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta, boolean expandable, boolean populatable) {
        if (!ctx.getOptions().getSqlValidation()) {
            return;
        }
        String filePath = SqlFileUtil.buildPath(daoMeta.getDaoElement()
                .getQualifiedName().toString(), queryMeta.getName());
        File file = getFile(queryMeta, method, filePath);
        File[] siblingfiles = getSiblingFiles(queryMeta, method, file);
        String dirPath = SqlFileUtil.buildPath(daoMeta.getDaoElement()
                .getQualifiedName().toString());
        String methodName = queryMeta.getName();
        for (File siblingfile : siblingfiles) {
            if (SqlFileUtil.isSqlFile(siblingfile, methodName)) {
                String fileName = siblingfile.getName();
                String sqlFilePath = dirPath + "/" + fileName;
                String sql = getSql(method, siblingfile, sqlFilePath);
                if (sql.isEmpty() || StringUtil.isWhitespace(sql)) {
                    throw new AptException(Message.DOMA4020, method, new Object[] { sqlFilePath });
                }
                SqlNode sqlNode = createSqlNode(queryMeta, method, daoMeta,
                        sqlFilePath, sql);
                SqlValidator validator = createSqlValidator(method,
                        queryMeta.getBindableParameterTypeMap(), sqlFilePath,
                        expandable, populatable);
                validator.validate(sqlNode);
                queryMeta.addFileName(fileName);
            }
        }
    }

    protected File getFile(M queryMeta, ExecutableElement method,
            String filePath) {
        FileObject fileObject = getFileObject(filePath, method);
        URI uri = fileObject.toUri();
        if (!uri.isAbsolute()) {
            uri = new File(".").toURI().resolve(uri);
        }
        File file = getCanonicalFile(new File(uri));
        if (!file.exists()) {
            throw new AptException(Message.DOMA4019, method, new Object[] {
                    filePath, file.getAbsolutePath() });
        }
        if (file.isDirectory()) {
            throw new AptException(Message.DOMA4021, method, new Object[] {
                    filePath, file.getAbsolutePath() });
        }
        if (!IOUtil.endsWith(file, filePath)) {
            throw new AptException(Message.DOMA4309, method, new Object[] {
                    filePath, file.getAbsolutePath() });
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

    protected File[] getSiblingFiles(M queryMeta, ExecutableElement method,
            File file) {
        File dir = getDir(file);
        File[] files = dir.listFiles();
        if (files == null) {
            throw new AptException(Message.DOMA4144, method, new Object[] { dir.getAbsolutePath() });
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

    protected FileObject getFileObject(String path, ExecutableElement method) {
        try {
            return ctx.getResources().getResource(path);
        } catch (IOException e) {
            throw new AptException(Message.DOMA4143, method, e, new Object[] { path, e });
        }
    }

    protected String getSql(ExecutableElement method, File file, String filePath) {
        try {
            return IOUtil.readAsString(file);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new AptException(Message.DOMA4068, method, cause, new Object[] { filePath, cause });
        }
    }

    protected SqlNode createSqlNode(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta, String path, String sql) {
        try {
            SqlParser sqlParser = new SqlParser(sql);
            return sqlParser.parse();
        } catch (JdbcException e) {
            throw new AptException(Message.DOMA4069, method, e, new Object[] { path, e });
        }
    }

    protected SqlValidator createSqlValidator(ExecutableElement method,
            LinkedHashMap<String, TypeMirror> parameterTypeMap,
            String sqlFilePath, boolean expandable, boolean populatable) {
        return new SqlValidator(ctx, method, parameterTypeMap, sqlFilePath,
                expandable, populatable);
    }
}
