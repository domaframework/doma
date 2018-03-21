package org.seasar.doma.internal.apt.validator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.decl.ConstructorDeclaration;
import org.seasar.doma.internal.apt.decl.FieldDeclaration;
import org.seasar.doma.internal.apt.decl.MethodDeclaration;
import org.seasar.doma.internal.apt.decl.TypeDeclaration;
import org.seasar.doma.internal.apt.decl.TypeParameterDeclaration;
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

public class ExpressionValidator implements ExpressionNodeVisitor<TypeDeclaration, Void> {

    private final Context ctx;

    private final ExecutableElement methodElement;

    private final Map<String, TypeMirror> parameterTypeMap;

    private final Set<String> validatedParameterNames;

    private final TypeDeclaration unknownTypeDeclaration;

    private final String exprFunctionsClassName;

    public ExpressionValidator(Context ctx, ExecutableElement methodElement,
            Map<String, TypeMirror> parameterTypeMap) {
        this(ctx, methodElement, parameterTypeMap, ctx.getOptions().getExprFunctions());
    }

    public ExpressionValidator(Context ctx, ExecutableElement methodElement,
            Map<String, TypeMirror> parameterTypeMap, String exprFunctionsClassName) {
        assertNotNull(ctx, methodElement, parameterTypeMap);
        this.ctx = ctx;
        this.methodElement = methodElement;
        this.parameterTypeMap = new HashMap<>(parameterTypeMap);
        this.validatedParameterNames = new HashSet<>();
        this.unknownTypeDeclaration = ctx.getDeclarations().newUnknownTypeDeclaration();
        this.exprFunctionsClassName = exprFunctionsClassName;
    }

    public TypeMirror removeParameterType(String parameterName) {
        return parameterTypeMap.remove(parameterName);
    }

    public void putParameterType(String parameterName, TypeMirror parameterType) {
        parameterTypeMap.put(parameterName, parameterType);
    }

    public void addValidatedParameterName(String name) {
        validatedParameterNames.add(name);
    }

    public Set<String> getValidatedParameterNames() {
        return validatedParameterNames;
    }

    public TypeDeclaration validate(ExpressionNode node) {
        return validateInternal(node);
    }

    private TypeDeclaration validateInternal(ExpressionNode node) {
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

    private TypeDeclaration handleNullAvailableComparisonOperation(ComparisonOperatorNode node,
            Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        if (left.isNullType() || right.isNullType() || left.isComparable(right)) {
            return ctx.getDeclarations().newTypeDeclaration(boolean.class);
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(Message.DOMA4116, methodElement,
                new Object[] { location.getExpression(), location.getPosition(),
                        node.getExpression(), node.getLeftNode().toString(), left.getBinaryName(),
                        node.getRightNode().toString(), right.getBinaryName() });
    }

    private TypeDeclaration handleNullUnavailableComparisonOperation(ComparisonOperatorNode node,
            Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        if (left.isNullType() || right.isNullType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4139, methodElement, new Object[] {
                    location.getExpression(), location.getPosition(), node.getExpression() });
        }
        if (left.isComparable(right)) {
            return ctx.getDeclarations().newTypeDeclaration(boolean.class);
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(Message.DOMA4116, methodElement,
                new Object[] { location.getExpression(), location.getPosition(),
                        node.getExpression(), node.getLeftNode().toString(), left.getBinaryName(),
                        node.getRightNode().toString(), right.getBinaryName() });
    }

    @Override
    public TypeDeclaration visitAndOperatorNode(AndOperatorNode node, Void p) {
        return handleLogicalBinaryOperatorNode(node, p);
    }

    @Override
    public TypeDeclaration visitOrOperatorNode(OrOperatorNode node, Void p) {
        return handleLogicalBinaryOperatorNode(node, p);
    }

    private TypeDeclaration handleLogicalBinaryOperatorNode(LogicalBinaryOperatorNode node,
            Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        if (!left.isBooleanType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4117, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(),
                            node.getExpression(), node.getLeftNode().toString(),
                            left.getBinaryName() });
        }
        if (!right.isBooleanType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4118, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(),
                            node.getExpression(), node.getRightNode().toString(),
                            right.getBinaryName() });
        }
        return ctx.getDeclarations().newTypeDeclaration(boolean.class);
    }

    @Override
    public TypeDeclaration visitNotOperatorNode(NotOperatorNode node, Void p) {
        TypeDeclaration result = node.getNode().accept(this, p);
        if (result.isBooleanType()) {
            return ctx.getDeclarations().newTypeDeclaration(boolean.class);
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(Message.DOMA4119, methodElement,
                new Object[] { location.getExpression(), location.getPosition(),
                        node.getExpression(), node.getNode().toString(), result.getBinaryName() });
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
            throw new AptException(Message.DOMA4126, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(),
                            node.getExpression(), node.getLeftNode().toString(),
                            left.getBinaryName() });
        }
        return handleArithmeticOperatorNode(node, left, right, p);
    }

    @Override
    public TypeDeclaration visitSubtractOperatorNode(SubtractOperatorNode node, Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        return handleArithmeticOperatorNode(node, left, right, p);
    }

    @Override
    public TypeDeclaration visitMultiplyOperatorNode(MultiplyOperatorNode node, Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        return handleArithmeticOperatorNode(node, left, right, p);
    }

    @Override
    public TypeDeclaration visitDivideOperatorNode(DivideOperatorNode node, Void p) {
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

    private TypeDeclaration handleArithmeticOperatorNode(ArithmeticOperatorNode node,
            TypeDeclaration left, TypeDeclaration right, Void p) {
        if (!left.isNumberType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4120, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(),
                            node.getExpression(), node.getLeftNode().toString(),
                            left.getBinaryName() });
        }
        if (!right.isNumberType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4121, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(),
                            node.getExpression(), node.getRightNode().toString(),
                            right.getBinaryName() });
        }
        return left.emulateArithmeticOperation(right);
    }

    @Override
    public TypeDeclaration visitLiteralNode(LiteralNode node, Void p) {
        TypeMirror type = node.getValueClass() == void.class ? ctx.getTypes().getNullType()
                : ctx.getTypes().getType(node.getValueClass());
        return ctx.getDeclarations().newTypeDeclaration(type);
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
        TypeElement typeElement = ctx.getElements().getTypeElement(className);
        if (typeElement == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4138, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(), className });
        }
        TypeDeclaration typeDeclaration = ctx.getDeclarations()
                .newTypeDeclaration(typeElement.asType());
        List<ConstructorDeclaration> constructorDeclarations = typeDeclaration
                .getConstructorDeclarations(parameterTypeDeclarations);
        if (constructorDeclarations.size() == 0) {
            ExpressionLocation location = node.getLocation();
            String signature = createConstructorSignature(className, parameterTypeDeclarations);
            throw new AptException(Message.DOMA4115, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(), signature });
        }
        if (constructorDeclarations.size() == 1) {
            return typeDeclaration;
        }
        ExpressionLocation location = node.getLocation();
        String signature = createConstructorSignature(className, parameterTypeDeclarations);
        throw new AptException(Message.DOMA4127, methodElement,
                new Object[] { location.getExpression(), location.getPosition(), signature });
    }

    private String createConstructorSignature(String className,
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
    public TypeDeclaration visitMethodOperatorNode(MethodOperatorNode node, Void p) {
        TypeDeclaration typeDeclaration = node.getTargetObjectNode().accept(this, p);
        List<TypeDeclaration> parameterTypeDeclarations = new ParameterCollector()
                .collect(node.getParametersNode());
        String methodName = node.getMethodName();
        List<MethodDeclaration> methodDeclarations = typeDeclaration
                .getMethodDeclarations(methodName, parameterTypeDeclarations);
        if (methodDeclarations.size() == 0) {
            ExpressionLocation location = node.getLocation();
            String methodSignature = createMethodSignature(methodName, parameterTypeDeclarations);
            throw new AptException(Message.DOMA4071, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(),
                            node.getTargetObjectNode().getExpression(),
                            typeDeclaration.getBinaryName(), methodSignature });
        }
        if (methodDeclarations.size() == 1) {
            MethodDeclaration methodDeclaration = methodDeclarations.get(0);
            TypeDeclaration returnTypeDeclaration = methodDeclaration.getReturnTypeDeclaration();
            if (returnTypeDeclaration != null) {
                return convertIfOptional(returnTypeDeclaration);
            }
        }
        ExpressionLocation location = node.getLocation();
        String methodSignature = createMethodSignature(methodName, parameterTypeDeclarations);
        throw new AptException(Message.DOMA4073, methodElement,
                new Object[] { location.getExpression(), location.getPosition(),
                        node.getTargetObjectNode().getExpression(), typeDeclaration.getBinaryName(),
                        methodSignature });
    }

    @Override
    public TypeDeclaration visitStaticMethodOperatorNode(StaticMethodOperatorNode node, Void p) {
        String className = node.getClassName();
        TypeElement typeElement = ctx.getElements().getTypeElement(node.getClassName());
        if (typeElement == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4145, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(), className });
        }
        TypeDeclaration typeDeclaration = ctx.getDeclarations()
                .newTypeDeclaration(typeElement.asType());
        List<TypeDeclaration> parameterTypeDeclarations = new ParameterCollector()
                .collect(node.getParametersNode());
        String methodName = node.getMethodName();
        List<MethodDeclaration> methodDeclarations = typeDeclaration
                .getStaticMethodDeclarations(methodName, parameterTypeDeclarations);
        if (methodDeclarations.size() == 0) {
            ExpressionLocation location = node.getLocation();
            String methodSignature = createMethodSignature(methodName, parameterTypeDeclarations);
            throw new AptException(Message.DOMA4146, methodElement, new Object[] {
                    location.getExpression(), location.getPosition(), className, methodSignature });
        }
        if (methodDeclarations.size() == 1) {
            MethodDeclaration methodDeclaration = methodDeclarations.get(0);
            TypeDeclaration returnTypeDeclaration = methodDeclaration.getReturnTypeDeclaration();
            if (returnTypeDeclaration != null) {
                return convertIfOptional(returnTypeDeclaration);
            }
        }
        ExpressionLocation location = node.getLocation();
        String methodSignature = createMethodSignature(methodName, parameterTypeDeclarations);
        throw new AptException(Message.DOMA4147, methodElement, new Object[] {
                location.getExpression(), location.getPosition(), className, methodSignature });
    }

    @Override
    public TypeDeclaration visitFunctionOperatorNode(FunctionOperatorNode node, Void p) {
        TypeDeclaration typeDeclaration = getExpressionFunctionsDeclaration(node);
        List<TypeDeclaration> parameterTypeDeclarations = new ParameterCollector()
                .collect(node.getParametersNode());
        String methodName = node.getMethodName();
        List<MethodDeclaration> methodDeclarations = typeDeclaration
                .getMethodDeclarations(methodName, parameterTypeDeclarations);
        if (methodDeclarations.size() == 0) {
            ExpressionLocation location = node.getLocation();
            String methodSignature = createMethodSignature(methodName, parameterTypeDeclarations);
            throw new AptException(Message.DOMA4072, methodElement, new Object[] {
                    location.getExpression(), location.getPosition(), methodSignature });
        }
        if (methodDeclarations.size() == 1) {
            MethodDeclaration methodDeclaration = methodDeclarations.get(0);
            TypeDeclaration returnTypeDeclaration = methodDeclaration.getReturnTypeDeclaration();
            if (returnTypeDeclaration != null) {
                return returnTypeDeclaration;
            }
        }
        throw new AptIllegalStateException(methodName);
    }

    private TypeDeclaration getExpressionFunctionsDeclaration(FunctionOperatorNode node) {
        if (exprFunctionsClassName == null) {
            return ctx.getDeclarations().newTypeDeclaration(ExpressionFunctions.class);
        }
        TypeElement element = ctx.getElements().getTypeElement(exprFunctionsClassName);
        if (element == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4189, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(),
                            node.getMethodName(), exprFunctionsClassName });
        }
        TypeMirror type = element.asType();
        if (!ctx.getTypes().isAssignable(type, ExpressionFunctions.class)) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4190, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(),
                            node.getMethodName(), exprFunctionsClassName });
        }
        return ctx.getDeclarations().newTypeDeclaration(type);
    }

    private String createMethodSignature(String methodName,
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
        TypeDeclaration typeDeclaration = node.getTargetObjectNode().accept(this, p);
        String fieldName = node.getFieldName();
        FieldDeclaration fieldDeclaration = typeDeclaration.getFieldDeclaration(fieldName);
        if (fieldDeclaration != null) {
            TypeDeclaration fieldTypeDeclaration = fieldDeclaration.getTypeDeclaration();
            if (fieldTypeDeclaration != null) {
                return convertIfOptional(fieldTypeDeclaration);
            }
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(Message.DOMA4114, methodElement,
                new Object[] { location.getExpression(), location.getPosition(),
                        node.getTargetObjectNode().getExpression(), typeDeclaration.getBinaryName(),
                        fieldName });
    }

    @Override
    public TypeDeclaration visitStaticFieldOperatorNode(StaticFieldOperatorNode node, Void p) {
        String className = node.getClassName();
        TypeElement typeElement = ctx.getElements().getTypeElement(node.getClassName());
        if (typeElement == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4145, methodElement,
                    new Object[] { location.getExpression(), location.getPosition(), className });
        }
        TypeDeclaration typeDeclaration = ctx.getDeclarations()
                .newTypeDeclaration(typeElement.asType());
        String fieldName = node.getFieldName();
        FieldDeclaration fieldDeclaration = typeDeclaration.getStaticFieldDeclaration(fieldName);
        if (fieldDeclaration != null) {
            TypeDeclaration fieldTypeDeclaration = fieldDeclaration.getTypeDeclaration();
            if (fieldTypeDeclaration != null) {
                return convertIfOptional(fieldTypeDeclaration);
            }
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(Message.DOMA4148, methodElement, new Object[] {
                location.getExpression(), location.getPosition(), className, fieldName });
    }

    private TypeDeclaration convertIfOptional(TypeDeclaration typeDeclaration) {
        if (typeDeclaration.is(Optional.class)) {
            TypeParameterDeclaration typeParameterDeclaration = typeDeclaration
                    .getTypeParameterDeclarations()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new AptIllegalStateException(typeDeclaration.toString()));
            return ctx.getDeclarations()
                    .newTypeDeclaration(typeParameterDeclaration.getActualType());
        } else if (typeDeclaration.is(OptionalInt.class)) {
            return ctx.getDeclarations().newTypeDeclaration(Integer.class);
        } else if (typeDeclaration.is(OptionalLong.class)) {
            return ctx.getDeclarations().newTypeDeclaration(Long.class);
        } else if (typeDeclaration.is(OptionalDouble.class)) {
            return ctx.getDeclarations().newTypeDeclaration(Double.class);
        }
        return typeDeclaration;
    }

    @Override
    public TypeDeclaration visitVariableNode(VariableNode node, Void p) {
        String variableName = node.getExpression();
        TypeMirror type = parameterTypeMap.get(variableName);
        if (type == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(Message.DOMA4067, methodElement,
                    new Object[] { variableName, location.getPosition() });
        }
        validatedParameterNames.add(variableName);
        return ctx.getDeclarations().newTypeDeclaration(type);
    }

    private class ParameterCollector implements ExpressionNodeVisitor<Void, List<TypeDeclaration>> {

        public List<TypeDeclaration> collect(ExpressionNode node) {
            List<TypeDeclaration> results = new ArrayList<>();
            node.accept(this, results);
            return results;
        }

        @Override
        public Void visitEqOperatorNode(EqOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitNeOperatorNode(NeOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitGeOperatorNode(GeOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitGtOperatorNode(GtOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitLeOperatorNode(LeOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitLtOperatorNode(LtOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitCommaOperatorNode(CommaOperatorNode node, List<TypeDeclaration> p) {
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
        public Void visitOrOperatorNode(OrOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitAndOperatorNode(AndOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitNotOperatorNode(NotOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitAddOperatorNode(AddOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitSubtractOperatorNode(SubtractOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitMultiplyOperatorNode(MultiplyOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitDivideOperatorNode(DivideOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitModOperatorNode(ModOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitNewOperatorNode(NewOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitMethodOperatorNode(MethodOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitStaticMethodOperatorNode(StaticMethodOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitFunctionOperatorNode(FunctionOperatorNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitFieldOperatorNode(FieldOperatorNode node, List<TypeDeclaration> p) {
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
            TypeDeclaration result = ExpressionValidator.this.validateInternal(node);
            p.add(result);
        }

    }

}
