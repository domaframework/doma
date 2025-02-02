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
import java.util.TreeMap;

public class ColumnNameMapFormatter {
  public static String format(Map<String, MappingSupport.PropType> columnNameMap) {
    StringBuilder buf = new StringBuilder();
    buf.append("------------------------------------------------------\n");
    buf.append("Lowercase Column Name -> Property Name (Entity Name)\n");
    buf.append("------------------------------------------------------\n");
    TreeMap<String, MappingSupport.PropType> sortedMap = new TreeMap<>(columnNameMap);
    for (Map.Entry<String, MappingSupport.PropType> entry : sortedMap.entrySet()) {
      String columnName = entry.getKey();
      MappingSupport.PropType propType = entry.getValue();
      buf.append(columnName);
      buf.append(" -> ");
      buf.append(propType.name());
      buf.append(" (");
      buf.append(propType.entityType().getName());
      buf.append(")\n");
    }
    buf.append("------------------------------------------------------");
    return buf.toString();
  }
}
