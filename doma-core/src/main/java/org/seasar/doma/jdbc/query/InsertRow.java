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
package org.seasar.doma.jdbc.query;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Represents a row to be inserted in an INSERT statement.
 *
 * <p>This class holds a list of values for a single row in a batch or multi-row insert operation.
 * It implements {@link Iterable} to allow iteration over the values in the row.
 */
public class InsertRow implements Iterable<QueryOperand> {
  /** The values for this insert row. */
  private final List<QueryOperand> values;

  /**
   * Constructs a new {@code InsertRow} with the specified values.
   *
   * @param values the values for this insert row
   */
  public InsertRow(List<QueryOperand> values) {
    this.values = Objects.requireNonNull(values);
  }

  /** {@inheritDoc} */
  @Override
  public Iterator<QueryOperand> iterator() {
    return values.iterator();
  }
}
