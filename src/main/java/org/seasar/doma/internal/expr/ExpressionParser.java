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

import static org.seasar.doma.internal.expr.ExpressionTokenType.*;
import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.seasar.doma.internal.expr.node.AddOperatorNode;
import org.seasar.doma.internal.expr.node.AndOperatorNode;
import org.seasar.doma.internal.expr.node.CommaOperatorNode;
import org.seasar.doma.internal.expr.node.DivideOperatorNode;
import org.seasar.doma.internal.expr.node.EmptyNode;
import org.seasar.doma.internal.expr.node.EqOperatorNode;
import org.seasar.doma.internal.expr.node.ExpressionLocation;
import org.seasar.doma.internal.expr.node.ExpressionNode;
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
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class ExpressionParser {

    protected final Deque<ExpressionNode> expressionNodes = new LinkedList<ExpressionNode>();

    protected final Deque<OperatorNode> operatorNodes = new LinkedList<OperatorNode>();

    protected final ExpressionReducer expressionReducer = new ExpressionReducer();

    protected final String expression;

    protected final String originalExpression;

    protected final int startPosition;

    protected final ExpressionTokenizer tokenizer;

    protected ExpressionTokenType tokenType;

    protected String token;

    public ExpressionParser(String expression) {
        this(expression, expression, 0);
    }

    protected ExpressionParser(String expression, String originalExpression,
            int startPosition) {
        assertNotNull(expression, originalExpression);
        this.expression = expression;
        this.originalExpression = originalExpression;
        this.startPosition = startPosition;
        this.tokenizer = new ExpressionTokenizer(expression);
    }

    public ExpressionNode parse() {
        outer: for (;;) {
            tokenType = tokenizer.next();
            token = tokenizer.getToken();
            switch (tokenType) {
            case VARIABLE: {
                parseVariable();
                break;
            }
            case OPENED_PARENS: {
                parseOpenedParens();
                break;
            }
            case CLOSED_PARENS: {
                break outer;
            }
            case CHAR_LITERAL: {
                parseCharLiteral();
                break;
            }
            case STRING_LITERAL: {
                parseStringLiteral();
                break;
            }
            case INT_LITERAL: {
                parseIntLiteral();
                break;
            }
            case LONG_LITERAL: {
                parseLongLiteral();
                break;
            }
            case FLOAT_LITERAL: {
                parseFloatLiteral();
                break;
            }
            case DOUBLE_LITERAL: {
                parseDoubleLiteral();
                break;
            }
            case BIGDECIMAL_LITERAL: {
                parseBigDecimalLiteral();
                break;
            }
            case ILLEGAL_NUMBER_LITERAL: {
                ExpressionLocation location = getLocation();
                throw new ExpressionException(Message.DOMA3012,
                        location.getExpression(), location.getPosition(), token);
            }
            case TRUE_LITERAL: {
                parseTrueLiteral();
                break;
            }
            case FALSE_LITERAL: {
                parseFalseLiteral();
                break;
            }
            case NULL_LITERAL: {
                parseNullLiteral();
                break;
            }
            case NOT_OPERATOR: {
                parseOperator(new NotOperatorNode(getLocation(), token));
                break;
            }
            case AND_OPERATOR: {
                parseOperator(new AndOperatorNode(getLocation(), token));
                break;
            }
            case OR_OPERATOR: {
                parseOperator(new OrOperatorNode(getLocation(), token));
                break;
            }
            case ADD_OPERATOR: {
                parseOperator(new AddOperatorNode(getLocation(), token));
                break;
            }
            case SUBTRACT_OPERATOR: {
                parseOperator(new SubtractOperatorNode(getLocation(), token));
                break;
            }
            case MULTIPLY_OPERATOR: {
                parseOperator(new MultiplyOperatorNode(getLocation(), token));
                break;
            }
            case DIVIDE_OPERATOR: {
                parseOperator(new DivideOperatorNode(getLocation(), token));
                break;
            }
            case MOD_OPERATOR: {
                parseOperator(new ModOperatorNode(getLocation(), token));
                break;
            }
            case COMMA_OPERATOR: {
                parseOperator(new CommaOperatorNode(getLocation(), token));
                break;
            }
            case EQ_OPERATOR: {
                parseOperator(new EqOperatorNode(getLocation(), token));
                break;
            }
            case NE_OPERATOR: {
                parseOperator(new NeOperatorNode(getLocation(), token));
                break;
            }
            case GE_OPERATOR: {
                parseOperator(new GeOperatorNode(getLocation(), token));
                break;
            }
            case LE_OPERATOR:
                parseOperator(new LeOperatorNode(getLocation(), token));
                break;
            case GT_OPERATOR: {
                parseOperator(new GtOperatorNode(getLocation(), token));
                break;
            }
            case LT_OPERATOR: {
                parseOperator(new LtOperatorNode(getLocation(), token));
                break;
            }
            case NEW_OPERATOR: {
                parseNewOperand();
                break;
            }
            case METHOD_OPERATOR: {
                parseMethodOperand();
                break;
            }
            case STATIC_METHOD_OPERATOR: {
                parseStaticMethodOperand();
                break;
            }
            case FUNCTION_OPERATOR: {
                parseFunctionOperand();
                break;
            }
            case FIELD_OPERATOR: {
                parseFieldOperand();
                break;
            }
            case STATIC_FIELD_OPERATOR: {
                parseStaticFieldOperand();
                break;
            }
            case OTHER: {
                ExpressionLocation location = getLocation();
                throw new ExpressionException(Message.DOMA3011,
                        location.getExpression(), location.getPosition(), token);
            }
            case EOE: {
                break outer;
            }
            default: {
                break;
            }
            }
        }
        reduceAll();
        if (expressionNodes.isEmpty()) {
            return new EmptyNode(getLocation());
        }
        return expressionNodes.pop();
    }

    protected void parseVariable() {
        VariableNode node = new VariableNode(getLocation(), token);
        expressionNodes.push(node);
    }

    protected void parseOpenedParens() {
        int start = tokenizer.getPosition();
        String subExpression = expression.substring(start);
        ExpressionParser parser = new ExpressionParser(subExpression,
                originalExpression, start);
        ExpressionNode childExpressionNode = parser.parse();
        if (parser.tokenType != CLOSED_PARENS) {
            ExpressionLocation location = getLocation();
            throw new ExpressionException(Message.DOMA3026,
                    location.getExpression(), location.getPosition());
        }
        int end = start + parser.tokenizer.getPosition();
        tokenizer.setPosition(end, true);
        ParensNode node = new ParensNode(getLocation(), childExpressionNode);
        expressionNodes.push(node);
    }

    protected void parseClosedParens() {
        reduceAll();
    }

    protected void parseStringLiteral() {
        String value = token.substring(1, token.length() - 1);
        LiteralNode node = new LiteralNode(getLocation(), token, value,
                String.class);
        expressionNodes.push(node);
    }

    protected void parseCharLiteral() {
        char value = token.charAt(1);
        LiteralNode node = new LiteralNode(getLocation(), token, value,
                char.class);
        expressionNodes.push(node);
    }

    protected void parseIntLiteral() {
        int start = token.charAt(0) == '+' ? 1 : 0;
        int end = token.length();
        Integer value = Integer.valueOf(token.substring(start, end));
        LiteralNode node = new LiteralNode(getLocation(), token, value,
                int.class);
        expressionNodes.push(node);
    }

    protected void parseLongLiteral() {
        int start = token.charAt(0) == '+' ? 1 : 0;
        int end = token.length() - 1;
        Long value = Long.valueOf(token.substring(start, end));
        LiteralNode node = new LiteralNode(getLocation(), token, value,
                long.class);
        expressionNodes.push(node);
    }

    protected void parseFloatLiteral() {
        int start = token.charAt(0) == '+' ? 1 : 0;
        int end = token.length() - 1;
        Float value = Float.valueOf(token.substring(start, end));
        LiteralNode node = new LiteralNode(getLocation(), token, value,
                float.class);
        expressionNodes.push(node);
    }

    protected void parseDoubleLiteral() {
        int start = token.charAt(0) == '+' ? 1 : 0;
        int end = token.length() - 1;
        Double value = Double.valueOf(token.substring(start, end));
        LiteralNode node = new LiteralNode(getLocation(), token, value,
                double.class);
        expressionNodes.push(node);
    }

    protected void parseBigDecimalLiteral() {
        int start = 0;
        int end = token.length() - 1;
        BigDecimal value = new BigDecimal(token.substring(start, end));
        LiteralNode node = new LiteralNode(getLocation(), token, value,
                BigDecimal.class);
        expressionNodes.push(node);
    }

    protected void parseTrueLiteral() {
        LiteralNode node = new LiteralNode(getLocation(), token, true,
                boolean.class);
        expressionNodes.push(node);
    }

    protected void parseFalseLiteral() {
        LiteralNode node = new LiteralNode(getLocation(), token, false,
                boolean.class);
        expressionNodes.push(node);
    }

    protected void parseNullLiteral() {
        LiteralNode node = new LiteralNode(getLocation(), token, null,
                void.class);
        expressionNodes.push(node);
    }

    protected void parseMethodOperand() {
        String name = token.substring(1);
        MethodOperatorNode node = new MethodOperatorNode(getLocation(), token,
                name);
        tokenType = tokenizer.next();
        assertEquals(OPENED_PARENS, tokenType);
        parseOpenedParens();
        reduce(node);
    }

    protected void parseStaticMethodOperand() {
        int pos = token.lastIndexOf('@');
        assertTrue(pos > -1);
        String className = token.substring(1, pos);
        String methodName = token.substring(pos + 1);
        StaticMethodOperatorNode node = new StaticMethodOperatorNode(
                getLocation(), token, className, methodName);
        tokenType = tokenizer.next();
        assertEquals(OPENED_PARENS, tokenType);
        parseOpenedParens();
        reduce(node);
    }

    protected void parseFunctionOperand() {
        String name = token.substring(1);
        FunctionOperatorNode node = new FunctionOperatorNode(getLocation(),
                token, name);
        tokenType = tokenizer.next();
        assertEquals(OPENED_PARENS, tokenType);
        parseOpenedParens();
        reduce(node);
    }

    protected void parseFieldOperand() {
        String name = token.substring(1);
        FieldOperatorNode node = new FieldOperatorNode(getLocation(), token,
                name);
        reduce(node);
    }

    protected void parseStaticFieldOperand() {
        int pos = token.lastIndexOf('@');
        assertTrue(pos > -1);
        String className = token.substring(1, pos);
        String fieldName = token.substring(pos + 1);
        StaticFieldOperatorNode node = new StaticFieldOperatorNode(
                getLocation(), token, className, fieldName);
        reduce(node);
    }

    protected void parseNewOperand() {
        StringBuilder buf = new StringBuilder();
        for (; tokenizer.next() != OPENED_PARENS;) {
            if (tokenType != WHITESPACE) {
                buf.append(tokenizer.getToken());
            }
        }
        NewOperatorNode node = new NewOperatorNode(getLocation(), token, buf
                .toString().trim());
        parseOpenedParens();
        reduce(node);
    }

    protected void parseOperator(OperatorNode currentNode) {
        if (operatorNodes.peek() == null) {
            operatorNodes.push(currentNode);
        } else {
            if (currentNode.getPriority() > operatorNodes.peek().getPriority()) {
                operatorNodes.push(currentNode);
            } else {
                for (Iterator<OperatorNode> it = operatorNodes.iterator(); it
                        .hasNext();) {
                    OperatorNode operatorNode = it.next();
                    if (operatorNode.getPriority() > currentNode.getPriority()) {
                        reduce(operatorNode);
                        it.remove();
                    }
                }
                operatorNodes.push(currentNode);
            }
        }
    }

    protected void reduceAll() {
        for (Iterator<OperatorNode> it = operatorNodes.iterator(); it.hasNext();) {
            OperatorNode operator = it.next();
            reduce(operator);
            it.remove();
        }
    }

    protected void reduce(OperatorNode operator) {
        expressionReducer.reduce(operator, expressionNodes);
        expressionNodes.push(operator);
    }

    protected ExpressionLocation getLocation() {
        return new ExpressionLocation(originalExpression, startPosition
                + tokenizer.getPosition());
    }
}
