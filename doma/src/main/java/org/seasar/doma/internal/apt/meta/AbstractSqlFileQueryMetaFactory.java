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

import java.io.InputStream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.FileObjectUtil;
import org.seasar.doma.internal.jdbc.sql.SqlFileUtil;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.DomaMessageCode;

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
        String path = SqlFileUtil.buildPath(daoMeta.getDaoElement()
                .getQualifiedName().toString(), queryMeta.getName());
        String sql = getSql(queryMeta, method, daoMeta, path);
        if (sql == null) {
            throw new AptException(DomaMessageCode.DOMA4019, env, method, path);
        }
        SqlNode sqlNode = createSqlNode(queryMeta, method, daoMeta, path, sql);
        SqlValidator validator = new SqlValidator(env, queryMeta, method, path);
        validator.validate(sqlNode);
    }

    protected String getSql(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta, String path) {
        InputStream inputStream = FileObjectUtil.getResourceAsStream(path, env);
        try {
            if (inputStream == null) {
                return null;
            }
            return IOUtil.readAsString(inputStream);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new AptException(DomaMessageCode.DOMA4068, env, method,
                    cause, path, cause);
        } finally {
            IOUtil.close(inputStream);
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
