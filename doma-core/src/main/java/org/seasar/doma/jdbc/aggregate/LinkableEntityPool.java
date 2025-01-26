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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LinkableEntityPool {
  private final Map<LinkableEntityKey, LinkableEntityData> keyDataMap = new LinkedHashMap<>();

  public void put(LinkableEntityKey key, LinkableEntityData data) {
    keyDataMap.put(key, data);
  }

  public Set<Map.Entry<LinkableEntityKey, LinkableEntityData>> entrySet() {
    return keyDataMap.entrySet();
  }
}
