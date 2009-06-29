package org.seasar.doma.internal.expr;

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.expr.node.AddOperatorNode;
import org.seasar.doma.internal.expr.node.AndOperatorNode;
import org.seasar.doma.internal.expr.node.CommaOperatorNode;
import org.seasar.doma.internal.expr.node.DivideOperatorNode;
import org.seasar.doma.internal.expr.node.EmptyNode;
import org.seasar.doma.internal.expr.node.EqOperatorNode;
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


/**
 * @author taedium
 * 
 */
public class ParameterCollector implements
        ExpressionNodeVisitor<Void, List<EvaluationResult>> {

    protected final ExpressionEvaluator evaluator;

    public ParameterCollector(ExpressionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public List<EvaluationResult> collect(ExpressionNode node) {
        List<EvaluationResult> EvaluationResults = new ArrayList<EvaluationResult>();
        node.accept(this, EvaluationResults);
        return EvaluationResults;
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
    public Void visitVariableNode(VariableNode node, List<EvaluationResult> p) {
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
    public Void visitParensNode(ParensNode node, List<EvaluationResult> p) {
        node.getNode().accept(this, p);
        return null;
    }

    @Override
    public Void visitEmptyNode(EmptyNode node, List<EvaluationResult> p) {
        return null;
    }

    protected void evaluate(ExpressionNode node, List<EvaluationResult> p) {
        EvaluationResult evaluationResult = evaluator.evaluate(node);
        p.add(evaluationResult);
    }

}
