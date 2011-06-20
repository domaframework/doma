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

import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class HsqldbPagingTransformer extends StandardPagingTransformer {

    public HsqldbPagingTransformer(long offset, long limit) {
        super(offset, limit);
    }

    @Override
    public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
        if (processed) {
            return node;
        }
        processed = true;

        OrderByClauseNode originalOrderBy = node.getOrderByClauseNode();
        OrderByClauseNode orderBy = node.getOrderByClauseNode();
        if (originalOrderBy != null) {
            orderBy = new OrderByClauseNode(originalOrderBy.getWordNode());
            for (SqlNode child : originalOrderBy.getChildren()) {
                orderBy.addNode(child);
            }
        } else {
            orderBy = new OrderByClauseNode("");
        }
        orderBy.addNode(new FragmentNode(" limit "));
        if (limit >= 0) {
            orderBy.addNode(new FragmentNode(String.valueOf(limit)));
        } else {
            orderBy.addNode(new FragmentNode("0"));
        }
        if (offset >= 0) {
            orderBy.addNode(new FragmentNode(" offset "));
            orderBy.addNode(new FragmentNode(String.valueOf(offset)));
        }
        ForUpdateClauseNode forUpdate = node.getForUpdateClauseNode();
        if (forUpdate != null) {
            orderBy.addNode(new FragmentNode(" "));
        }

        SelectStatementNode result = new SelectStatementNode();
        result.setSelectClauseNode(node.getSelectClauseNode());
        result.setFromClauseNode(node.getFromClauseNode());
        result.setWhereClauseNode(node.getWhereClauseNode());
        result.setGroupByClauseNode(node.getGroupByClauseNode());
        result.setHavingClauseNode(node.getHavingClauseNode());
        result.setOrderByClauseNode(orderBy);
        result.setForUpdateClauseNode(forUpdate);
        return result;
    }
}