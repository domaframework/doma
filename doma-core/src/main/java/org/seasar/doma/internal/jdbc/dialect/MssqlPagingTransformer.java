/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

public class MssqlPagingTransformer extends Mssql2008PagingTransformer {

  private final boolean forceOffsetFetch;

  public MssqlPagingTransformer(long offset, long limit, boolean forceOffsetFetch) {
    super(offset, limit);
    this.forceOffsetFetch = forceOffsetFetch;
  }

  @Override
  public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
    if (processed) {
      return node;
    }
    processed = true;

    if (!forceOffsetFetch && offset <= 0) {
      return super.appendTopNode(node);
    }

    OrderByClauseNode originalOrderBy = node.getOrderByClauseNode();
    if (originalOrderBy == null) {
      throw new JdbcException(Message.DOMA2201);
    }

    OrderByClauseNode orderBy = new OrderByClauseNode(originalOrderBy.getWordNode());

    for (SqlNode child : originalOrderBy.getChildren()) {
      orderBy.appendNode(child);
    }

    String offset = this.offset <= 0 ? "0" : String.valueOf(this.offset);

    orderBy.appendNode(new FragmentNode(" offset "));
    orderBy.appendNode(new FragmentNode(offset));
    orderBy.appendNode(new FragmentNode(" rows"));
    if (this.limit > 0) {
      orderBy.appendNode(new FragmentNode(" fetch next "));
      orderBy.appendNode(new FragmentNode(String.valueOf(this.limit)));
      orderBy.appendNode(new FragmentNode(" rows only"));
    }

    SelectStatementNode result = new SelectStatementNode();
    result.setSelectClauseNode(node.getSelectClauseNode());
    result.setFromClauseNode(node.getFromClauseNode());
    result.setWhereClauseNode(node.getWhereClauseNode());
    result.setGroupByClauseNode(node.getGroupByClauseNode());
    result.setHavingClauseNode(node.getHavingClauseNode());
    result.setOrderByClauseNode(orderBy);
    result.setForUpdateClauseNode(node.getForUpdateClauseNode());
    result.setOptionClauseNode(node.getOptionClauseNode());
    return result;
  }
}
