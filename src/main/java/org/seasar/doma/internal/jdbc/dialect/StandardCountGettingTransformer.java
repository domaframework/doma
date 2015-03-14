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
package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.SimpleSqlNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class StandardCountGettingTransformer extends
        SimpleSqlNodeVisitor<SqlNode, Void> {

    protected boolean processed;

    public SqlNode transform(SqlNode sqlNode) {
        AnonymousNode result = new AnonymousNode();
        for (SqlNode child : sqlNode.getChildren()) {
            result.appendNode(child.accept(this, null));
        }
        return result;
    }

    @Override
    public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
        if (processed) {
            return node;
        }
        processed = true;

        SelectStatementNode subStatement = new SelectStatementNode();
        subStatement.setSelectClauseNode(node.getSelectClauseNode());
        subStatement.setFromClauseNode(node.getFromClauseNode());
        subStatement.setWhereClauseNode(node.getWhereClauseNode());
        subStatement.setGroupByClauseNode(node.getGroupByClauseNode());
        subStatement.setHavingClauseNode(node.getHavingClauseNode());

        SelectClauseNode select = new SelectClauseNode("select");
        select.appendNode(new FragmentNode(" count(*) "));
        FromClauseNode from = new FromClauseNode("from");
        from.appendNode(new FragmentNode(" ( "));
        from.appendNode(subStatement);
        from.appendNode(new FragmentNode(") t_"));

        SelectStatementNode result = new SelectStatementNode();
        result.setSelectClauseNode(select);
        result.setFromClauseNode(from);
        result.setOptionClauseNode(node.getOptionClauseNode());
        return result;
    }

    @Override
    public SqlNode defaultAction(SqlNode node, Void p) {
        return node;
    }

}