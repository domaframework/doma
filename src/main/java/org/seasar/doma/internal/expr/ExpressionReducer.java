package org.seasar.doma.internal.expr;

import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.util.Deque;
import java.util.Iterator;
import org.seasar.doma.internal.expr.node.*;
import org.seasar.doma.message.Message;

public class ExpressionReducer implements ExpressionNodeVisitor<Void, Deque<ExpressionNode>> {

  public void reduce(OperatorNode operator, Deque<ExpressionNode> operands) {
    operator.accept(this, operands);
  }

  @Override
  public Void visitEqOperatorNode(EqOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitNeOperatorNode(NeOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitGeOperatorNode(GeOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitGtOperatorNode(GtOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitLeOperatorNode(LeOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitLtOperatorNode(LtOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitOrOperatorNode(OrOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitAndOperatorNode(AndOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitNotOperatorNode(NotOperatorNode node, Deque<ExpressionNode> p) {
    node.setNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitAddOperatorNode(AddOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitSubtractOperatorNode(SubtractOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitMultiplyOperatorNode(MultiplyOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitDivideOperatorNode(DivideOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitModOperatorNode(ModOperatorNode node, Deque<ExpressionNode> p) {
    node.setRightNode(pop(node, p));
    node.setLeftNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitNewOperatorNode(NewOperatorNode node, Deque<ExpressionNode> p) {
    node.setParametersNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitMethodOperatorNode(MethodOperatorNode node, Deque<ExpressionNode> p) {
    node.setParametersNode(pop(node, p));
    node.setTargetObjectNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitStaticMethodOperatorNode(
      StaticMethodOperatorNode node, Deque<ExpressionNode> p) {
    node.setParametersNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitFunctionOperatorNode(FunctionOperatorNode node, Deque<ExpressionNode> p) {
    node.setParametersNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitFieldOperatorNode(FieldOperatorNode node, Deque<ExpressionNode> p) {
    node.setTargetObjectNode(pop(node, p));
    return null;
  }

  @Override
  public Void visitStaticFieldOperatorNode(StaticFieldOperatorNode node, Deque<ExpressionNode> p) {
    return null;
  }

  @Override
  public Void visitCommaOperatorNode(CommaOperatorNode node, Deque<ExpressionNode> p) {
    for (Iterator<ExpressionNode> it = p.descendingIterator(); it.hasNext(); ) {
      node.addNode(it.next());
      it.remove();
    }
    return null;
  }

  @Override
  public Void visitLiteralNode(LiteralNode node, Deque<ExpressionNode> p) {
    assertUnreachable();
    return null;
  }

  @Override
  public Void visitVariableNode(VariableNode node, Deque<ExpressionNode> p) {
    assertUnreachable();
    return null;
  }

  @Override
  public Void visitEmptyNode(EmptyNode node, Deque<ExpressionNode> p) {
    assertUnreachable();
    return null;
  }

  @Override
  public Void visitParensNode(ParensNode node, Deque<ExpressionNode> p) {
    assertUnreachable();
    return null;
  }

  protected ExpressionNode pop(OperatorNode node, Deque<ExpressionNode> p) {
    if (p.peek() == null) {
      ExpressionLocation location = node.getLocation();
      throw new ExpressionException(
          Message.DOMA3010, location.getExpression(), location.getPosition(), node.getExpression());
    }
    return p.pop();
  }
}
