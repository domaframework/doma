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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssociationEntityPool {
  private final Map<AssociationPathKey, AssociationEntityPoolEntry> entries = new HashMap<>();

  public void add(AssociationEntityPoolEntry entry) {
    Objects.requireNonNull(entry);
    entries.put(entry.pathKey(), entry);
  }

  public AssociationEntityPoolEntry get(AssociationPathKey pathKey) {
    Objects.requireNonNull(pathKey);
    return entries.get(pathKey);
  }

  public void replace(AssociationEntityPoolEntry entry) {
    Objects.requireNonNull(entry);
    Objects.requireNonNull(entry);
    entries.replace(entry.pathKey(), entry);
  }
}
