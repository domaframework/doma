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
package org.seasar.doma.jdbc;

import java.util.List;
import org.seasar.doma.DomaNullPointerException;

/**
 * Represents a node in the parsed SQL statement tree structure.
 *
 * <p>SQL statements in Doma are parsed into a tree of SqlNode objects that can be
 * traversed and processed using the visitor pattern. This interface defines the
 * common behavior for all types of SQL nodes.
 *
 * <p>The implementation class is not required to be thread safe.
 *
 * <p>Treat this object as read-only when you cannot control its life cycle.
 *
 * @see SqlNodeVisitor
 */
public interface SqlNode {

  /**
   * Returns the children list.
   *
   * @return the children list
   */
  List<SqlNode> getChildren();

  /**
   * Accepts the visitor.
   *
   * @param <R> the result type
   * @param <P> the parameter type
   * @param visitor the visitor
   * @param p the parameter for the visitor
   * @return the result
   * @throws DomaNullPointerException if {@code visitor} is {@code null}
   */
  <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p);
}
