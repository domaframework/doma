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

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.jdbc.query.SelectQuery;

public class MapStreamHandler<RESULT> extends AbstractStreamHandler<Map<String, Object>, RESULT> {

  private final MapKeyNamingType mapKeyNamingType;

  public MapStreamHandler(
      MapKeyNamingType keyNamingType, Function<Stream<Map<String, Object>>, RESULT> mapper) {
    super(mapper);
    assertNotNull(keyNamingType);
    this.mapKeyNamingType = keyNamingType;
  }

  @Override
  protected MapProvider createObjectProvider(SelectQuery query) {
    return new MapProvider(query, mapKeyNamingType);
  }
}
