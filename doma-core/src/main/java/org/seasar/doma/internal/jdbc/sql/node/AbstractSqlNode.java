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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNode;

public abstract class AbstractSqlNode implements AppendableSqlNode {

  protected final List<SqlNode> children = new ArrayList<>();

  @Override
  public void appendNode(SqlNode child) {
    if (child == null) {
      throw new DomaNullPointerException("child");
    }
    children.add(child);
  }

  @Override
  public List<SqlNode> getChildren() {
    return Collections.unmodifiableList(children);
  }
}
