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
package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.CommentNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNode;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.EndNode;
import org.seasar.doma.internal.jdbc.sql.node.EolNode;
import org.seasar.doma.internal.jdbc.sql.node.ExpandNode;
import org.seasar.doma.internal.jdbc.sql.node.ForBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.ForNode;
import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.GroupByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.HavingClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.IfBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.IfNode;
import org.seasar.doma.internal.jdbc.sql.node.LiteralVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.LogicalOperatorNode;
import org.seasar.doma.internal.jdbc.sql.node.OptionClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.OtherNode;
import org.seasar.doma.internal.jdbc.sql.node.ParensNode;
import org.seasar.doma.internal.jdbc.sql.node.PopulateNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.SetClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.UpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.UpdateStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.WhitespaceNode;
import org.seasar.doma.internal.jdbc.sql.node.WordNode;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author nakamura-to
 * 
 */
public class SimpleSqlNodeVisitor<R, P> implements SqlNodeVisitor<R, P> {

    protected R defaultAction(SqlNode node, P p) {
        return null;
    }

    @Override
    public R visitAnonymousNode(AnonymousNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitBindVariableNode(BindVariableNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitCommentNode(CommentNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitElseifNode(ElseifNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitElseNode(ElseNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitEmbeddedVariableNode(EmbeddedVariableNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitEndNode(EndNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitEolNode(EolNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitExpandNode(ExpandNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitForBlockNode(ForBlockNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitForNode(ForNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitForUpdateClauseNode(ForUpdateClauseNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitFragmentNode(FragmentNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitFromClauseNode(FromClauseNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitGroupByClauseNode(GroupByClauseNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitHavingClauseNode(HavingClauseNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitIfBlockNode(IfBlockNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitIfNode(IfNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitLiteralVariableNode(LiteralVariableNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitLogicalOperatorNode(LogicalOperatorNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitOptionClauseNode(OptionClauseNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitOrderByClauseNode(OrderByClauseNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitOtherNode(OtherNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitParensNode(ParensNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitPopulateNode(PopulateNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitSelectClauseNode(SelectClauseNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitSelectStatementNode(SelectStatementNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitSetClauseNode(SetClauseNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitUpdateClauseNode(UpdateClauseNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitUpdateStatementNode(UpdateStatementNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitWhereClauseNode(WhereClauseNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitWhitespaceNode(WhitespaceNode node, P p) {
        return defaultAction(node, p);
    }

    @Override
    public R visitWordNode(WordNode node, P p) {
        return defaultAction(node, p);
    }

}
