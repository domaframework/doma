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
package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;

@Entity(immutable = true)
@AllArgsConstructor
public class LombokAllArgsConstructor {

  @SuppressWarnings("unused")
  private int id;

  @SuppressWarnings("unused")
  private String name;

  public LombokAllArgsConstructor(int id, String name) {}
}
