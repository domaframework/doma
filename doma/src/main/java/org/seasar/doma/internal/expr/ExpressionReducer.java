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

import java.util.Deque;
import java.util.Iterator;

import org.seasar.doma.internal.expr.node.AddOperatorNode;
import org.seasar.doma.internal.expr.node.AndOperatorNode;
import org.seasar.doma.internal.expr.node.CommaOperatorNode;
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
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class ExpressionReducer implements
        ExpressionNodeVisitor<Void, Deque<ExpressionNode>> {

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
    public Void visitAndOperatorNode(AndOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setRightNode(pop(node, p));
        node.setLeftNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitNotOperatorNode(NotOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitAddOperatorNode(AddOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setRightNode(pop(node, p));
        node.setLeftNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitSubtractOperatorNode(SubtractOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setRightNode(pop(node, p));
        node.setLeftNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitMultiplyOperatorNode(MultiplyOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setRightNode(pop(node, p));
        node.setLeftNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitDivideOperatorNode(DivideOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setRightNode(pop(node, p));
        node.setLeftNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitModOperatorNode(ModOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setRightNode(pop(node, p));
        node.setLeftNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitNewOperatorNode(NewOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setParametersNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitMethodOperatorNode(MethodOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setParametersNode(pop(node, p));
        node.setTargetObjectNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitStaticMethodOperatorNode(StaticMethodOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setParametersNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitFunctionOperatorNode(FunctionOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setParametersNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitFieldOperatorNode(FieldOperatorNode node,
            Deque<ExpressionNode> p) {
        node.setTargetObjectNode(pop(node, p));
        return null;
    }

    @Override
    public Void visitStaticFieldOperatorNode(StaticFieldOperatorNode node,
            Deque<ExpressionNode> p) {
        return null;
    }

    @Override
    public Void visitCommaOperatorNode(CommaOperatorNode node,
            Deque<ExpressionNode> p) {
        for (Iterator<ExpressionNode> it = p.descendingIterator(); it.hasNext();) {
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
            throw new ExpressionException(Message.DOMA3010,
                    location.getExpression(), location.getPosition(),
                    node.getExpression());
        }
        return p.pop();
    }
}
