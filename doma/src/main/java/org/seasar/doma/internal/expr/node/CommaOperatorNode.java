package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author taedium
 * 
 */
public class CommaOperatorNode implements OperatorNode {

    protected static final int PRIORITY = 0;

    protected final List<ExpressionNode> expressionNodes = new ArrayList<ExpressionNode>();

    protected final ExpressionLocation location;

    protected final String operator;

    public CommaOperatorNode(ExpressionLocation location, String operator) {
        assertNotNull(location, operator);
        this.location = location;
        this.operator = operator;
    }

    public void addNode(ExpressionNode expressionNode) {
        expressionNodes.add(expressionNode);
    }

    public List<ExpressionNode> getNodes() {
        return expressionNodes;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitCommaOperatorNode(this, p);
    }

    public ExpressionLocation getLocation() {
        return location;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (ExpressionNode node : expressionNodes) {
            buf.append(node.toString());
            buf.append(operator);
            buf.append(" ");
        }
        if (buf.length() > 2) {
            buf.setLength(buf.length() - (operator.length() + 1));
        }
        return buf.toString();
    }
}
