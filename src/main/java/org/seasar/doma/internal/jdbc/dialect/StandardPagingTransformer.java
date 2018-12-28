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

import static org.seasar.doma.internal.Constants.ROWNUMBER_COLUMN_NAME;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import org.seasar.doma.internal.jdbc.sql.SimpleSqlNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.WordNode;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

/** @author taedium */
public class StandardPagingTransformer extends SimpleSqlNodeVisitor<SqlNode, Void> {

  protected final long offset;

  protected final long limit;

  protected boolean processed;

  public StandardPagingTransformer(long offset, long limit) {
    assertTrue(offset >= 0 || limit >= 0);
    this.offset = offset;
    this.limit = limit;
  }

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

    OrderByClauseNode originalOrderBy = node.getOrderByClauseNode();
    if (originalOrderBy == null) {
      throw new JdbcException(Message.DOMA2201);
    }
    SelectStatementNode subStatement = new SelectStatementNode();
    subStatement.setSelectClauseNode(node.getSelectClauseNode());
    subStatement.setFromClauseNode(node.getFromClauseNode());
    subStatement.setWhereClauseNode(node.getWhereClauseNode());
    subStatement.setGroupByClauseNode(node.getGroupByClauseNode());
    subStatement.setHavingClauseNode(node.getHavingClauseNode());

    OrderByClauseNode orderBy = new OrderByClauseNode(originalOrderBy.getWordNode());
    for (SqlNode child : originalOrderBy.getChildren()) {
      if (child instanceof WordNode) {
        WordNode wordNode = (WordNode) child;
        String word = wordNode.getWord();
        String[] names = word.split("\\.");
        if (names.length == 2) {
          orderBy.appendNode(new WordNode("temp_." + names[1]));
        } else {
          orderBy.appendNode(child);
        }
      } else {
        orderBy.appendNode(child);
      }
    }

    SelectClauseNode select = new SelectClauseNode("select");
    select.appendNode(new FragmentNode(" * "));
    FromClauseNode from = new FromClauseNode("from");
    from.appendNode(new FragmentNode(" ( select temp_.*, row_number() over( "));
    from.appendNode(orderBy);
    from.appendNode(new FragmentNode(" ) as " + ROWNUMBER_COLUMN_NAME + " from ( "));
    from.appendNode(subStatement);
    from.appendNode(new FragmentNode(") as temp_ ) as temp2_ "));
    WhereClauseNode where = new WhereClauseNode("where");
    where.appendNode(new FragmentNode(" "));
    if (offset >= 0) {
      where.appendNode(new FragmentNode(ROWNUMBER_COLUMN_NAME + " > "));
      where.appendNode(new FragmentNode(String.valueOf(offset)));
    }
    if (limit > 0) {
      if (offset >= 0) {
        where.appendNode(new FragmentNode(" and "));
      }
      long bias = offset < 0 ? 0 : offset;
      where.appendNode(new FragmentNode(ROWNUMBER_COLUMN_NAME + " <= "));
      where.appendNode(new FragmentNode(String.valueOf(bias + limit)));
    }

    SelectStatementNode result = new SelectStatementNode();
    result.setSelectClauseNode(select);
    result.setFromClauseNode(from);
    result.setWhereClauseNode(where);
    result.setForUpdateClauseNode(node.getForUpdateClauseNode());
    result.setOptionClauseNode(node.getOptionClauseNode());
    return result;
  }

  @Override
  protected SqlNode defaultAction(SqlNode node, Void p) {
    return node;
  }
}
