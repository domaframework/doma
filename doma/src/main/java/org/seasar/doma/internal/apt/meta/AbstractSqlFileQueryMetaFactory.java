/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.FileObjectUtil;
import org.seasar.doma.internal.apt.SqlValidator;
import org.seasar.doma.internal.jdbc.sql.SqlFileUtil;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.message.DomaMessageCode;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public abstract class AbstractSqlFileQueryMetaFactory<M extends AbstractSqlFileQueryMeta>
        extends AbstractQueryMetaFactory<M> {

    public AbstractSqlFileQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    protected void doSqlFile(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        String methodName = queryMeta.getName();
        File sqlFile = getSqlFile(methodName, method, daoMeta);
        File sqlFileDir = sqlFile.getParentFile();
        if (sqlFileDir == null) {
            assertUnreachable();
            return;
        }
        String dirPath = SqlFileUtil.buildPath(daoMeta.getDaoElement()
                .getQualifiedName().toString());
        for (File file : sqlFileDir.listFiles()) {
            if (SqlFileUtil.isSqlFile(file, methodName)) {
                String filePath = dirPath + "/" + file.getName();
                String sql = getSql(method, file, filePath);
                if (sql.isEmpty() || StringUtil.isWhitespace(sql)) {
                    throw new AptException(DomaMessageCode.DOMA4020, env,
                            method, filePath);
                }
                SqlNode sqlNode = createSqlNode(queryMeta, method, daoMeta,
                        filePath, sql);
                SqlValidator validator = new SqlValidator(env, method,
                        queryMeta.getParameterTypeMap(), filePath);
                validator.validate(sqlNode);
            }
        }
    }

    protected File getSqlFile(String methodName, ExecutableElement method,
            DaoMeta daoMeta) {
        String path = SqlFileUtil.buildPath(daoMeta.getDaoElement()
                .getQualifiedName().toString(), methodName);
        File sqlFile = FileObjectUtil.getFile(path, env);
        if (sqlFile == null) {
            throw new AptException(DomaMessageCode.DOMA4019, env, method, path);
        }
        return sqlFile;
    }

    protected String getSql(ExecutableElement method, File file, String filePath) {
        try {
            return IOUtil.readAsString(file);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new AptException(DomaMessageCode.DOMA4068, env, method,
                    cause, filePath, cause);
        }
    }

    protected SqlNode createSqlNode(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta, String path, String sql) {
        try {
            SqlParser sqlParser = new SqlParser(sql);
            return sqlParser.parse();
        } catch (JdbcException e) {
            throw new AptException(DomaMessageCode.DOMA4069, env, method, e,
                    path, e);
        }
    }

}
