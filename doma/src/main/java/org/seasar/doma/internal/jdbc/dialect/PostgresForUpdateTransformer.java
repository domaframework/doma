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
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class PostgresForUpdateTransformer extends StandardForUpdateTransformer {

    public PostgresForUpdateTransformer(SelectForUpdateType forUpdateType,
            int waitSeconds, String... aliases) {
        super(forUpdateType, waitSeconds, aliases);
    }

    @Override
    public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
        if (processed) {
            return node;
        }
        processed = true;

        StringBuilder buf = new StringBuilder(100);
        if (aliases.length > 0) {
            buf.append(" of ");
            for (String alias : aliases) {
                buf.append(alias);
                buf.append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        if (forUpdateType == SelectForUpdateType.NOWAIT) {
            buf.append(" nowait ");
        }
        ForUpdateClauseNode forUpdate = new ForUpdateClauseNode(" for update");
        forUpdate.addNode(new FragmentNode(buf.toString()));

        SelectStatementNode result = new SelectStatementNode();
        result.setSelectClauseNode(node.getSelectClauseNode());
        result.setFromClauseNode(node.getFromClauseNode());
        result.setWhereClauseNode(node.getWhereClauseNode());
        result.setGroupByClauseNode(node.getGroupByClauseNode());
        result.setHavingClauseNode(node.getHavingClauseNode());
        result.setOrderByClauseNode(node.getOrderByClauseNode());
        result.setForUpdateClauseNode(forUpdate);
        return result;
    }
}
