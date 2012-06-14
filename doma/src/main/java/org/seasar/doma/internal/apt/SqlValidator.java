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
package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.internal.apt.decl.TypeDeclaration;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.IterableType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.expr.ExpressionException;
import org.seasar.doma.internal.expr.ExpressionParser;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.ForBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.ForNode;
import org.seasar.doma.internal.jdbc.sql.node.ForNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.IfNode;
import org.seasar.doma.internal.jdbc.sql.node.IfNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class SqlValidator implements BindVariableNodeVisitor<Void, Void>,
        EmbeddedVariableNodeVisitor<Void, Void>, IfNodeVisitor<Void, Void>,
        ElseifNodeVisitor<Void, Void>, ForNodeVisitor<Void, Void> {

    protected static final int SQL_MAX_LENGTH = 5000;

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
                            Notifier.notify(env, Kind.ERROR, Message.DOMA4122,
                                    parameterElement, path, parameterName);
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
        SqlLocation location = node.getLocation();
        String variableName = node.getVariableName();
        TypeDeclaration typeDeclaration = validateExpressionVariable(location,
                variableName);
        if (node.getWordNode() != null) {
            if (!isBindable(typeDeclaration)) {
                String sql = getSql(location);
                throw new AptException(Message.DOMA4153, env, methodElement,
                        path, sql, location.getLineNumber(),
                        location.getPosition(), variableName,
                        typeDeclaration.getBinaryName());
            }
        } else {
            if (!isBindableIterable(typeDeclaration)) {
                String sql = getSql(location);
                throw new AptException(Message.DOMA4161, env, methodElement,
                        path, sql, location.getLineNumber(),
                        location.getPosition(), variableName,
                        typeDeclaration.getBinaryName());
            }
        }
        visitNode(node, p);
        return null;
    }

    protected boolean isBindable(TypeDeclaration typeDeclaration) {
        TypeMirror typeMirror = typeDeclaration.getType();
        return BasicType.newInstance(typeMirror, env) != null
                || DomainType.newInstance(typeMirror, env) != null;
    }

    protected boolean isBindableIterable(TypeDeclaration typeDeclaration) {
        TypeMirror typeMirror = typeDeclaration.getType();
        IterableType iterableType = IterableType.newInstance(typeMirror, env);
        if (iterableType != null) {
            return iterableType.getElementType().accept(
                    new SimpleDataTypeVisitor<Boolean, Void, RuntimeException>(
                            false) {

                        @Override
                        public Boolean visitBasicType(BasicType dataType, Void p)
                                throws RuntimeException {
                            return true;
                        }

                        @Override
                        public Boolean visitDomainType(DomainType dataType,
                                Void p) throws RuntimeException {
                            return true;
                        }

                    }, null);
        }
        return false;
    }

    @Override
    public Void visitEmbeddedVariableNode(EmbeddedVariableNode node, Void p) {
        SqlLocation location = node.getLocation();
        String variableName = node.getVariableName();
        validateExpressionVariable(location, variableName);
        visitNode(node, p);
        return null;
    }

    @Override
    public Void visitIfNode(IfNode node, Void p) {
        SqlLocation location = node.getLocation();
        String expression = node.getExpression();
        TypeDeclaration typeDeclaration = validateExpressionVariable(location,
                expression);
        if (!typeDeclaration.isBooleanType()) {
            String sql = getSql(location);
            throw new AptException(Message.DOMA4140, env, methodElement, path,
                    sql, location.getLineNumber(), location.getPosition(),
                    expression, typeDeclaration.getBinaryName());
        }
        visitNode(node, p);
        return null;
    }

    @Override
    public Void visitElseifNode(ElseifNode node, Void p) {
        SqlLocation location = node.getLocation();
        String expression = node.getExpression();
        TypeDeclaration typeDeclaration = validateExpressionVariable(location,
                expression);
        if (!typeDeclaration.isBooleanType()) {
            String sql = getSql(location);
            throw new AptException(Message.DOMA4141, env, methodElement, path,
                    sql, location.getLineNumber(), location.getPosition(),
                    expression, typeDeclaration.getBinaryName());
        }
        visitNode(node, p);
        return null;
    }

    @Override
    public Void visitForNode(ForNode node, Void p) {
        SqlLocation location = node.getLocation();
        String identifier = node.getIdentifier();
        String expression = node.getExpression();
        TypeDeclaration typeDeclaration = validateExpressionVariable(location,
                expression);
        TypeMirror typeMirror = typeDeclaration.getType();
        if (!TypeMirrorUtil.isAssignable(typeMirror, Iterable.class, env)) {
            String sql = getSql(location);
            throw new AptException(Message.DOMA4149, env, methodElement, path,
                    sql, location.getLineNumber(), location.getPosition(),
                    expression, typeDeclaration.getBinaryName());
        }
        DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(typeMirror,
                env);
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.isEmpty()) {
            String sql = getSql(location);
            throw new AptException(Message.DOMA4150, env, methodElement, path,
                    sql, location.getLineNumber(), location.getPosition(),
                    expression, typeDeclaration.getBinaryName());
        }

        TypeMirror originalIdentifierType = expressionValidator
                .removeParameterType(identifier);
        expressionValidator.putParameterType(identifier, typeArgs.get(0));
        String hasNextVariable = identifier + ForBlockNode.HAS_NEXT_SUFFIX;
        TypeMirror originalHasNextType = expressionValidator
                .removeParameterType(hasNextVariable);
        expressionValidator.putParameterType(hasNextVariable,
                TypeMirrorUtil.getTypeMirror(boolean.class, env));
        String indexVariable = identifier + ForBlockNode.INDEX_SUFFIX;
        TypeMirror originalIndexType = expressionValidator
                .removeParameterType(indexVariable);
        expressionValidator.putParameterType(indexVariable,
                TypeMirrorUtil.getTypeMirror(int.class, env));
        visitNode(node, p);
        if (originalIdentifierType == null) {
            expressionValidator.removeParameterType(identifier);
        } else {
            expressionValidator.putParameterType(identifier,
                    originalIdentifierType);
        }
        if (originalHasNextType == null) {
            expressionValidator.removeParameterType(hasNextVariable);
        } else {
            expressionValidator.putParameterType(hasNextVariable,
                    originalHasNextType);
        }
        if (originalIndexType == null) {
            expressionValidator.removeParameterType(indexVariable);
        } else {
            expressionValidator.putParameterType(indexVariable,
                    originalIndexType);
        }
        return null;
    }

    @Override
    public Void visitUnknownNode(SqlNode node, Void p) {
        visitNode(node, p);
        return null;
    }

    protected Void visitNode(SqlNode node, Void p) {
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        return null;
    }

    protected TypeDeclaration validateExpressionVariable(SqlLocation location,
            String expression) {
        ExpressionNode expressionNode = parseExpression(location, expression);
        try {
            return expressionValidator.validate(expressionNode);
        } catch (AptIllegalStateException e) {
            throw e;
        } catch (AptException e) {
            String sql = getSql(location);
            throw new AptException(Message.DOMA4092, env, methodElement, path,
                    sql, location.getLineNumber(), location.getPosition(),
                    e.getMessage());
        }
    }

    protected ExpressionNode parseExpression(SqlLocation location,
            String expression) {
        try {
            ExpressionParser parser = new ExpressionParser(expression);
            return parser.parse();
        } catch (ExpressionException e) {
            String sql = getSql(location);
            throw new AptException(Message.DOMA4092, env, methodElement, path,
                    sql, location.getLineNumber(), location.getPosition(),
                    e.getMessage());
        }
    }

    protected String getSql(SqlLocation location) {
        String sql = location.getSql();
        if (sql != null && sql.length() > SQL_MAX_LENGTH) {
            sql = sql.substring(0, SQL_MAX_LENGTH);
            sql += Message.DOMA4185.getSimpleMessage(SQL_MAX_LENGTH);
        }
        return sql;
    }
}
