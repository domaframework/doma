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

import java.util.Map;
import java.util.stream.Collector;
import org.seasar.doma.MapKeyNamingType;

public class MapCollectorHandler<RESULT>
    extends AbstractCollectorHandler<Map<String, Object>, RESULT> {

  public MapCollectorHandler(
      MapKeyNamingType mapKeyNamingType, Collector<Map<String, Object>, ?, RESULT> collector) {
    super(new MapStreamHandler<>(mapKeyNamingType, s -> s.collect(collector)));
  }
}
