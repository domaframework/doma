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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class ParensNode extends AbstractSqlNode {

  protected final SqlLocation location;

  protected boolean attachedWithValue;

  protected boolean empty = true;

  protected final OtherNode openedParensNode;

  protected OtherNode closedParensNode;

  public ParensNode(SqlLocation location) {
    assertNotNull(location);
    this.location = location;
    openedParensNode = OtherNode.of("(");
  }

  public boolean isAttachedWithValue() {
    return attachedWithValue;
  }

  public void setAttachedWithValue(boolean attachedWithValue) {
    this.attachedWithValue = attachedWithValue;
  }

  public boolean isEmpty() {
    return empty;
  }

  public void setEmpty(boolean empty) {
    this.empty = empty;
  }

  public OtherNode getOpenedFragmentNode() {
    return openedParensNode;
  }

  public OtherNode getClosedFragmentNode() {
    return closedParensNode;
  }

  public SqlLocation getLocation() {
    return location;
  }

  public void close() {
    closedParensNode = OtherNode.of(")");
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitParensNode(this, p);
  }
}
