package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public class MethodOperatorNode implements OperatorNode {

    protected static final int PRIORITY = 0;

    protected final ExpressionLocation location;

    protected final String operator;

    protected final String name;

    protected ExpressionNode targetObjectNode;

    protected ExpressionNode parametersNode;

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    public MethodOperatorNode(ExpressionLocation location, String operator,
            String name) {
        assertNotNull(location, name);
        this.location = location;
        this.operator = operator;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ExpressionNode getTargetObjectNode() {
        return targetObjectNode;
    }

    public void setTargetObjectNode(ExpressionNode targetObjectNode) {
        this.targetObjectNode = targetObjectNode;
    }

    public ExpressionNode getParametersNode() {
        return parametersNode;
    }

    public void setParametersNode(ExpressionNode parametersNode) {
        this.parametersNode = parametersNode;
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitMethodOperatorNode(this, p);
    }

    public ExpressionLocation getLocation() {
        return location;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return targetObjectNode + operator + parametersNode;
    }

}
