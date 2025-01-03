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

import java.util.Objects;
import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.dialect.MysqlDialect;

public class MysqlForUpdateTransformer extends StandardForUpdateTransformer {

  private final MysqlDialect.MySqlVersion version;

  public MysqlForUpdateTransformer(
      SelectForUpdateType forUpdateType, int waitSeconds, String... aliases) {
    this(forUpdateType, waitSeconds, aliases, MysqlDialect.DEFAULT_VERSION);
  }

  public MysqlForUpdateTransformer(
      SelectForUpdateType forUpdateType,
      int waitSeconds,
      String[] aliases,
      MysqlDialect.MySqlVersion version) {
    super(forUpdateType, waitSeconds, aliases);
    this.version = Objects.requireNonNull(version);
  }

  @Override
  public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
    if (processed) {
      return node;
    }
    processed = true;

    SelectStatementNode result = new SelectStatementNode();
    result.setSelectClauseNode(node.getSelectClauseNode());
    result.setFromClauseNode(node.getFromClauseNode());
    result.setWhereClauseNode(node.getWhereClauseNode());
    result.setGroupByClauseNode(node.getGroupByClauseNode());
    result.setHavingClauseNode(node.getHavingClauseNode());
    result.setOrderByClauseNode(node.getOrderByClauseNode());
    result.setForUpdateClauseNode(createForUpdateClauseNode());
    result.setOptionClauseNode(node.getOptionClauseNode());
    return result;
  }

  protected ForUpdateClauseNode createForUpdateClauseNode() {
    ForUpdateClauseNode forUpdate = new ForUpdateClauseNode("for update");
    switch (version) {
      case V5:
        break;
      case V8:
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
        forUpdate.appendNode(new FragmentNode(buf.toString()));
        break;
    }
    return forUpdate;
  }
}
