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
package org.seasar.doma.internal;

import java.util.Objects;
import org.seasar.doma.internal.util.ClassUtil;

public class ClassName implements CharSequence {

  private final String qualifiedName;

  public ClassName(String qualifiedName) {
    this.qualifiedName = Objects.requireNonNull(qualifiedName);
  }

  public String getPackageName() {
    return ClassUtil.getPackageName(qualifiedName);
  }

  public String getSimpleName() {
    return ClassUtil.getSimpleName(qualifiedName);
  }

  @Override
  public int length() {
    return qualifiedName.length();
  }

  @Override
  public char charAt(int index) {
    return qualifiedName.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return qualifiedName.subSequence(start, end);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public String toString() {
    return qualifiedName;
  }
}
