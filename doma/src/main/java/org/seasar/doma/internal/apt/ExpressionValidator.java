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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.apt.decl.ConstructorDeclaration;
import org.seasar.doma.internal.apt.decl.FieldDeclaration;
import org.seasar.doma.internal.apt.decl.MethodDeclaration;
import org.seasar.doma.internal.apt.decl.TypeDeclaration;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.expr.node.AddOperatorNode;
import org.seasar.doma.internal.expr.node.AndOperatorNode;
import org.seasar.doma.internal.expr.node.ArithmeticOperatorNode;
import org.seasar.doma.internal.expr.node.CommaOperatorNode;
import org.seasar.doma.internal.expr.node.ComparisonOperatorNode;
import org.seasar.doma.internal.expr.node.DivideOperatorNode;
import org.seasar.doma.internal.expr.node.EmptyNode;
import org.seasar.doma.internal.expr.node.EqOperatorNode;
import org.seasar.doma.internal.expr.node.ExpressionLocation;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.internal.expr.node.ExpressionNodeVisitor;
import org.seasar.doma.internal.expr.node.FieldOperatorNode;
import org.seasar.doma.internal.expr.node.FunctionOperatorNode;
import org.seasar.doma.internal.expr.node.GeOperatorNode;
import org.seasar.doma.internal.expr.node.GtOperatorNode;
import org.seasar.doma.internal.expr.node.LeOperatorNode;
import org.seasar.doma.internal.expr.node.LiteralNode;
import org.seasar.doma.internal.expr.node.LogicalBinaryOperatorNode;
import org.seasar.doma.internal.expr.node.LtOperatorNode;
import org.seasar.doma.internal.expr.node.MethodOperatorNode;
import org.seasar.doma.internal.expr.node.ModOperatorNode;
import org.seasar.doma.internal.expr.node.MultiplyOperatorNode;
import org.seasar.doma.internal.expr.node.NeOperatorNode;
import org.seasar.doma.internal.expr.node.NewOperatorNode;
import org.seasar.doma.internal.expr.node.NotOperatorNode;
import org.seasar.doma.internal.expr.node.OrOperatorNode;
import org.seasar.doma.internal.expr.node.ParensNode;
import org.seasar.doma.internal.expr.node.StaticFieldOperatorNode;
import org.seasar.doma.internal.expr.node.StaticMethodOperatorNode;
import org.seasar.doma.internal.expr.node.SubtractOperatorNode;
import org.seasar.doma.internal.expr.node.VariableNode;
import org.seasar.doma.message.Message;

public class ExpressionValidator implements
        ExpressionNodeVisitor<TypeDeclaration, Void> {

    protected final ProcessingEnvironment env;

    protected final ExecutableElement methodElement;

    protected final Map<String, TypeMirror> parameterTypeMap;

    protected final Set<String> validatedParameterNames;

    protected final TypeDeclaration unknownTypeDeclaration;

    protected final String exprFunctionsClassName;

    public ExpressionValidator(ProcessingEnvironment env,
            ExecutableElement methodElement,
            Map<String, TypeMirror> parameterTypeMap) {
        this(env, methodElement, parameterTypeMap, Options
                .getExprFunctions(env));
    }

    public ExpressionValidator(ProcessingEnvironment env,
            ExecutableElement methodElement,
            Map<String, TypeMirror> parameterTypeMap,
            String exprFunctionsClassName) {
        assertNotNull(env, methodElement, parameterTypeMap);
        this.env = env;
        this.methodElement = methodElement;
        this.parameterTypeMap = new HashMap<String, TypeMirror>(
                parameterTypeMap);
        this.validatedParameterNames = new HashSet<String>();
        this.unknownTypeDeclaration = TypeDeclaration
                .newUnknownTypeDeclaration(env);
        this.exprFunctionsClassName = exprFunctionsClassName;
    }

    public TypeMirror removeParameterType(String parameterName) {
        return parameterTypeMap.remove(parameterName);
    }

    public void putParameterType(String parameterName, TypeMirror parameterType) {
        parameterTypeMap.put(parameterName, parameterType);
    }

    public Set<String> getValidatedParameterNames() {
        return validatedParameterNames;
    }

    public TypeDeclaration validate(ExpressionNode node) {
        return validateInternal(node);
    }

    protected TypeDeclaration validateInternal(ExpressionNode node) {
        return node.accept(this, null);
    }

    @Override
    public TypeDeclaration visitEqOperatorNode(EqOperatorNode node, Void p) {
        return handleNullAvailableComparisonOperation(node, p);
    }

    @Override
    public TypeDeclaration visitNeOperatorNode(NeOperatorNode node, Void p) {
        return handleNullAvailableComparisonOperation(node, p);
    }

    @Override
    public TypeDeclaration visitGeOperatorNode(GeOperatorNode node, Void p) {
        return handleNullUnavailableComparisonOperation(node, p);
    }

    @Override
    public TypeDeclaration visitGtOperatorNode(GtOperatorNode node, Void p) {
        return handleNullUnavailableComparisonOperation(node, p);
    }

    @Override
    public TypeDeclaration visitLeOperatorNode(LeOperatorNode node, Void p) {
        return handleNullUnavailableComparisonOperation(node, p);
    }

    @Override
    public TypeDeclaration visitLtOperatorNode(LtOperatorNode node, Void p) {
        return handleNullUnavailableComparisonOperation(node, p);
    }

    protected TypeDeclaration handleNullAvailableComparisonOperation(
            ComparisonOperatorNode node, Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        if (left.isNullType() || right.isNullType() || left.isSameType(right)) {
            return TypeDeclaration.newBooleanTypeDeclaration(env);
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(Message.DOMA4116, env, methodElement,
                location.getExpression(), location.getPosition(),
                node.getExpression(), node.getLeftNode().toString(),
                left.getBinaryName(), node.getRightNode().toString(),
                right.getBinaryName());
    }

    protected TypeDeclaration handleNullUnavailableComparisonOperation(
            ComparisonOperatorNode node, Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        if (left.isNullType() || right.isNullType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4139, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    node.getExpression());
        }
        if (left.isSameType(right)) {
            return TypeDeclaration.newBooleanTypeDeclaration(env);
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(Message.DOMA4116, env, methodElement,
                location.getExpression(), location.getPosition(),
                node.getExpression(), node.getLeftNode().toString(),
                left.getBinaryName(), node.getRightNode().toString(),
                right.getBinaryName());
    }

    @Override
    public TypeDeclaration visitAndOperatorNode(AndOperatorNode node, Void p) {
        return handleLogicalBinaryOperatorNode(node, p);
    }

    @Override
    public TypeDeclaration visitOrOperatorNode(OrOperatorNode node, Void p) {
        return handleLogicalBinaryOperatorNode(node, p);
    }

    protected TypeDeclaration handleLogicalBinaryOperatorNode(
            LogicalBinaryOperatorNode node, Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        if (!left.isBooleanType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4117, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    node.getExpression(), node.getLeftNode().toString(),
                    left.getBinaryName());
        }
        if (!right.isBooleanType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4118, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    node.getExpression(), node.getRightNode().toString(),
                    right.getBinaryName());
        }
        return TypeDeclaration.newBooleanTypeDeclaration(env);
    }

    @Override
    public TypeDeclaration visitNotOperatorNode(NotOperatorNode node, Void p) {
        TypeDeclaration result = node.getNode().accept(this, p);
        if (result.isBooleanType()) {
            return TypeDeclaration.newBooleanTypeDeclaration(env);
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(Message.DOMA4119, env, methodElement,
                location.getExpression(), location.getPosition(),
                node.getExpression(), node.getNode().toString(),
                result.getBinaryName());
    }

    @Override
    public TypeDeclaration visitAddOperatorNode(AddOperatorNode node, Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        if (left.isTextType()) {
            if (right.isTextType()) {
                return left.emulateConcatOperation(right);
            }
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4126, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    node.getExpression(), node.getLeftNode().toString(),
                    left.getBinaryName());
        }
        return handleArithmeticOperatorNode(node, left, right, p);
    }

    @Override
    public TypeDeclaration visitSubtractOperatorNode(SubtractOperatorNode node,
            Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        return handleArithmeticOperatorNode(node, left, right, p);
    }

    @Override
    public TypeDeclaration visitMultiplyOperatorNode(MultiplyOperatorNode node,
            Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        return handleArithmeticOperatorNode(node, left, right, p);
    }

    @Override
    public TypeDeclaration visitDivideOperatorNode(DivideOperatorNode node,
            Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        return handleArithmeticOperatorNode(node, left, right, p);
    }

    @Override
    public TypeDeclaration visitModOperatorNode(ModOperatorNode node, Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        return handleArithmeticOperatorNode(node, left, right, p);
    }

    protected TypeDeclaration handleArithmeticOperatorNode(
            ArithmeticOperatorNode node, TypeDeclaration left,
            TypeDeclaration right, Void p) {
        if (!left.isNumberType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4120, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    node.getExpression(), node.getLeftNode().toString(),
                    left.getBinaryName());
        }
        if (!right.isNumberType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4121, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    node.getExpression(), node.getRightNode().toString(),
                    right.getBinaryName());
        }
        return left.emulateArithmeticOperation(right);
    }

    @Override
    public TypeDeclaration visitLiteralNode(LiteralNode node, Void p) {
        TypeMirror type = node.getValueClass() == void.class ? env
                .getTypeUtils().getNullType() : TypeMirrorUtil.getTypeMirror(
                node.getValueClass(), env);
        return TypeDeclaration.newTypeDeclaration(type, env);
    }

    @Override
    public TypeDeclaration visitParensNode(ParensNode node, Void p) {
        return node.getNode().accept(this, p);
    }

    @Override
    public TypeDeclaration visitNewOperatorNode(NewOperatorNode node, Void p) {
        node.getParametersNode().accept(this, p);

        List<TypeDeclaration> parameterTypeDeclarations = new ParameterCollector()
                .collect(node.getParametersNode());

        String className = node.getClassName();
        TypeElement typeElement = ElementUtil.getTypeElement(className, env);
        if (typeElement == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4138, env, methodElement,
                    location.getExpression(), location.getPosition(), className);
        }
        TypeDeclaration typeDeclaration = TypeDeclaration.newTypeDeclaration(
                typeElement.asType(), env);
        List<ConstructorDeclaration> constructorDeclarations = typeDeclaration
                .getConstructorDeclarations(parameterTypeDeclarations);
        if (constructorDeclarations.size() == 0) {
            ExpressionLocation location = node.getLocation();
            String signature = createConstructorSignature(className,
                    parameterTypeDeclarations);
            throw new AptException(Message.DOMA4115, env, methodElement,
                    location.getExpression(), location.getPosition(), signature);
        }
        if (constructorDeclarations.size() == 1) {
            return typeDeclaration;
        }
        ExpressionLocation location = node.getLocation();
        String signature = createConstructorSignature(className,
                parameterTypeDeclarations);
        throw new AptException(Message.DOMA4127, env, methodElement,
                location.getExpression(), location.getPosition(), signature);
    }

    protected String createConstructorSignature(String className,
            List<TypeDeclaration> parameterTypeDeclarations) {
        StringBuilder buf = new StringBuilder();
        buf.append(className);
        buf.append("(");
        if (parameterTypeDeclarations.size() > 0) {
            for (TypeDeclaration declaration : parameterTypeDeclarations) {
                buf.append(declaration.getType());
                buf.append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        buf.append(")");
        return buf.toString();
    }

    @Override
    public TypeDeclaration visitCommaOperatorNode(CommaOperatorNode node, Void p) {
        return unknownTypeDeclaration;
    }

    @Override
    public TypeDeclaration visitEmptyNode(EmptyNode node, Void p) {
        return unknownTypeDeclaration;
    }

    @Override
    public TypeDeclaration visitMethodOperatorNode(MethodOperatorNode node,
            Void p) {
        TypeDeclaration typeDeclaration = node.getTargetObjectNode().accept(
                this, p);
        List<TypeDeclaration> parameterTypeDeclarations = new ParameterCollector()
                .collect(node.getParametersNode());
        String methodName = node.getMethodName();
        List<MethodDeclaration> methodDeclarations = typeDeclaration
                .getMethodDeclarations(methodName, parameterTypeDeclarations);
        if (methodDeclarations.size() == 0) {
            ExpressionLocation location = node.getLocation();
            String methodSignature = createMethodSignature(methodName,
                    parameterTypeDeclarations);
            throw new AptException(Message.DOMA4071, env, methodElement,
                    location.getExpression(), location.getPosition(), node
                            .getTargetObjectNode().getExpression(),
                    typeDeclaration.getBinaryName(), methodSignature);
        }
        if (methodDeclarations.size() == 1) {
            MethodDeclaration methodDeclaration = methodDeclarations.get(0);
            TypeDeclaration returnTypeDeclaration = methodDeclaration
                    .getReturnTypeDeclaration();
            if (returnTypeDeclaration != null) {
                return returnTypeDeclaration;
            }
        }
        ExpressionLocation location = node.getLocation();
        String methodSignature = createMethodSignature(methodName,
                parameterTypeDeclarations);
        throw new AptException(Message.DOMA4073, env, methodElement,
                location.getExpression(), location.getPosition(), node
                        .getTargetObjectNode().getExpression(),
                typeDeclaration.getBinaryName(), methodSignature);
    }

    @Override
    public TypeDeclaration visitStaticMethodOperatorNode(
            StaticMethodOperatorNode node, Void p) {
        String className = node.getClassName();
        TypeElement typeElement = ElementUtil.getTypeElement(
                node.getClassName(), env);
        if (typeElement == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4145, env, methodElement,
                    location.getExpression(), location.getPosition(), className);
        }
        TypeDeclaration typeDeclaration = TypeDeclaration.newTypeDeclaration(
                typeElement.asType(), env);
        List<TypeDeclaration> parameterTypeDeclarations = new ParameterCollector()
                .collect(node.getParametersNode());
        String methodName = node.getMethodName();
        List<MethodDeclaration> methodDeclarations = typeDeclaration
                .getStaticMethodDeclarations(methodName,
                        parameterTypeDeclarations);
        if (methodDeclarations.size() == 0) {
            ExpressionLocation location = node.getLocation();
            String methodSignature = createMethodSignature(methodName,
                    parameterTypeDeclarations);
            throw new AptException(Message.DOMA4146, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    className, methodSignature);
        }
        if (methodDeclarations.size() == 1) {
            MethodDeclaration methodDeclaration = methodDeclarations.get(0);
            TypeDeclaration returnTypeDeclaration = methodDeclaration
                    .getReturnTypeDeclaration();
            if (returnTypeDeclaration != null) {
                return returnTypeDeclaration;
            }
        }
        ExpressionLocation location = node.getLocation();
        String methodSignature = createMethodSignature(methodName,
                parameterTypeDeclarations);
        throw new AptException(Message.DOMA4147, env, methodElement,
                location.getExpression(), location.getPosition(), className,
                methodSignature);
    }

    @Override
    public TypeDeclaration visitFunctionOperatorNode(FunctionOperatorNode node,
            Void p) {
        TypeDeclaration typeDeclaration = getExpressionFunctionsDeclaration(node);
        List<TypeDeclaration> parameterTypeDeclarations = new ParameterCollector()
                .collect(node.getParametersNode());
        String methodName = node.getMethodName();
        List<MethodDeclaration> methodDeclarations = typeDeclaration
                .getMethodDeclarations(methodName, parameterTypeDeclarations);
        if (methodDeclarations.size() == 0) {
            ExpressionLocation location = node.getLocation();
            String methodSignature = createMethodSignature(methodName,
                    parameterTypeDeclarations);
            throw new AptException(Message.DOMA4072, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    methodSignature);
        }
        if (methodDeclarations.size() == 1) {
            MethodDeclaration methodDeclaration = methodDeclarations.get(0);
            TypeDeclaration returnTypeDeclaration = methodDeclaration
                    .getReturnTypeDeclaration();
            if (returnTypeDeclaration != null) {
                return returnTypeDeclaration;
            }
        }
        throw new AptIllegalStateException(methodName);
    }

    protected TypeDeclaration getExpressionFunctionsDeclaration(
            FunctionOperatorNode node) {
        if (exprFunctionsClassName == null) {
            return TypeDeclaration.newTypeDeclaration(
                    ExpressionFunctions.class, env);
        }
        TypeElement element = ElementUtil.getTypeElement(
                exprFunctionsClassName, env);
        if (element == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4189, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    node.getMethodName(), exprFunctionsClassName);
        }
        TypeMirror type = element.asType();
        if (!TypeMirrorUtil.isAssignable(type, ExpressionFunctions.class, env)) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4190, env, methodElement,
                    location.getExpression(), location.getPosition(),
                    node.getMethodName(), exprFunctionsClassName);
        }
        return TypeDeclaration.newTypeDeclaration(type, env);
    }

    protected String createMethodSignature(String methodName,
            List<TypeDeclaration> parameterTypeDeclarations) {
        StringBuilder buf = new StringBuilder();
        buf.append(methodName);
        buf.append("(");
        if (parameterTypeDeclarations.size() > 0) {
            for (TypeDeclaration declaration : parameterTypeDeclarations) {
                buf.append(declaration.getType());
                buf.append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        buf.append(")");
        return buf.toString();
    }

    @Override
    public TypeDeclaration visitFieldOperatorNode(FieldOperatorNode node, Void p) {
        TypeDeclaration typeDeclaration = node.getTargetObjectNode().accept(
                this, p);
        String fieldName = node.getFieldName();
        FieldDeclaration fieldDeclarations = typeDeclaration
                .getFieldDeclaration(fieldName);
        if (fieldDeclarations != null) {
            TypeDeclaration fieldTypeDeclaration = fieldDeclarations
                    .getTypeDeclaration();
            if (fieldTypeDeclaration != null) {
                return fieldTypeDeclaration;
            }
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(Message.DOMA4114, env, methodElement,
                location.getExpression(), location.getPosition(), node
                        .getTargetObjectNode().getExpression(),
                typeDeclaration.getBinaryName(), fieldName);
    }

    @Override
    public TypeDeclaration visitStaticFieldOperatorNode(
            StaticFieldOperatorNode node, Void p) {
        String className = node.getClassName();
        TypeElement typeElement = ElementUtil.getTypeElement(
                node.getClassName(), env);
        if (typeElement == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4145, env, methodElement,
                    location.getExpression(), location.getPosition(), className);
        }
        TypeDeclaration typeDeclaration = TypeDeclaration.newTypeDeclaration(
                typeElement.asType(), env);
        String fieldName = node.getFieldName();
        FieldDeclaration fieldDeclarations = typeDeclaration
                .getStaticFieldDeclaration(fieldName);
        if (fieldDeclarations != null) {
            TypeDeclaration fieldTypeDeclaration = fieldDeclarations
                    .getTypeDeclaration();
            if (fieldTypeDeclaration != null) {
                return fieldTypeDeclaration;
            }
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(Message.DOMA4148, env, methodElement,
                location.getExpression(), location.getPosition(), className,
                fieldName);
    }

    @Override
    public TypeDeclaration visitVariableNode(VariableNode node, Void p) {
        String variableName = node.getExpression();
        TypeMirror type = parameterTypeMap.get(variableName);
        if (type == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4067, env, methodElement,
                    variableName, location.getPosition());
        }
        validatedParameterNames.add(variableName);
        return TypeDeclaration.newTypeDeclaration(type, env);
    }

    protected class ParameterCollector implements
            ExpressionNodeVisitor<Void, List<TypeDeclaration>> {

        public List<TypeDeclaration> collect(ExpressionNode node) {
            List<TypeDeclaration> results = new ArrayList<TypeDeclaration>();
            node.accept(this, results);
            return results;
        }

        @Override
        public Void visitEqOperatorNode(EqOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitNeOperatorNode(NeOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitGeOperatorNode(GeOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitGtOperatorNode(GtOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitLeOperatorNode(LeOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitLtOperatorNode(LtOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitCommaOperatorNode(CommaOperatorNode node,
                List<TypeDeclaration> p) {
            for (ExpressionNode expressionNode : node.getNodes()) {
                expressionNode.accept(this, p);
            }
            return null;
        }

        @Override
        public Void visitLiteralNode(LiteralNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitVariableNode(VariableNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitOrOperatorNode(OrOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitAndOperatorNode(AndOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitNotOperatorNode(NotOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitAddOperatorNode(AddOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitSubtractOperatorNode(SubtractOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitMultiplyOperatorNode(MultiplyOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitDivideOperatorNode(DivideOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitModOperatorNode(ModOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitNewOperatorNode(NewOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitMethodOperatorNode(MethodOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitStaticMethodOperatorNode(
                StaticMethodOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitFunctionOperatorNode(FunctionOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitFieldOperatorNode(FieldOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitStaticFieldOperatorNode(StaticFieldOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitParensNode(ParensNode node, List<TypeDeclaration> p) {
            node.getNode().accept(this, p);
            return null;
        }

        @Override
        public Void visitEmptyNode(EmptyNode node, List<TypeDeclaration> p) {
            return null;
        }

        protected void validate(ExpressionNode node, List<TypeDeclaration> p) {
            TypeDeclaration result = ExpressionValidator.this
                    .validateInternal(node);
            p.add(result);
        }

    }

}
