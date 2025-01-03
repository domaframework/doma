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
package org.seasar.doma.internal.jdbc.util;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Function;

public final class DatabaseObjectUtil {

  public static String getQualifiedName(
      Function<String, String> quoteFunction,
      String catalogName,
      String schemaName,
      String simpleName) {
    assertNotNull(quoteFunction, simpleName);
    StringBuilder buf = new StringBuilder();
    if (catalogName != null && !catalogName.isEmpty()) {
      buf.append(quoteFunction.apply(catalogName)).append(".");
    }
    if (schemaName != null && !schemaName.isEmpty()) {
      buf.append(quoteFunction.apply(schemaName)).append(".");
    }
    return buf.append(quoteFunction.apply(simpleName)).toString();
  }
}
