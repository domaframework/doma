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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Options;
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

    protected AbstractSqlFileQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    protected void doSqlFiles(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        if (!Options.getSqlValidation(env)) {
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
                    throw new AptException(Message.DOMA4020, env, method,
                            sqlFilePath);
                }
                SqlNode sqlNode = createSqlNode(queryMeta, method, daoMeta,
                        sqlFilePath, sql);
                SqlValidator validator = createSqlValidator(method,
                        queryMeta.getBindableParameterTypeMap(), sqlFilePath);
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
        File file = new File(uri);
        if (!file.exists()) {
            throw new AptException(Message.DOMA4019, env, method, filePath,
                    file.getAbsolutePath());
        }
        if (file.isDirectory()) {
            throw new AptException(Message.DOMA4021, env, method, filePath,
                    file.getAbsolutePath());
        }
        return file;
    }

    protected File[] getSiblingFiles(M queryMeta, ExecutableElement method,
            File file) {
        File dir = getDir(file);
        File[] files = dir.listFiles();
        if (files == null) {
            throw new AptException(Message.DOMA4144, env, method,
                    dir.getAbsolutePath());
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
        Filer filer = env.getFiler();
        try {
            return filer.getResource(StandardLocation.CLASS_OUTPUT, "", path);
        } catch (IOException e) {
            throw new AptException(Message.DOMA4143, env, method, e, path, e);
        }
    }

    protected String getSql(ExecutableElement method, File file, String filePath) {
        try {
            return IOUtil.readAsString(file);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new AptException(Message.DOMA4068, env, method, cause,
                    filePath, cause);
        }
    }

    protected SqlNode createSqlNode(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta, String path, String sql) {
        try {
            SqlParser sqlParser = new SqlParser(sql);
            return sqlParser.parse();
        } catch (JdbcException e) {
            throw new AptException(Message.DOMA4069, env, method, e, path, e);
        }
    }

    protected SqlValidator createSqlValidator(ExecutableElement method,
            Map<String, TypeMirror> parameterTypeMap, String sqlFilePath) {
        return new SqlValidator(env, method, parameterTypeMap, sqlFilePath);
    }
}
