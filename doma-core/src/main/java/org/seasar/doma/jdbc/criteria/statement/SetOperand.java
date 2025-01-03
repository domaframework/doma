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
package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

/**
 * Represents a set operand.
 *
 * @param <ELEMENT> the element type of the set operation result
 */
public interface SetOperand<ELEMENT> extends StreamMappable<ELEMENT> {

  /**
   * Returns the set operation context
   *
   * @return the context
   */
  SetOperationContext<ELEMENT> getContext();

  /**
   * Creates an UNION statement
   *
   * @param other the UNION operand
   * @return the UNION operator
   */
  SetOperator<ELEMENT> union(SetOperand<ELEMENT> other);

  /**
   * Creates an UNION ALL statement
   *
   * @param other the UNION ALL operand
   * @return the UNION ALL operator
   */
  SetOperator<ELEMENT> unionAll(SetOperand<ELEMENT> other);

  @Override
  SetOperand<ELEMENT> peek(Consumer<Sql<?>> consumer);
}
