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
package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class ForBlockNode extends AbstractSqlNode implements BlockNode {

  public static final String HAS_NEXT_SUFFIX = "_has_next";

  public static final String INDEX_SUFFIX = "_index";

  protected ForNode forNode;

  protected EndNode endNode;

  public ForBlockNode() {}

  public void setForNode(ForNode forNode) {
    this.forNode = forNode;
    appendNodeInternal(forNode);
  }

  public ForNode getForNode() {
    return forNode;
  }

  @Override
  public void setEndNode(EndNode endNode) {
    this.endNode = endNode;
    appendNodeInternal(endNode);
  }

  public EndNode getEndNode() {
    return endNode;
  }

  @Override
  public void appendNode(SqlNode child) {
    throw new JdbcUnsupportedOperationException(getClass().getName(), "addNode");
  }

  protected void appendNodeInternal(SqlNode child) {
    if (child != null) {
      super.appendNode(child);
    }
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitForBlockNode(this, p);
  }
}
