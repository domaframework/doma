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
package org.seasar.doma.jdbc.aggregate;

import java.util.stream.Stream;

/**
 * Provides a mechanism for reducing a stream of entities into a single result.
 *
 * @param <RESULT> the type of the result of the reduction
 * @param <ENTITY> the type of the elements in the stream to be reduced
 */
public interface StreamReducer<RESULT, ENTITY> {

  RESULT reduce(Stream<ENTITY> stream);
}
