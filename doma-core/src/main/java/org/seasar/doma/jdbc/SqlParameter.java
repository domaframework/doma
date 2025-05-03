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

/**
 * Represents a parameter used in SQL statements.
 *
 * <p>This interface defines the common behavior for parameters that can be
 * bound to prepared statements during SQL execution.
 *
 * @see Sql
 */
public interface SqlParameter {

  /**
   * Returns the parameter value.
   *
   * @return the parameter value
   */
  Object getValue();

  /**
   * Accepts the visitor.
   *
   * @param <R> the result type
   * @param <P> the parameter type
   * @param <TH> the throwable type
   * @param visitor the visitor
   * @param p the parameter
   * @return the result
   * @throws TH the exception
   */
  <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p) throws TH;
}
