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
package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.internal.expr.ExpressionException;
import org.seasar.doma.internal.expr.ExpressionParser;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.IfNode;
import org.seasar.doma.internal.jdbc.sql.node.IfNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class SqlValidator implements BindVariableNodeVisitor<Void, Void>,
        EmbeddedVariableNodeVisitor<Void, Void>, IfNodeVisitor<Void, Void>,
        ElseifNodeVisitor<Void, Void> {

    protected final ProcessingEnvironment env;

    protected final ExecutableElement methodElement;

    protected final Map<String, TypeMirror> parameterTypeMap;

    protected final String path;

    protected final ExpressionValidator expressionValidator;

    public SqlValidator(ProcessingEnvironment env,
            ExecutableElement methodElement,
            Map<String, TypeMirror> parameterTypeMap, String path) {
        assertNotNull(env, methodElement, parameterTypeMap, path);
        this.env = env;
        this.methodElement = methodElement;
        this.parameterTypeMap = parameterTypeMap;
        this.path = path;
        expressionValidator = new ExpressionValidator(env, methodElement,
                parameterTypeMap);
    }

    public void validate(SqlNode sqlNode) {
        try {
            sqlNode.accept(this, null);
            Set<String> validatedParameterNames = expressionValidator
                    .getValidatedParameterNames();
            for (String parameterName : parameterTypeMap.keySet()) {
                if (!validatedParameterNames.contains(parameterName)) {
                    for (VariableElement parameterElement : methodElement
                            .getParameters()) {
                        if (parameterElement.getSimpleName().contentEquals(
                                parameterName)) {
                            Notifier.notify(env, Kind.ERROR,
                                    DomaMessageCode.DOMA4122, parameterElement,
                                    path, parameterName);
                        }
                    }
                }
            }
        } catch (AptIllegalStateException e) {
            throw e;
        } catch (AptException e) {
            Notifier.notify(env, e);
        }
    }

    @Override
    public Void visitBindVariableNode(BindVariableNode node, Void p) {
        validateExpressionVariable(node.getLocation(), node.getVariableName());
        visitNode(node, p);
        return null;
    }

    @Override
    public Void visitEmbeddedVariableNode(EmbeddedVariableNode node, Void p) {
        validateExpressionVariable(node.getLocation(), node.getVariableName());
        visitNode(node, p);
        return null;
    }

    @Override
    public Void visitIfNode(IfNode node, Void p) {
        validateExpressionVariable(node.getLocation(), node.getExpression());
        visitNode(node, p);
        return null;
    }

    @Override
    public Void visitElseifNode(ElseifNode node, Void p) {
        validateExpressionVariable(node.getLocation(), node.getExpression());
        visitNode(node, p);
        return null;
    }

    @Override
    public Void visitUnknownNode(SqlNode node, Void p) {
        visitNode(node, p);
        return null;
    }

    protected void visitNode(SqlNode node, Void p) {
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
    }

    protected void validateExpressionVariable(SqlLocation location,
            String expression) {
        ExpressionNode expressionNode = parseExpression(location, expression);
        try {
            expressionValidator.validate(expressionNode);
        } catch (AptIllegalStateException e) {
            throw e;
        } catch (AptException e) {
            throw new AptException(DomaMessageCode.DOMA4092, env,
                    methodElement, path, location.getSql(), location
                            .getLineNumber(), location.getPosition(), e
                            .getMessage());
        }
    }

    protected ExpressionNode parseExpression(SqlLocation location,
            String expression) {
        try {
            ExpressionParser parser = new ExpressionParser(expression);
            return parser.parse();
        } catch (ExpressionException e) {
            throw new AptException(DomaMessageCode.DOMA4092, env,
                    methodElement, path, location.getSql(), location
                            .getLineNumber(), location.getPosition(), e
                            .getMessage());
        }
    }
}
