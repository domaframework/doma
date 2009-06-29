package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public interface ExpressionNodeVisitor<R, P> {

    R visitAndOperatorNode(AndOperatorNode node, P p);

    R visitOrOperatorNode(OrOperatorNode node, P p);

    R visitNotOperatorNode(NotOperatorNode node, P p);

    R visitNewOperatorNode(NewOperatorNode node, P p);

    R visitMethodOperatorNode(MethodOperatorNode node, P p);

    R visitCommaOperatorNode(CommaOperatorNode node, P p);

    R visitEqOperatorNode(EqOperatorNode node, P p);

    R visitNeOperatorNode(NeOperatorNode node, P p);

    R visitGeOperatorNode(GeOperatorNode node, P p);

    R visitLeOperatorNode(LeOperatorNode node, P p);

    R visitGtOperatorNode(GtOperatorNode node, P p);

    R visitLtOperatorNode(LtOperatorNode node, P p);

    R visitAddOperatorNode(AddOperatorNode node, P p);

    R visitSubtractOperatorNode(SubtractOperatorNode node, P p);

    R visitMultiplyOperatorNode(MultiplyOperatorNode node, P p);

    R visitDivideOperatorNode(DivideOperatorNode node, P p);

    R visitParensNode(ParensNode node, P p);

    R visitLiteralNode(LiteralNode node, P p);

    R visitVariableNode(VariableNode node, P p);

    R visitEmptyNode(EmptyNode node, P p);

}
