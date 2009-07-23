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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNodeVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.DomaMessageCode;


/**
 * @author taedium
 * 
 */
public class BindVariableValidator implements
        BindVariableNodeVisitor<Void, Void> {

    protected final ProcessingEnvironment env;

    protected final SqlFileQueryMeta queryMeta;

    protected final ExecutableElement method;

    protected final String path;

    public BindVariableValidator(ProcessingEnvironment env,
            SqlFileQueryMeta queryMeta, ExecutableElement method, String path) {
        assertNotNull(env, queryMeta, method, path);
        this.env = env;
        this.queryMeta = queryMeta;
        this.method = method;
        this.path = path;
    }

    public void validate(SqlNode sqlNode) {
        sqlNode.accept(this, null);
    }

    @Override
    public Void visitBindVariableNode(BindVariableNode node, Void p) {
        String variableName = node.getVariableName();
        String[] names = variableName.split("\\.");
        TypeMirror parameterType = queryMeta.getMethodParameterType(names[0]);
        if (parameterType == null) {
            throw new AptException(DomaMessageCode.DOMA4067, env, method, path,
                    variableName, names[0]);
        }
        if (names.length == 1) {
            return null;
        }
        TypeElement typeElement = TypeUtil.toTypeElement(parameterType, env);
        if (typeElement == null) {
            throw new AptException(DomaMessageCode.DOMA4072, env, method, path,
                    variableName, names[0], parameterType);
        }
        int length = names.length;
        for (int i = 1; i < length; i++) {
            String methodName = names[i];
            ExecutableElement foundMethod = findMethod(typeElement, methodName);
            if (foundMethod == null) {
                throw new AptException(DomaMessageCode.DOMA4071, env, method, path,
                        variableName, i + 1, names[i], parameterType);
            }
            if (i + 1 < length) {
                TypeMirror returnType = foundMethod.getReturnType();
                typeElement = TypeUtil.toTypeElement(returnType, env);
                if (typeElement == null) {
                    throw new AptException(DomaMessageCode.DOMA4070, env, method,
                            path, variableName, i + 1, names[i], returnType);
                }
            }
        }
        return null;
    }

    public ExecutableElement findMethod(TypeElement typeElement,
            String methodName) {
        for (ExecutableElement e : ElementFilter.methodsIn(typeElement
                .getEnclosedElements())) {
            if (e.getSimpleName().contentEquals(methodName)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public Void visitUnknownNode(SqlNode node, Void p) {
        for (SqlNode child : node.getChildren()) {
            child.accept(this, null);
        }
        return null;
    }

}
