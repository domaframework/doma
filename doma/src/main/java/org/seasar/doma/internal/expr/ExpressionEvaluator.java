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
package org.seasar.doma.internal.expr;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.doma.DomaMessageCode;
import org.seasar.doma.domain.Domain;
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
import org.seasar.doma.internal.expr.node.GeOperatorNode;
import org.seasar.doma.internal.expr.node.GtOperatorNode;
import org.seasar.doma.internal.expr.node.LeOperatorNode;
import org.seasar.doma.internal.expr.node.LiteralNode;
import org.seasar.doma.internal.expr.node.LtOperatorNode;
import org.seasar.doma.internal.expr.node.MethodOperatorNode;
import org.seasar.doma.internal.expr.node.MultiplyOperatorNode;
import org.seasar.doma.internal.expr.node.NeOperatorNode;
import org.seasar.doma.internal.expr.node.NewOperatorNode;
import org.seasar.doma.internal.expr.node.NotOperatorNode;
import org.seasar.doma.internal.expr.node.OrOperatorNode;
import org.seasar.doma.internal.expr.node.ParensNode;
import org.seasar.doma.internal.expr.node.SubtractOperatorNode;
import org.seasar.doma.internal.expr.node.VariableNode;
import org.seasar.doma.internal.util.ConstructorUtil;
import org.seasar.doma.internal.util.MethodUtil;


/**
 * @author taedium
 * 
 */
public class ExpressionEvaluator implements
        ExpressionNodeVisitor<EvaluationResult, Void> {

    protected final Map<String, Object> variableValues = new HashMap<String, Object>();

    public ExpressionEvaluator() {
    }

    public ExpressionEvaluator(Map<String, ? extends Object> variableValues) {
        this.variableValues.putAll(variableValues);
    }

    public void add(String varialbeName, Object value) {
        variableValues.put(varialbeName, value);
    }

    public EvaluationResult evaluate(ExpressionNode node) {
        return node.accept(this, null);
    }

    @Override
    public EvaluationResult visitEqOperatorNode(EqOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        left = getEnclosedValueIfDomain(left);
        Object right = node.getRightNode().accept(this, p).getValue();
        right = getEnclosedValueIfDomain(right);
        if (left == null && right == null) {
            return new EvaluationResult(true, boolean.class);
        }
        if (left == null || right == null) {
            return new EvaluationResult(false, boolean.class);
        }
        try {
            @SuppressWarnings("unchecked")
            Comparable<Object> c1 = Comparable.class.cast(left);
            @SuppressWarnings("unchecked")
            Comparable<Object> c2 = Comparable.class.cast(right);
            return new EvaluationResult(c1.compareTo(c2) == 0, boolean.class);
        } catch (ClassCastException e) {
            ExpressionLocation location = node.getLocation();
            throw new ExpressionException(DomaMessageCode.DOMA3008, e, location
                    .getExpression(), node.getOperator(), e);
        }
    }

    @Override
    public EvaluationResult visitNeOperatorNode(NeOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        left = getEnclosedValueIfDomain(left);
        Object right = node.getRightNode().accept(this, p).getValue();
        right = getEnclosedValueIfDomain(right);
        if (left == null && right == null) {
            return new EvaluationResult(false, boolean.class);
        }
        if (left == null || right == null) {
            return new EvaluationResult(true, boolean.class);
        }
        try {
            @SuppressWarnings("unchecked")
            Comparable<Object> c1 = Comparable.class.cast(left);
            @SuppressWarnings("unchecked")
            Comparable<Object> c2 = Comparable.class.cast(right);
            return new EvaluationResult(c1.compareTo(c2) != 0, boolean.class);
        } catch (ClassCastException e) {
            ExpressionLocation location = node.getLocation();
            throw new ExpressionException(DomaMessageCode.DOMA3008, e, location
                    .getExpression(), node.getOperator(), e);
        }
    }

    @Override
    public EvaluationResult visitGeOperatorNode(GeOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        left = getEnclosedValueIfDomain(left);
        Object right = node.getRightNode().accept(this, p).getValue();
        right = getEnclosedValueIfDomain(right);
        return new EvaluationResult(compare(node, left, right) >= 0,
                boolean.class);
    }

    @Override
    public EvaluationResult visitGtOperatorNode(GtOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        left = getEnclosedValueIfDomain(left);
        Object right = node.getRightNode().accept(this, p).getValue();
        right = getEnclosedValueIfDomain(right);
        return new EvaluationResult(compare(node, left, right) > 0,
                boolean.class);
    }

    @Override
    public EvaluationResult visitLeOperatorNode(LeOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        left = getEnclosedValueIfDomain(left);
        Object right = node.getRightNode().accept(this, p).getValue();
        right = getEnclosedValueIfDomain(right);
        return new EvaluationResult(compare(node, left, right) <= 0,
                boolean.class);
    }

    @Override
    public EvaluationResult visitLtOperatorNode(LtOperatorNode node, Void p) {
        Object left = node.getLeftNode().accept(this, p).getValue();
        left = getEnclosedValueIfDomain(left);
        Object right = node.getRightNode().accept(this, p).getValue();
        right = getEnclosedValueIfDomain(right);
        return new EvaluationResult(compare(node, left, right) < 0,
                boolean.class);
    }

    protected Object getEnclosedValueIfDomain(Object maybeDomain) {
        if (Domain.class.isInstance(maybeDomain)) {
            Domain<?, ?> domain = Domain.class.cast(maybeDomain);
            return domain.get();
        }
        return maybeDomain;
    }

    protected int compare(ComparisonOperatorNode node, Object left, Object right)
            throws ClassCastException {
        if (left == null || right == null) {
            ExpressionLocation location = node.getLocation();
            throw new ExpressionException(DomaMessageCode.DOMA3009, location
                    .getExpression(), location.getPosition(), node
                    .getOperator());
        }
        try {
            @SuppressWarnings("unchecked")
            Comparable<Object> c1 = Comparable.class.cast(left);
            @SuppressWarnings("unchecked")
            Comparable<Object> c2 = Comparable.class.cast(right);
            return c1.compareTo(c2);
        } catch (ClassCastException e) {
            ExpressionLocation location = node.getLocation();
            throw new ExpressionException(DomaMessageCode.DOMA3008, e, location
                    .getExpression(), location.getPosition(), node
                    .getOperator(), e);
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
        EvaluationResult leftResult = leftNode.accept(this, p);
        Decimal leftDecimal = new Decimal(node, leftNode, leftResult);
        ExpressionNode rightNode = node.getRightNode();
        EvaluationResult rightResult = rightNode.accept(this, p);
        Decimal rightDecimal = new Decimal(node, rightNode, rightResult);
        return leftDecimal.add(rightDecimal);
    }

    @Override
    public EvaluationResult visitSubtractOperatorNode(
            SubtractOperatorNode node, Void p) {
        ExpressionNode leftNode = node.getLeftNode();
        EvaluationResult leftResult = leftNode.accept(this, p);
        Decimal leftDecimal = new Decimal(node, leftNode, leftResult);
        ExpressionNode rightNode = node.getRightNode();
        EvaluationResult rightResult = rightNode.accept(this, p);
        Decimal rightDecimal = new Decimal(node, rightNode, rightResult);
        return leftDecimal.subtract(rightDecimal);
    }

    @Override
    public EvaluationResult visitMultiplyOperatorNode(
            MultiplyOperatorNode node, Void p) {
        ExpressionNode leftNode = node.getLeftNode();
        EvaluationResult leftResult = leftNode.accept(this, p);
        Decimal leftDecimal = new Decimal(node, leftNode, leftResult);
        ExpressionNode rightNode = node.getRightNode();
        EvaluationResult rightResult = rightNode.accept(this, p);
        Decimal rightDecimal = new Decimal(node, rightNode, rightResult);
        return leftDecimal.multiply(rightDecimal);
    }

    @Override
    public EvaluationResult visitDivideOperatorNode(DivideOperatorNode node,
            Void p) {
        ExpressionNode leftNode = node.getLeftNode();
        EvaluationResult leftResult = leftNode.accept(this, p);
        Decimal leftDecimal = new Decimal(node, leftNode, leftResult);
        ExpressionNode rightNode = node.getRightNode();
        EvaluationResult rightResult = rightNode.accept(this, p);
        Decimal rightDecimal = new Decimal(node, rightNode, rightResult);
        return leftDecimal.divide(rightDecimal);
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
        ParameterCollector collector = new ParameterCollector(this);
        List<EvaluationResult> paramResults = collector.collect(node
                .getParametersNode());
        int size = paramResults.size();
        Object[] params = new Object[size];
        Class<?>[] paramTypes = new Class<?>[size];
        int i = 0;
        for (Iterator<EvaluationResult> it = paramResults.iterator(); it
                .hasNext();) {
            EvaluationResult evaluationResult = it.next();
            params[i] = evaluationResult.getValue();
            paramTypes[i] = evaluationResult.getValueClass();
            i++;
        }
        ExpressionLocation location = node.getLocation();
        String className = node.getClassName();
        Class<?> clazz = forClassName(location, className);
        Constructor<?> constructor = findConstructor(location, clazz, paramTypes);
        return invokeConstructor(location, clazz, constructor, params);
    }

    protected Class<?> forClassName(ExpressionLocation location,
            String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ExpressionException(DomaMessageCode.DOMA3005, e, location
                    .getExpression(), location.getPosition(), className);
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
        throw new ExpressionException(DomaMessageCode.DOMA3006, location
                .getExpression(), location.getPosition(), ConstructorUtil
                .toSignature(clazz, paramTypes));
    }

    protected EvaluationResult invokeConstructor(ExpressionLocation location,
            Class<?> clazz, Constructor<?> constructor, Object... params) {
        Object value = null;
        try {
            value = ConstructorUtil.newInstance(constructor, params);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new ExpressionException(DomaMessageCode.DOMA3007, cause, location
                    .getExpression(), location.getPosition(), ConstructorUtil
                    .toSignature(constructor), cause);
        }
        return new EvaluationResult(value, clazz);
    }

    @Override
    public EvaluationResult visitMethodOperatorNode(MethodOperatorNode node,
            Void p) {
        EvaluationResult targetResult = node.getTargetObjectNode()
                .accept(this, p);
        Object target = targetResult.getValue();
        ParameterCollector collector = new ParameterCollector(this);
        List<EvaluationResult> paramResults = collector.collect(node
                .getParametersNode());
        int size = paramResults.size();
        Object[] params = new Object[size];
        Class<?>[] paramTypes = new Class<?>[size];
        int i = 0;
        for (Iterator<EvaluationResult> it = paramResults.iterator(); it
                .hasNext();) {
            EvaluationResult evaluationResult = it.next();
            params[i] = evaluationResult.getValue();
            paramTypes[i] = evaluationResult.getValueClass();
            i++;
        }
        ExpressionLocation location = node.getLocation();
        Method method = findMethod(location, node.getName(), target, paramTypes);
        return invokeMethod(location, method, target, paramTypes, params);
    }

    protected EvaluationResult invokeMethod(ExpressionLocation location,
            Method method, Object target, Class<?>[] paramTypes, Object[] params) {
        Object value;
        try {
            value = MethodUtil.invoke(method, target, params);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new ExpressionException(DomaMessageCode.DOMA3001, cause, location
                    .getExpression(), location.getPosition(), target.getClass()
                    .getName(), method.getName(), cause);
        }
        Class<?> returnType = method.getReturnType();
        return new EvaluationResult(value, returnType);
    }

    protected Method findMethod(ExpressionLocation location, String methodName,
            Object target, Class<?>[] paramTypes) {
        List<Method> methods = new ArrayList<Method>();
        for (Class<?> clazz = target.getClass(); clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    methods.add(method);
                }
            }
        }
        outer: for (Method method : methods) {
            Class<?> types[] = method.getParameterTypes();
            if (types.length == paramTypes.length) {
                for (int i = 0; i < types.length; i++) {
                    if (!types[i].isAssignableFrom(paramTypes[i])) {
                        continue outer;
                    }
                }
                return method;
            }
        }
        throw new ExpressionException(DomaMessageCode.DOMA3002, location
                .getExpression(), location.getPosition(), target.getClass()
                .getName(), methodName);
    }

    @Override
    public EvaluationResult visitVariableNode(VariableNode node, Void p) {
        String variableName = node.getName();
        Object value = variableValues.get(node.getName());
        if (value == null) {
            ExpressionLocation location = node.getLocation();
            throw new ExpressionException(DomaMessageCode.DOMA3003, location
                    .getExpression(), location.getPosition(), variableName);
        }
        return new EvaluationResult(value, value.getClass());
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

    protected static class Decimal {

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

        protected final ExpressionNode operandNode;

        protected final BigDecimal decimalValue;

        protected final Class<?> realClass;

        protected final Integer priority;

        protected Decimal(ArithmeticOperatorNode operatorNode,
                ExpressionNode operandNode, EvaluationResult evaluationResult) {
            Object value = evaluationResult.getValue();
            Class<?> valueClass = evaluationResult.getValueClass();
            if (value == null) {
                ExpressionLocation location = operandNode.getLocation();
                throw new ExpressionException(DomaMessageCode.DOMA3015, location
                        .getExpression(), location.getPosition(), operatorNode
                        .getOperator());
            }
            priority = priorityMap.get(evaluationResult.getValueClass());
            if (priority == null) {
                ExpressionLocation location = operandNode.getLocation();
                throw new ExpressionException(DomaMessageCode.DOMA3013, location
                        .getExpression(), location.getPosition(), operatorNode
                        .getOperator(), value, valueClass.getName());
            }
            this.operandNode = operandNode;
            this.operatorNode = operatorNode;
            this.decimalValue = widenValue(operatorNode, operandNode, value, valueClass);
            this.realClass = valueClass;
        }

        protected BigDecimal widenValue(ArithmeticOperatorNode operatorNode,
                ExpressionNode operandNode, Object value, Class<?> clazz) {
            if (clazz == BigDecimal.class) {
                return BigDecimal.class.cast(value);
            } else if (clazz == BigInteger.class) {
                BigInteger v = BigInteger.class.cast(value);
                return new BigDecimal(v);
            } else if (clazz == Double.class || clazz == double.class) {
                Double v = Double.class.cast(value);
                return new BigDecimal(v);
            } else if (clazz == Float.class || clazz == float.class) {
                Float v = Float.class.cast(value);
                return new BigDecimal(v);
            } else if (clazz == Long.class || clazz == long.class) {
                Long v = Long.class.cast(value);
                return new BigDecimal(v);
            } else if (clazz == Integer.class || clazz == int.class) {
                Integer v = Integer.class.cast(value);
                return new BigDecimal(v);
            } else if (clazz == Short.class || clazz == short.class) {
                Short v = Short.class.cast(value);
                return new BigDecimal(v);
            } else if (clazz == Byte.class || clazz == byte.class) {
                Byte v = Byte.class.cast(value);
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

        protected EvaluationResult add(Decimal other) {
            BigDecimal newValue = null;
            try {
                newValue = decimalValue.add(other.decimalValue);
            } catch (ArithmeticException e) {
                handleArithmeticException(e);
            }
            return createEvaluationResult(other, newValue);
        }

        protected EvaluationResult subtract(Decimal other) {
            BigDecimal newValue = null;
            try {
                newValue = decimalValue.subtract(other.decimalValue);
            } catch (ArithmeticException e) {
                handleArithmeticException(e);
            }
            return createEvaluationResult(other, newValue);
        }

        protected EvaluationResult multiply(Decimal other) {
            BigDecimal newValue = null;
            try {
                newValue = decimalValue.multiply(other.decimalValue);
            } catch (ArithmeticException e) {
                handleArithmeticException(e);
            }
            return createEvaluationResult(other, newValue);
        }

        protected EvaluationResult divide(Decimal other) {
            BigDecimal newValue = null;
            try {
                newValue = decimalValue.divide(other.decimalValue);
            } catch (ArithmeticException e) {
                handleArithmeticException(e);
            }
            return createEvaluationResult(other, newValue);
        }

        protected void handleArithmeticException(ArithmeticException e) {
            ExpressionLocation location = operatorNode.getLocation();
            throw new ExpressionException(DomaMessageCode.DOMA3014, e, location
                    .getExpression(), operatorNode.getOperator(), location
                    .getPosition(), e);
        }

        protected EvaluationResult createEvaluationResult(Decimal other,
                BigDecimal newValue) {
            Class<?> realClass = this.priority >= other.priority ? this.realClass
                    : other.realClass;
            Object narrowedValue = narrowValue(newValue, realClass);
            return new EvaluationResult(narrowedValue, realClass);
        }

    }
}
