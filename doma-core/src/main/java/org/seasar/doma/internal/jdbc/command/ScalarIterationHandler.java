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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.query.SelectQuery;

public class ScalarIterationHandler<BASIC, CONTAINER, RESULT>
    extends AbstractIterationHandler<CONTAINER, RESULT> {

  protected final Supplier<Scalar<BASIC, CONTAINER>> supplier;

  public ScalarIterationHandler(
      Supplier<Scalar<BASIC, CONTAINER>> supplier,
      IterationCallback<CONTAINER, RESULT> iterationCallback) {
    super(iterationCallback);
    assertNotNull(supplier);
    this.supplier = supplier;
  }

  @Override
  protected ScalarProvider<BASIC, CONTAINER> createObjectProvider(SelectQuery query) {
    return new ScalarProvider<>(supplier, query);
  }
}
