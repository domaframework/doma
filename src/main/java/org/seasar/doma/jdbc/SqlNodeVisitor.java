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
package org.seasar.doma.jdbc;

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

/**
 * {@link SqlNode} へのビジターです。
 * <p>
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * 
 * @author taedium
 * 
 * @param <R>
 *            戻り値の型
 * @param <P>
 *            パラメータの型
 */
public interface SqlNodeVisitor<R, P> {

    R visitAnonymousNode(AnonymousNode node, P p);

    R visitBindVariableNode(BindVariableNode node, P p);

    R visitCommentNode(CommentNode node, P p);

    R visitElseifNode(ElseifNode node, P p);

    R visitElseNode(ElseNode node, P p);

    R visitEmbeddedVariableNode(EmbeddedVariableNode node, P p);

    R visitEndNode(EndNode node, P p);

    R visitEolNode(EolNode node, P p);

    R visitExpandNode(ExpandNode node, P p);

    R visitForBlockNode(ForBlockNode node, P p);

    R visitForNode(ForNode node, P p);

    R visitForUpdateClauseNode(ForUpdateClauseNode node, P p);

    R visitFragmentNode(FragmentNode node, P p);

    R visitFromClauseNode(FromClauseNode node, P p);

    R visitGroupByClauseNode(GroupByClauseNode node, P p);

    R visitHavingClauseNode(HavingClauseNode node, P p);

    R visitIfBlockNode(IfBlockNode node, P p);

    R visitIfNode(IfNode node, P p);

    R visitLiteralVariableNode(LiteralVariableNode node, P p);

    R visitLogicalOperatorNode(LogicalOperatorNode node, P p);

    R visitOptionClauseNode(OptionClauseNode node, P p);

    R visitOrderByClauseNode(OrderByClauseNode node, P p);

    R visitOtherNode(OtherNode node, P p);

    R visitParensNode(ParensNode node, P p);

    R visitPopulateNode(PopulateNode node, P p);

    R visitSelectClauseNode(SelectClauseNode node, P p);

    R visitSelectStatementNode(SelectStatementNode node, P p);

    R visitSetClauseNode(SetClauseNode node, P p);

    R visitUpdateClauseNode(UpdateClauseNode node, P p);

    R visitUpdateStatementNode(UpdateStatementNode node, P p);

    R visitWhereClauseNode(WhereClauseNode node, P p);

    R visitWhitespaceNode(WhitespaceNode node, P p);

    R visitWordNode(WordNode node, P p);

}
