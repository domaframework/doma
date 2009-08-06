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
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.FileObjectUtil;
import org.seasar.doma.internal.apt.TypeUtil;
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

    @Override
    protected void doParameters(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        LinkedList<VariableElement> params = new LinkedList<VariableElement>(
                method.getParameters());
        for (VariableElement param : params) {
            TypeMirror paramType = TypeUtil.resolveTypeParameter(daoMeta
                    .getTypeParameterMap(), param.asType());
            if (isList(paramType)) {
                DeclaredType listTyep = TypeUtil.toDeclaredType(paramType, env);
                List<? extends TypeMirror> args = listTyep.getTypeArguments();
                if (args.isEmpty()) {
                    throw new AptException(DomaMessageCode.DOMA4027, env, method);
                }
                TypeMirror elementType = TypeUtil.resolveTypeParameter(daoMeta
                        .getTypeParameterMap(), args.get(0));
                if (!isDomain(elementType)) {
                    throw new AptException(DomaMessageCode.DOMA4028, env, method);
                }
            } else if (!isDomain(paramType) && !isEntity(paramType, daoMeta)) {
                throw new AptException(DomaMessageCode.DOMA4010, env, method);
            }
            String parameterName = ElementUtil.getParameterName(param);
            String parameterTypeName = TypeUtil.getTypeName(paramType, daoMeta
                    .getTypeParameterMap(), env);
            queryMeta.addMethodParameter(parameterName, parameterTypeName);
            queryMeta.addBindVariableType(parameterName, paramType);
        }
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
        BindVariableValidator validator = new BindVariableValidator(env,
                queryMeta, method, path);
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
            throw new AptException(DomaMessageCode.DOMA4068, env, method, cause,
                    path, cause);
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
            throw new AptException(DomaMessageCode.DOMA4069, env, method, e, path,
                    e);
        }
    }

}
