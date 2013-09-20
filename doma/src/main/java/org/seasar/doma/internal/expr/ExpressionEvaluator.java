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
package org.seasar.doma.internal.expr;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.WrapException;
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
import org.seasar.doma.internal.expr.node.LtOperatorNode;
import org.seasar.doma.internal.expr.node.MethodOperatorNode;
import org.seasar.doma.internal.expr.node.ModOperatorNode;
import org.seasar.doma.internal.expr.node.MultiplyOperatorNode;
import org.seasar.doma.internal.expr.node.NeOperatorNode;
import org.seasar.doma.internal.expr.node.NewOperatorNode;
import org.seasar.doma.internal.expr.node.NotOperatorNode;
import org.seasar.doma.internal.expr.node.OperatorNode;
import org.seasar.doma.internal.expr.node.OrOperatorNode;
import org.seasar.doma.internal.expr.node.ParensNode;
import org.seasar.doma.internal.expr.node.StaticFieldOperatorNode;
import org.seasar.doma.internal.expr.node.StaticMethodOperatorNode;
import org.seasar.doma.internal.expr.node.SubtractOperatorNode;
import org.seasar.doma.internal.expr.node.VariableNode;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.ConstructorUtil;
import org.seasar.doma.internal.util.FieldUtil;
import org.seasar.doma.internal.util.GenericsUtil;
import org.seasar.doma.internal.util.MethodUtil;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.DefaultClassHelper;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class ExpressionEvaluator implements
        ExpressionNodeVisitor<EvaluationResult, Void> {

    protected final Map<String, Value> variableValues;

    protected final ExpressionFunctions expressionFunctions;

    protected final ClassHelper classHelper;

    public ExpressionEvaluator() {
        this(new NullExpressionFunctions(), new DefaultClassHelper());
    }

    public ExpressionEvaluator(ExpressionFunctions expressionFunctions,
            ClassHelper classHelper) {
        this(Collections.<String, Value> emptyMap(), expressionFunctions,
                classHelper);
    }

    public ExpressionEvaluator(Map<String, Value> variableValues,
            ExpressionFunctions expressionFunctions, ClassHelper classHelper) {
        assertNotNull(variableValues, expressionFunctions, classHelper);
        this.variableValues = new HashMap<String, Value>(variableValues);
        this.expressionFunctions = expressionFunctions;
        this.classHelper = classHelper;
    }

    public void putValue(String variableName, Value value) {
        assertNotNull(variableName, value);
        variableValues.put(variableName, value);
    }

    public Value removeValue(String variableName) {
        assertNotNull(variableName);
        return variableValues.remove(variableName);
    }

    public void add(String varialbeName, Value value) {
        assertNotNull(varialbeName, value);
        variableValues.put(varialbeName, value);
    }

    public EvaluationResult evaluate(ExpressionNode node) {
        assertNotNull(node);
        return evaluateInternal(node);
    }

    protected EvaluationResult evaluateInternal(ExpressionNode node) {
        return node.accept(this, null);
    }

    @Override
    public EvaluationResult visitEqOperatorNode(EqOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        Object right = node.getRightNode().accept(this, p).getValue();
        if (left == null && right == null) {
            return new EvaluationResult(true, boolean.class);
        }
        if (left == null || right == null) {
            return new EvaluationResult(false, boolean.class);
        }
        try {
            @SuppressWarnings("unchecked")
            Comparable<Object> c1 = (Comparable<Object>) left;
            @SuppressWarnings("unchecked")
            Comparable<Object> c2 = (Comparable<Object>) right;
            return new EvaluationResult(c1.compareTo(c2) == 0, boolean.class);
        } catch (ClassCastException e) {
            ExpressionLocation location = node.getLocation();
            throw new ExpressionException(Message.DOMA3008, e,
                    location.getExpression(), node.getExpression(), e);
        }
    }

    @Override
    public EvaluationResult visitNeOperatorNode(NeOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        Object right = node.getRightNode().accept(this, p).getValue();
        if (left == null && right == null) {
            return new EvaluationResult(false, boolean.class);
        }
        if (left == null || right == null) {
            return new EvaluationResult(true, boolean.class);
        }
        try {
            @SuppressWarnings("unchecked")
            Comparable<Object> c1 = (Comparable<Object>) left;
            @SuppressWarnings("unchecked")
            Comparable<Object> c2 = (Comparable<Object>) right;
            return new EvaluationResult(c1.compareTo(c2) != 0, boolean.class);
        } catch (ClassCastException e) {
            ExpressionLocation location = node.getLocation();
            throw new ExpressionException(Message.DOMA3008, e,
                    location.getExpression(), node.getExpression(), e);
        }
    }

    @Override
    public EvaluationResult visitGeOperatorNode(GeOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        Object right = node.getRightNode().accept(this, p).getValue();
        return new EvaluationResult(compare(node, left, right) >= 0,
                boolean.class);
    }

    @Override
    public EvaluationResult visitGtOperatorNode(GtOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        Object right = node.getRightNode().accept(this, p).getValue();
        return new EvaluationResult(compare(node, left, right) > 0,
                boolean.class);
    }

    @Override
    public EvaluationResult visitLeOperatorNode(LeOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        Object right = node.getRightNode().accept(this, p).getValue();
        return new EvaluationResult(compare(node, left, right) <= 0,
                boolean.class);
    }

    @Override
    public EvaluationResult visitLtOperatorNode(LtOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        Object right = node.getRightNode().accept(this, p).getValue();
        return new EvaluationResult(compare(node, left, right) < 0,
                boolean.class);
    }

    protected int compare(ComparisonOperatorNode node, Object left, Object right)
            throws ClassCastException {
        if (left == null || right == null) {
            ExpressionLocation location = node.getLocation();
            throw new ExpressionException(Message.DOMA3009,
                    location.getExpression(), location.getPosition(),
                    node.getExpression());
        }
        try {
            @SuppressWarnings("unchecked")
            Comparable<Object> c1 = (Comparable<Object>) left;
            @SuppressWarnings("unchecked")
            Comparable<Object> c2 = (Comparable<Object>) right;
            return c1.compareTo(c2);
        } catch (ClassCastException e) {
            ExpressionLocation location = node.getLocation();
            throw new ExpressionException(Message.DOMA3008, e,
                    location.getExpression(), location.getPosition(),
                    node.getExpression(), e);
        }
    }

    @Override
    public EvaluationResult visitAndOperatorNode(AndOperatorNode node, Void p) {
        boolean result = node.getLeftNode().accept(this, p).getBooleanValue()
                && node.getRightNode().accept(this, p).getBooleanValue();
        return new EvaluationResult(result, boolean.class);
    }

    @Override
    public EvaluationResult visitOrOperatorNode(OrOperatorNode node, Void p) {
        boolean result = node.getLeftNode().accept(this, p).getBooleanValue()
                || node.getRightNode().accept(this, p).getBooleanValue();
        return new EvaluationResult(result, boolean.class);
    }

    @Override
    public EvaluationResult visitNotOperatorNode(NotOperatorNode node, Void p) {
        boolean result = node.getNode().accept(this, p).getBooleanValue();
        return new EvaluationResult(!result, boolean.class);
    }

    @Override
    public EvaluationResult visitAddOperatorNode(AddOperatorNode node, Void p) {
        ExpressionNode leftNode = node.getLeftNode();
        EvaluationResult leftResult = evaluateNotNullableOperandNode(node,
                leftNode, p);
        ExpressionNode rightNode = node.getRightNode();
        EvaluationResult rightResult = evaluateNotNullableOperandNode(node,
                rightNode, p);

        Text leftText = createText(node, leftNode, leftResult);
        if (leftText != null) {
            Text rightText = createText(node, rightNode, rightResult);
            if (rightText != null) {
                return leftText.concat(rightText);
            }
            throwNotTextException(node, rightNode, rightResult);
        }

        Number leftNumber = createNumber(node, leftNode, leftResult);
        if (leftNumber == null) {
            throwNotNumberException(node, leftNode, leftResult);
        }
        Number rightNumber = createNumber(node, rightNode, rightResult);
        if (rightNumber == null) {
            throwNotNumberException(node, rightNode, rightResult);
        }
        return leftNumber.add(rightNumber);
    }

    protected Text createText(OperatorNode operatoNode,
            ExpressionNode operandNode, EvaluationResult evaluationResult) {
        if (!Text.isAcceptable(evaluationResult.getValueClass())) {
            return null;
        }
        return new Text(operatoNode, evaluationResult.getValue(),
                evaluationResult.getValueClass());
    }

    protected void throwNotTextException(OperatorNode operatorNode,
            ExpressionNode operandNode, EvaluationResult evaluationResult) {
        ExpressionLocation location = operandNode.getLocation();
        throw new ExpressionException(Message.DOMA3020,
                location.getExpression(), location.getPosition(),
                operatorNode.getExpression(), evaluationResult.getValue(),
                evaluationResult.getValueClass().getName());
    }

    @Override
    public EvaluationResult visitSubtractOperatorNode(
            SubtractOperatorNode node, Void p) {
        ExpressionNode leftNode = node.getLeftNode();
        EvaluationResult leftResult = evaluateNotNullableOperandNode(node,
                leftNode, p);
        ExpressionNode rightNode = node.getRightNode();
        EvaluationResult rightResult = evaluateNotNullableOperandNode(node,
                rightNode, p);
        Number leftNumber = createNumber(node, leftNode, leftResult);
        if (leftNumber == null) {
            throwNotNumberException(node, leftNode, leftResult);
        }
        Number rightNumber = createNumber(node, rightNode, rightResult);
        if (rightNumber == null) {
            throwNotNumberException(node, rightNode, rightResult);
        }
        return leftNumber.subtract(rightNumber);
    }

    @Override
    public EvaluationResult visitMultiplyOperatorNode(
            MultiplyOperatorNode node, Void p) {
        ExpressionNode leftNode = node.getLeftNode();
        EvaluationResult leftResult = evaluateNotNullableOperandNode(node,
                leftNode, p);
        ExpressionNode rightNode = node.getRightNode();
        EvaluationResult rightResult = evaluateNotNullableOperandNode(node,
                rightNode, p);
        Number leftNumber = createNumber(node, leftNode, leftResult);
        if (leftNumber == null) {
            throwNotNumberException(node, leftNode, leftResult);
        }
        Number rightNumber = createNumber(node, rightNode, rightResult);
        if (rightNumber == null) {
            throwNotNumberException(node, rightNode, rightResult);
        }
        return leftNumber.multiply(rightNumber);
    }

    @Override
    public EvaluationResult visitDivideOperatorNode(DivideOperatorNode node,
            Void p) {
        ExpressionNode leftNode = node.getLeftNode();
        EvaluationResult leftResult = evaluateNotNullableOperandNode(node,
                leftNode, p);
        ExpressionNode rightNode = node.getRightNode();
        EvaluationResult rightResult = evaluateNotNullableOperandNode(node,
                rightNode, p);
        Number leftNumber = createNumber(node, leftNode, leftResult);
        if (leftNumber == null) {
            throwNotNumberException(node, leftNode, leftResult);
        }
        Number rightNumber = createNumber(node, rightNode, rightResult);
        if (rightNumber == null) {
            throwNotNumberException(node, rightNode, rightResult);
        }
        return leftNumber.divide(rightNumber);
    }

    @Override
    public EvaluationResult visitModOperatorNode(ModOperatorNode node, Void p) {
        ExpressionNode leftNode = node.getLeftNode();
        EvaluationResult leftResult = evaluateNotNullableOperandNode(node,
                leftNode, p);
        ExpressionNode rightNode = node.getRightNode();
        EvaluationResult rightResult = evaluateNotNullableOperandNode(node,
                rightNode, p);
        Number leftNumber = createNumber(node, leftNode, leftResult);
        if (leftNumber == null) {
            throwNotNumberException(node, leftNode, leftResult);
        }
        Number rightNumber = createNumber(node, rightNode, rightResult);
        if (rightNumber == null) {
            throwNotNumberException(node, rightNode, rightResult);
        }
        return leftNumber.mod(rightNumber);
    }

    protected Number createNumber(ArithmeticOperatorNode operatoNode,
            ExpressionNode operandNode, EvaluationResult evaluationResult) {
        if (!Number.isAcceptable(evaluationResult.getValueClass())) {
            return null;
        }
        return new Number(operatoNode, evaluationResult.getValue(),
                evaluationResult.getValueClass());
    }

    protected void throwNotNumberException(ArithmeticOperatorNode operatorNode,
            ExpressionNode operandNode, EvaluationResult evaluationResult) {
        ExpressionLocation location = operandNode.getLocation();
        throw new ExpressionException(Message.DOMA3013,
                location.getExpression(), location.getPosition(),
                operatorNode.getExpression(), evaluationResult.getValue(),
                evaluationResult.getValueClass().getName());
    }

    protected EvaluationResult evaluateNotNullableOperandNode(
            ExpressionNode operatorNode, ExpressionNode operandNode, Void p) {
        EvaluationResult evaluationResult = operandNode.accept(this, p);
        if (evaluationResult.getValue() == null) {
            ExpressionLocation location = operandNode.getLocation();
            throw new ExpressionException(Message.DOMA3015,
                    location.getExpression(), location.getPosition(),
                    operatorNode.getExpression());
        }
        return evaluationResult;
    }

    @Override
    public EvaluationResult visitLiteralNode(LiteralNode node, Void p) {
        return new EvaluationResult(node.getValue(), node.getValueClass());
    }

    @Override
    public EvaluationResult visitParensNode(ParensNode node, Void p) {
        return node.getNode().accept(this, p);
    }

    @Override
    public EvaluationResult visitNewOperatorNode(NewOperatorNode node, Void p) {
        ParameterCollector collector = new ParameterCollector();
        ParameterCollection collection = collector.collect(node
                .getParametersNode());
        ExpressionLocation location = node.getLocation();
        String className = node.getClassName();
        Class<?> clazz = forClassName(location, className);
        Constructor<?> constructor = findConstructor(location, clazz,
                collection.getParamTypes());
        if (constructor == null) {
            String signature = ConstructorUtil.createSignature(clazz,
                    collection.getParamTypes());
            throw new ExpressionException(Message.DOMA3006,
                    location.getExpression(), location.getPosition(), signature);
        }
        return invokeConstructor(location, clazz, constructor,
                collection.getParams());
    }

    protected Class<?> forClassName(ExpressionLocation location,
            String className) {
        try {
            return classHelper.forName(className);
        } catch (WrapException e) {
            throw new ExpressionException(Message.DOMA3005, e.getCause(),
                    location.getExpression(), location.getPosition(), className);
        } catch (Exception e) {
            throw new ExpressionException(Message.DOMA3005, e,
                    location.getExpression(), location.getPosition(), className);
        }
    }

    protected Constructor<?> findConstructor(ExpressionLocation location,
            Class<?> clazz, Class<?>... paramTypes) {
        outer: for (Constructor<?> constructor : clazz.getConstructors()) {
            Class<?>[] types = constructor.getParameterTypes();
            if (types.length == paramTypes.length) {
                for (int i = 0; i < types.length; i++) {
                    if (!types[i].isAssignableFrom(paramTypes[i])) {
                        continue outer;
                    }
                }
                return constructor;
            }
        }
        return null;
    }

    protected EvaluationResult invokeConstructor(ExpressionLocation location,
            Class<?> clazz, Constructor<?> constructor, Object... params) {
        Object value = null;
        try {
            value = ConstructorUtil.newInstance(constructor, params);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new ExpressionException(Message.DOMA3007, cause,
                    location.getExpression(), location.getPosition(),
                    ConstructorUtil.createSignature(constructor), cause);
        }
        return new EvaluationResult(value, clazz);
    }

    @Override
    public EvaluationResult visitMethodOperatorNode(MethodOperatorNode node,
            Void p) {
        ExpressionNode targetObjectNode = node.getTargetObjectNode();
        EvaluationResult targetResult = targetObjectNode.accept(this, p);
        Object target = targetResult.getValue();
        if (target == null) {
            ExpressionLocation location = node.getLocation();
            throw new ExpressionException(Message.DOMA3027,
                    location.getExpression(), location.getPosition(),
                    targetObjectNode.getExpression(), node.getMethodName());
        }
        Class<?> targetClass = target.getClass();
        ParameterCollector collector = new ParameterCollector();
        ParameterCollection collection = collector.collect(node
                .getParametersNode());
        ExpressionLocation location = node.getLocation();
        Method method = findMethod(node.getMethodName(), target, targetClass,
                collection.getParamTypes());
        if (method == null) {
            String signature = MethodUtil.createSignature(node.getMethodName(),
                    collection.getParamTypes());
            throw new ExpressionException(Message.DOMA3002,
                    location.getExpression(), location.getPosition(),
                    targetClass.getName(), signature);
        }
        return invokeMethod(location, method, target, targetClass,
                collection.getParamTypes(), collection.getParams());
    }

    @Override
    public EvaluationResult visitStaticMethodOperatorNode(
            StaticMethodOperatorNode node, Void p) {
        Class<?> targetClass = forClassName(node.getLocation(),
                node.getClassName());
        ParameterCollector collector = new ParameterCollector();
        ParameterCollection collection = collector.collect(node
                .getParametersNode());
        ExpressionLocation location = node.getLocation();
        Method method = findMethod(node.getMethodName(), null, targetClass,
                collection.getParamTypes());
        if (method == null) {
            String signature = MethodUtil.createSignature(node.getMethodName(),
                    collection.getParamTypes());
            throw new ExpressionException(Message.DOMA3002,
                    location.getExpression(), location.getPosition(),
                    targetClass.getName(), signature);
        }
        return invokeMethod(location, method, null, targetClass,
                collection.getParamTypes(), collection.getParams());
    }

    protected Method findMethod(String methodName, Object target,
            Class<?> targetClass, Class<?>[] paramTypes) {
        Method result = findMethodFromInterfaces(methodName, target,
                targetClass, paramTypes);
        if (result != null) {
            return result;
        }
        return findMethodFromClasses(methodName, target, targetClass,
                paramTypes);
    }

    protected Method findMethodFromInterfaces(String methodName, Object target,
            Class<?> targetClass, Class<?>[] paramTypes) {
        LinkedList<Method> methods = new LinkedList<Method>();
        for (Class<?> clazz = targetClass; clazz != null
                && clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Class<?> interfaze : clazz.getInterfaces()) {
                for (Method method : interfaze.getMethods()) {
                    if (method.getName().equals(methodName)) {
                        methods.addFirst(method);
                    }
                }
            }
        }
        return findSuiteMethod(methods, target, targetClass, paramTypes);
    }

    protected Method findMethodFromClasses(String methodName, Object target,
            Class<?> targetClass, Class<?>[] paramTypes) {
        LinkedList<Method> methods = new LinkedList<Method>();
        for (Class<?> clazz = targetClass; clazz != null
                && clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(methodName)) {
                    methods.addFirst(method);
                }
            }
        }
        return findSuiteMethod(methods, target, targetClass, paramTypes);
    }

    protected Method findSuiteMethod(List<Method> methods, Object target,
            Class<?> targetClass, Class<?>[] argTypes) {
        CandidateMethod candidate = null;
        outer: for (Method method : methods) {
            Class<?> paramTypes[] = method.getParameterTypes();
            if (paramTypes.length == argTypes.length) {
                int degreeOfcoincidence = 0;
                for (int i = 0; i < paramTypes.length; i++) {
                    int difference = calculateHierarchyDifference(
                            paramTypes[i], argTypes[i], 0);
                    if (difference == -1) {
                        continue outer;
                    }
                    degreeOfcoincidence += difference;
                }
                if (degreeOfcoincidence == 0) {
                    return method;
                }
                if (candidate == null
                        || degreeOfcoincidence < candidate.degreeOfcoincidence) {
                    candidate = new CandidateMethod(degreeOfcoincidence, method);
                }
            }
        }
        return candidate != null ? candidate.method : null;
    }

    protected int calculateHierarchyDifference(Class<?> paramType,
            Class<?> argType, int initDifference) {
        int difference = initDifference;
        if (paramType.equals(Object.class) && argType.isInterface()) {
            return Integer.MAX_VALUE;
        }
        for (Class<?> type = argType; type != null; type = type.getSuperclass()) {
            if (paramType.equals(type)
                    || paramType.equals(ClassUtil
                            .toBoxedPrimitiveTypeIfPossible(type))) {
                return difference;
            }
            difference++;
            if (paramType.isInterface()) {
                for (Class<?> interfaceClass : type.getInterfaces()) {
                    int result = calculateHierarchyDifference(paramType,
                            interfaceClass, difference);
                    if (result != -1) {
                        return result;
                    }
                }
            }
        }
        return -1;
    }

    protected Method findStaticMethod(String methodName, Class<?> targetClass,
            Class<?>[] paramTypes) {
        Method method = findMethod(methodName, null, targetClass, paramTypes);
        if (method == null) {
            return null;
        }
        if ((method.getModifiers() & Modifier.STATIC) != 0) {
            return method;
        }
        return null;
    }

    protected EvaluationResult invokeMethod(ExpressionLocation location,
            Method method, Object target, Class<?> targetClass,
            Class<?>[] paramTypes, Object[] params) {
        Object value;
        try {
            value = MethodUtil.invoke(method, target, params);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new ExpressionException(Message.DOMA3001, cause,
                    location.getExpression(), location.getPosition(),
                    targetClass.getName(), method.getName(), cause);
        }
        if (target != null) {
            Type genericType = method.getGenericReturnType();
            if (genericType instanceof TypeVariable) {
                Class<?> typeArgument = GenericsUtil.inferTypeArgument(
                        target.getClass(), (TypeVariable<?>) genericType);
                if (typeArgument != null) {
                    return new EvaluationResult(value, typeArgument);
                }
            }
        }
        return new EvaluationResult(value, method.getReturnType());
    }

    @Override
    public EvaluationResult visitFunctionOperatorNode(
            FunctionOperatorNode node, Void p) {
        Class<?> targetClass = expressionFunctions.getClass();
        ParameterCollector collector = new ParameterCollector();
        ParameterCollection collection = collector.collect(node
                .getParametersNode());
        ExpressionLocation location = node.getLocation();
        Method method = findMethod(node.getMethodName(), expressionFunctions,
                targetClass, collection.getParamTypes());
        if (method == null) {
            String signature = MethodUtil.createSignature(node.getMethodName(),
                    collection.getParamTypes());
            throw new ExpressionException(Message.DOMA3028,
                    location.getExpression(), location.getPosition(), signature);
        }
        return invokeMethod(node.getLocation(), method, expressionFunctions,
                targetClass, collection.getParamTypes(), collection.getParams());
    }

    @Override
    public EvaluationResult visitFieldOperatorNode(FieldOperatorNode node,
            Void p) {
        EvaluationResult targetResult = node.getTargetObjectNode().accept(this,
                p);
        Object target = targetResult.getValue();
        ExpressionLocation location = node.getLocation();
        Field field = findField(node.getFieldName(), target.getClass());
        if (field == null) {
            throw new ExpressionException(Message.DOMA3018,
                    location.getExpression(), location.getPosition(), target
                            .getClass().getName(), node.getFieldName());
        }
        return getFieldValue(location, field, target);
    }

    @Override
    public EvaluationResult visitStaticFieldOperatorNode(
            StaticFieldOperatorNode node, Void p) {
        Class<?> targetClass = forClassName(node.getLocation(),
                node.getClassName());
        ExpressionLocation location = node.getLocation();
        Field field = findStaticField(node.getFieldName(), targetClass);
        if (field == null) {
            throw new ExpressionException(Message.DOMA3033,
                    location.getExpression(), location.getPosition(),
                    targetClass.getName(), node.getFieldName());
        }
        return getFieldValue(location, field, null);
    }

    protected Field findField(String fieldName, Class<?> targetClass) {
        for (Class<?> clazz = targetClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {
            }
        }
        return null;
    }

    protected Field findStaticField(String fieldName, Class<?> targetClass) {
        Field field = findField(fieldName, targetClass);
        if (field != null && (field.getModifiers() & Modifier.STATIC) != 0) {
            return field;
        }
        return null;
    }

    protected EvaluationResult getFieldValue(ExpressionLocation location,
            Field field, Object target) {
        Object value;
        try {
            value = FieldUtil.get(field, target);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new ExpressionException(Message.DOMA3019, cause,
                    location.getExpression(), location.getPosition(), target
                            .getClass().getName(), field.getName(), cause);
        }
        if (target != null) {
            Type genericType = field.getGenericType();
            if (genericType instanceof TypeVariable) {
                Class<?> typeArgument = GenericsUtil.inferTypeArgument(
                        target.getClass(), (TypeVariable<?>) genericType);
                if (typeArgument != null) {
                    return new EvaluationResult(value, typeArgument);
                }
            }
        }
        return new EvaluationResult(value, field.getType());
    }

    @Override
    public EvaluationResult visitVariableNode(VariableNode node, Void p) {
        String variableName = node.getExpression();
        Value value = variableValues.get(node.getExpression());
        if (value == null) {
            ExpressionLocation location = node.getLocation();
            throw new ExpressionException(Message.DOMA3003,
                    location.getExpression(), location.getPosition(),
                    variableName);
        }
        return new EvaluationResult(value.getValue(), value.getType());
    }

    @Override
    public EvaluationResult visitCommaOperatorNode(CommaOperatorNode node,
            Void p) {
        return new EvaluationResult(null, Void.class);
    }

    @Override
    public EvaluationResult visitEmptyNode(EmptyNode node, Void p) {
        return new EvaluationResult(null, Void.class);
    }

    protected static class Text {

        protected final OperatorNode operatorNode;

        protected final String stringValue;

        protected Text(OperatorNode operatorNode, Object value,
                Class<?> valueClass) {
            assertNotNull(operatorNode);
            assertNotNull(value);
            assertNotNull(isAcceptable(valueClass));
            this.operatorNode = operatorNode;
            this.stringValue = value.toString();
        }

        protected static boolean isAcceptable(Class<?> valueClass) {
            return valueClass == String.class || valueClass == Character.class
                    || valueClass == char.class;
        }

        protected EvaluationResult concat(Text other) {
            String newValue = stringValue.concat(other.stringValue);
            return new EvaluationResult(newValue, String.class);
        }
    }

    protected static class Number {

        protected static final Map<Class<?>, Integer> priorityMap = new HashMap<Class<?>, Integer>();
        static {
            priorityMap.put(BigDecimal.class, 80);
            priorityMap.put(BigInteger.class, 70);
            priorityMap.put(double.class, 60);
            priorityMap.put(Double.class, 60);
            priorityMap.put(float.class, 50);
            priorityMap.put(Float.class, 50);
            priorityMap.put(long.class, 40);
            priorityMap.put(Long.class, 40);
            priorityMap.put(int.class, 30);
            priorityMap.put(Integer.class, 30);
            priorityMap.put(short.class, 20);
            priorityMap.put(Short.class, 20);
            priorityMap.put(byte.class, 10);
            priorityMap.put(Byte.class, 10);
        }

        protected final ArithmeticOperatorNode operatorNode;

        protected final BigDecimal numberValue;

        protected final Class<?> realClass;

        protected final Integer priority;

        protected Number(ArithmeticOperatorNode operatorNode, Object value,
                Class<?> valueClass) {
            assertNotNull(operatorNode);
            assertNotNull(value);
            assertTrue(isAcceptable(valueClass));
            this.priority = priorityMap.get(valueClass);
            this.operatorNode = operatorNode;
            this.numberValue = widenValue(value, valueClass);
            this.realClass = valueClass;
        }

        protected static boolean isAcceptable(Class<?> valueClass) {
            return priorityMap.containsKey(valueClass);
        }

        protected BigDecimal widenValue(Object value, Class<?> clazz) {
            if (clazz == BigDecimal.class) {
                return (BigDecimal) value;
            } else if (clazz == BigInteger.class) {
                BigInteger v = (BigInteger) value;
                return new BigDecimal(v);
            } else if (clazz == Double.class || clazz == double.class) {
                Double v = (Double) value;
                return new BigDecimal(v);
            } else if (clazz == Float.class || clazz == float.class) {
                Float v = (Float) value;
                return new BigDecimal(v);
            } else if (clazz == Long.class || clazz == long.class) {
                Long v = (Long) value;
                return new BigDecimal(v);
            } else if (clazz == Integer.class || clazz == int.class) {
                Integer v = (Integer) value;
                return new BigDecimal(v);
            } else if (clazz == Short.class || clazz == short.class) {
                Short v = (Short) value;
                return new BigDecimal(v);
            } else if (clazz == Byte.class || clazz == byte.class) {
                Byte v = (Byte) value;
                return new BigDecimal(v);
            }
            return assertUnreachable();
        }

        protected Object narrowValue(BigDecimal value, Class<?> clazz) {
            if (clazz == BigDecimal.class) {
                return value;
            } else if (clazz == BigInteger.class) {
                return value.toBigInteger();
            } else if (clazz == Double.class || clazz == double.class) {
                return value.doubleValue();
            } else if (clazz == Float.class || clazz == float.class) {
                return value.floatValue();
            } else if (clazz == Long.class || clazz == long.class) {
                return value.floatValue();
            } else if (clazz == Integer.class || clazz == int.class) {
                return value.intValue();
            } else if (clazz == Short.class || clazz == short.class) {
                return value.shortValue();
            } else if (clazz == Byte.class || clazz == byte.class) {
                return value.byteValue();
            }
            return assertUnreachable();
        }

        protected EvaluationResult add(Number other) {
            BigDecimal newValue = null;
            try {
                newValue = numberValue.add(other.numberValue);
            } catch (ArithmeticException e) {
                handleArithmeticException(e);
            }
            return createEvaluationResult(other, newValue);
        }

        protected EvaluationResult subtract(Number other) {
            BigDecimal newValue = null;
            try {
                newValue = numberValue.subtract(other.numberValue);
            } catch (ArithmeticException e) {
                handleArithmeticException(e);
            }
            return createEvaluationResult(other, newValue);
        }

        protected EvaluationResult multiply(Number other) {
            BigDecimal newValue = null;
            try {
                newValue = numberValue.multiply(other.numberValue);
            } catch (ArithmeticException e) {
                handleArithmeticException(e);
            }
            return createEvaluationResult(other, newValue);
        }

        protected EvaluationResult divide(Number other) {
            BigDecimal newValue = null;
            try {
                newValue = numberValue.divide(other.numberValue);
            } catch (ArithmeticException e) {
                handleArithmeticException(e);
            }
            return createEvaluationResult(other, newValue);
        }

        protected EvaluationResult mod(Number other) {
            BigDecimal newValue = null;
            try {
                newValue = numberValue.remainder(other.numberValue);
            } catch (ArithmeticException e) {
                handleArithmeticException(e);
            }
            return createEvaluationResult(other, newValue);
        }

        protected void handleArithmeticException(ArithmeticException e) {
            ExpressionLocation location = operatorNode.getLocation();
            throw new ExpressionException(Message.DOMA3014, e,
                    location.getExpression(), operatorNode.getExpression(),
                    location.getPosition(), e);
        }

        protected EvaluationResult createEvaluationResult(Number other,
                BigDecimal newValue) {
            Class<?> realClass = this.priority >= other.priority ? this.realClass
                    : other.realClass;
            Object narrowedValue = narrowValue(newValue, realClass);
            return new EvaluationResult(narrowedValue, realClass);
        }

    }

    protected class ParameterCollector implements
            ExpressionNodeVisitor<Void, List<EvaluationResult>> {

        public ParameterCollection collect(ExpressionNode node) {
            List<EvaluationResult> evaluationResults = new ArrayList<EvaluationResult>();
            node.accept(this, evaluationResults);
            return new ParameterCollection(evaluationResults);
        }

        @Override
        public Void visitEqOperatorNode(EqOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitNeOperatorNode(NeOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitGeOperatorNode(GeOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitGtOperatorNode(GtOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitLeOperatorNode(LeOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitLtOperatorNode(LtOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitCommaOperatorNode(CommaOperatorNode node,
                List<EvaluationResult> p) {
            for (ExpressionNode expressionNode : node.getNodes()) {
                expressionNode.accept(this, p);
            }
            return null;
        }

        @Override
        public Void visitLiteralNode(LiteralNode node, List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitVariableNode(VariableNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitOrOperatorNode(OrOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitAndOperatorNode(AndOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitNotOperatorNode(NotOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitAddOperatorNode(AddOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitSubtractOperatorNode(SubtractOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitMultiplyOperatorNode(MultiplyOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitDivideOperatorNode(DivideOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitModOperatorNode(ModOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitNewOperatorNode(NewOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitMethodOperatorNode(MethodOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitStaticMethodOperatorNode(
                StaticMethodOperatorNode node, List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitFunctionOperatorNode(FunctionOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitFieldOperatorNode(FieldOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitStaticFieldOperatorNode(StaticFieldOperatorNode node,
                List<EvaluationResult> p) {
            evaluate(node, p);
            return null;
        }

        @Override
        public Void visitParensNode(ParensNode node, List<EvaluationResult> p) {
            node.getNode().accept(this, p);
            return null;
        }

        @Override
        public Void visitEmptyNode(EmptyNode node, List<EvaluationResult> p) {
            return null;
        }

        protected void evaluate(ExpressionNode node, List<EvaluationResult> p) {
            EvaluationResult evaluationResult = ExpressionEvaluator.this
                    .evaluateInternal(node);
            p.add(evaluationResult);
        }

    }

    protected static class ParameterCollection {

        protected final Object[] params;

        protected final Class<?>[] paramTypes;

        public ParameterCollection(List<EvaluationResult> evaluationResults) {
            assertNotNull(evaluationResults);
            int size = evaluationResults.size();
            params = new Object[size];
            paramTypes = new Class<?>[size];
            int i = 0;
            for (EvaluationResult result : evaluationResults) {
                params[i] = result.getValue();
                paramTypes[i] = result.getValueClass();
                i++;
            }
        }

        public Object[] getParams() {
            return params;
        }

        public Class<?>[] getParamTypes() {
            return paramTypes;
        }

    }

    protected static class CandidateMethod {
        final int degreeOfcoincidence;
        final Method method;

        CandidateMethod(int degreeOfcoincidence, Method method) {
            this.degreeOfcoincidence = degreeOfcoincidence;
            this.method = method;
        }
    }
}
