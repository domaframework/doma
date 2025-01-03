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
package org.seasar.doma.it.criteria;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.seasar.doma.Domain;

@Domain(valueType = String.class)
public class Names implements Iterable<String> {
  private final List<String> value;

  public Names(String value) {
    this.value = Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
  }

  public String getValue() {
    return String.join(",", value);
  }

  @Override
  public Iterator<String> iterator() {
    return value.iterator();
  }
}
