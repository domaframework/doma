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

import java.util.OptionalLong;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;

public class OptionalLongStreamHandler<RESULT>
    extends ScalarStreamHandler<Long, OptionalLong, RESULT> {

  public OptionalLongStreamHandler(Function<Stream<OptionalLong>, RESULT> mapper) {
    super(OptionalLongScalar::new, mapper);
  }
}
