package org.seasar.doma.internal.expr;

import static org.seasar.doma.internal.expr.ExpressionTokenType.CLOSED_PARENS;
import static org.seasar.doma.internal.expr.ExpressionTokenType.OPENED_PARENS;
import static org.seasar.doma.internal.expr.ExpressionTokenType.WHITESPACE;
import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.math.BigDecimal;
import java.util.Deque;
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

public class ExpressionParser {

  protected final Deque<ExpressionNode> expressionNodes = new LinkedList<>();

  protected final Deque<OperatorNode> operatorNodes = new LinkedList<>();

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

  protected ExpressionParser(String expression, String originalExpression, int startPosition) {
    assertNotNull(expression, originalExpression);
    this.expression = expression;
    this.originalExpression = originalExpression;
    this.startPosition = startPosition;
    this.tokenizer = new ExpressionTokenizer(expression);
  }

  public ExpressionNode parse() {
    outer:
    for (; ; ) {
      tokenType = tokenizer.next();
      token = tokenizer.getToken();
      switch (tokenType) {
        case VARIABLE:
          {
            parseVariable();
            break;
          }
        case OPENED_PARENS:
          {
            parseOpenedParens();
            break;
          }
        case CLOSED_PARENS:
          {
            break outer;
          }
        case CHAR_LITERAL:
          {
            parseCharLiteral();
            break;
          }
        case STRING_LITERAL:
          {
            parseStringLiteral();
            break;
          }
        case INT_LITERAL:
          {
            parseIntLiteral();
            break;
          }
        case LONG_LITERAL:
          {
            parseLongLiteral();
            break;
          }
        case FLOAT_LITERAL:
          {
            parseFloatLiteral();
            break;
          }
        case DOUBLE_LITERAL:
          {
            parseDoubleLiteral();
            break;
          }
        case BIGDECIMAL_LITERAL:
          {
            parseBigDecimalLiteral();
            break;
          }
        case ILLEGAL_NUMBER_LITERAL:
          {
            var location = getLocation();
            throw new ExpressionException(
                Message.DOMA3012, location.getExpression(), location.getPosition(), token);
          }
        case TRUE_LITERAL:
          {
            parseTrueLiteral();
            break;
          }
        case FALSE_LITERAL:
          {
            parseFalseLiteral();
            break;
          }
        case NULL_LITERAL:
          {
            parseNullLiteral();
            break;
          }
        case NOT_OPERATOR:
          {
            parseOperator(new NotOperatorNode(getLocation(), token));
            break;
          }
        case AND_OPERATOR:
          {
            parseOperator(new AndOperatorNode(getLocation(), token));
            break;
          }
        case OR_OPERATOR:
          {
            parseOperator(new OrOperatorNode(getLocation(), token));
            break;
          }
        case ADD_OPERATOR:
          {
            parseOperator(new AddOperatorNode(getLocation(), token));
            break;
          }
        case SUBTRACT_OPERATOR:
          {
            parseOperator(new SubtractOperatorNode(getLocation(), token));
            break;
          }
        case MULTIPLY_OPERATOR:
          {
            parseOperator(new MultiplyOperatorNode(getLocation(), token));
            break;
          }
        case DIVIDE_OPERATOR:
          {
            parseOperator(new DivideOperatorNode(getLocation(), token));
            break;
          }
        case MOD_OPERATOR:
          {
            parseOperator(new ModOperatorNode(getLocation(), token));
            break;
          }
        case COMMA_OPERATOR:
          {
            parseOperator(new CommaOperatorNode(getLocation(), token));
            break;
          }
        case EQ_OPERATOR:
          {
            parseOperator(new EqOperatorNode(getLocation(), token));
            break;
          }
        case NE_OPERATOR:
          {
            parseOperator(new NeOperatorNode(getLocation(), token));
            break;
          }
        case GE_OPERATOR:
          {
            parseOperator(new GeOperatorNode(getLocation(), token));
            break;
          }
        case LE_OPERATOR:
          parseOperator(new LeOperatorNode(getLocation(), token));
          break;
        case GT_OPERATOR:
          {
            parseOperator(new GtOperatorNode(getLocation(), token));
            break;
          }
        case LT_OPERATOR:
          {
            parseOperator(new LtOperatorNode(getLocation(), token));
            break;
          }
        case NEW_OPERATOR:
          {
            parseNewOperand();
            break;
          }
        case METHOD_OPERATOR:
          {
            parseMethodOperand();
            break;
          }
        case STATIC_METHOD_OPERATOR:
          {
            parseStaticMethodOperand();
            break;
          }
        case FUNCTION_OPERATOR:
          {
            parseFunctionOperand();
            break;
          }
        case FIELD_OPERATOR:
          {
            parseFieldOperand();
            break;
          }
        case STATIC_FIELD_OPERATOR:
          {
            parseStaticFieldOperand();
            break;
          }
        case OTHER:
          {
            var location = getLocation();
            throw new ExpressionException(
                Message.DOMA3011, location.getExpression(), location.getPosition(), token);
          }
        case EOE:
          {
            break outer;
          }
        default:
          {
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
    var node = new VariableNode(getLocation(), token);
    expressionNodes.push(node);
  }

  protected void parseOpenedParens() {
    var start = tokenizer.getPosition();
    var subExpression = expression.substring(start);
    var parser = new ExpressionParser(subExpression, originalExpression, start);
    var childExpressionNode = parser.parse();
    if (parser.tokenType != CLOSED_PARENS) {
      var location = getLocation();
      throw new ExpressionException(
          Message.DOMA3026, location.getExpression(), location.getPosition());
    }
    var end = start + parser.tokenizer.getPosition();
    tokenizer.setPosition(end, true);
    var node = new ParensNode(getLocation(), childExpressionNode);
    expressionNodes.push(node);
  }

  protected void parseClosedParens() {
    reduceAll();
  }

  protected void parseStringLiteral() {
    var value = token.substring(1, token.length() - 1);
    var node = new LiteralNode(getLocation(), token, value, String.class);
    expressionNodes.push(node);
  }

  protected void parseCharLiteral() {
    var value = token.charAt(1);
    var node = new LiteralNode(getLocation(), token, value, char.class);
    expressionNodes.push(node);
  }

  protected void parseIntLiteral() {
    var start = token.charAt(0) == '+' ? 1 : 0;
    var end = token.length();
    var value = Integer.valueOf(token.substring(start, end));
    var node = new LiteralNode(getLocation(), token, value, int.class);
    expressionNodes.push(node);
  }

  protected void parseLongLiteral() {
    var start = token.charAt(0) == '+' ? 1 : 0;
    var end = token.length() - 1;
    var value = Long.valueOf(token.substring(start, end));
    var node = new LiteralNode(getLocation(), token, value, long.class);
    expressionNodes.push(node);
  }

  protected void parseFloatLiteral() {
    var start = token.charAt(0) == '+' ? 1 : 0;
    var end = token.length() - 1;
    var value = Float.valueOf(token.substring(start, end));
    var node = new LiteralNode(getLocation(), token, value, float.class);
    expressionNodes.push(node);
  }

  protected void parseDoubleLiteral() {
    var start = token.charAt(0) == '+' ? 1 : 0;
    var end = token.length() - 1;
    var value = Double.valueOf(token.substring(start, end));
    var node = new LiteralNode(getLocation(), token, value, double.class);
    expressionNodes.push(node);
  }

  protected void parseBigDecimalLiteral() {
    var start = 0;
    var end = token.length() - 1;
    var value = new BigDecimal(token.substring(start, end));
    var node = new LiteralNode(getLocation(), token, value, BigDecimal.class);
    expressionNodes.push(node);
  }

  protected void parseTrueLiteral() {
    var node = new LiteralNode(getLocation(), token, true, boolean.class);
    expressionNodes.push(node);
  }

  protected void parseFalseLiteral() {
    var node = new LiteralNode(getLocation(), token, false, boolean.class);
    expressionNodes.push(node);
  }

  protected void parseNullLiteral() {
    var node = new LiteralNode(getLocation(), token, null, void.class);
    expressionNodes.push(node);
  }

  protected void parseMethodOperand() {
    var name = token.substring(1);
    var node = new MethodOperatorNode(getLocation(), token, name);
    tokenType = tokenizer.next();
    assertEquals(OPENED_PARENS, tokenType);
    parseOpenedParens();
    reduce(node);
  }

  protected void parseStaticMethodOperand() {
    var pos = token.lastIndexOf('@');
    assertTrue(pos > -1);
    var className = token.substring(1, pos);
    var methodName = token.substring(pos + 1);
    var node = new StaticMethodOperatorNode(getLocation(), token, className, methodName);
    tokenType = tokenizer.next();
    assertEquals(OPENED_PARENS, tokenType);
    parseOpenedParens();
    reduce(node);
  }

  protected void parseFunctionOperand() {
    var name = token.substring(1);
    var node = new FunctionOperatorNode(getLocation(), token, name);
    tokenType = tokenizer.next();
    assertEquals(OPENED_PARENS, tokenType);
    parseOpenedParens();
    reduce(node);
  }

  protected void parseFieldOperand() {
    var name = token.substring(1);
    var node = new FieldOperatorNode(getLocation(), token, name);
    reduce(node);
  }

  protected void parseStaticFieldOperand() {
    var pos = token.lastIndexOf('@');
    assertTrue(pos > -1);
    var className = token.substring(1, pos);
    var fieldName = token.substring(pos + 1);
    var node = new StaticFieldOperatorNode(getLocation(), token, className, fieldName);
    reduce(node);
  }

  protected void parseNewOperand() {
    var buf = new StringBuilder();
    for (; tokenizer.next() != OPENED_PARENS; ) {
      if (tokenType != WHITESPACE) {
        buf.append(tokenizer.getToken());
      }
    }
    var node = new NewOperatorNode(getLocation(), token, buf.toString().trim());
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
        for (var it = operatorNodes.iterator(); it.hasNext(); ) {
          var operatorNode = it.next();
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
    for (var it = operatorNodes.iterator(); it.hasNext(); ) {
      var operator = it.next();
      reduce(operator);
      it.remove();
    }
  }

  protected void reduce(OperatorNode operator) {
    expressionReducer.reduce(operator, expressionNodes);
    expressionNodes.push(operator);
  }

  protected ExpressionLocation getLocation() {
    return new ExpressionLocation(originalExpression, startPosition + tokenizer.getPosition());
  }
}
