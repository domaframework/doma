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

    R visitStaticMethodOperatorNode(StaticMethodOperatorNode node, P p);

    R visitFunctionOperatorNode(FunctionOperatorNode node, P p);

    R visitFieldOperatorNode(FieldOperatorNode node, P p);

    R visitStaticFieldOperatorNode(StaticFieldOperatorNode node, P p);

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

    R visitModOperatorNode(ModOperatorNode node, P p);

    R visitParensNode(ParensNode node, P p);

    R visitLiteralNode(LiteralNode node, P p);

    R visitVariableNode(VariableNode node, P p);

    R visitEmptyNode(EmptyNode node, P p);

}
