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

import static org.seasar.doma.internal.Constants.*;

import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseNode;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class OraclePagingTransformer extends StandardPagingTransformer {

    public OraclePagingTransformer(long offset, long limit) {
        super(offset, limit);
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
        subStatement.setOrderByClauseNode(node.getOrderByClauseNode());

        SelectClauseNode select = new SelectClauseNode("select");
        select.addNode(new FragmentNode(" * "));
        FromClauseNode from = new FromClauseNode("from");
        from.addNode(new FragmentNode(" ( select temp_.*, rownum "
                + ROWNUMBER_COLUMN_NAME + " from ( "));
        from.addNode(subStatement);
        from.addNode(new FragmentNode(" ) temp_ ) "));
        WhereClauseNode where = new WhereClauseNode("where");
        where.addNode(new FragmentNode(" "));
        if (offset >= 0) {
            where.addNode(new FragmentNode(ROWNUMBER_COLUMN_NAME + " > "));
            where.addNode(new FragmentNode(String.valueOf(offset)));
        }
        if (limit > 0) {
            if (offset >= 0) {
                where.addNode(new FragmentNode(" and "));
            }
            where.addNode(new FragmentNode(ROWNUMBER_COLUMN_NAME + " <= "));
            long bias = offset < 0 ? 0 : offset;
            where.addNode(new FragmentNode(String.valueOf(bias + limit)));
        }
        ForUpdateClauseNode forUpdate = node.getForUpdateClauseNode();
        if (forUpdate != null) {
            where.addNode(new FragmentNode(" "));
        }

        SelectStatementNode result = new SelectStatementNode();
        result.setSelectClauseNode(select);
        result.setFromClauseNode(from);
        result.setWhereClauseNode(where);
        result.setForUpdateClauseNode(forUpdate);
        return result;
    }
}
