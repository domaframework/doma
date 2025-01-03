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
package org.seasar.doma.jdbc.query;

import java.util.Arrays;
import org.seasar.doma.GenerationType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;

final class QueryUtil {

  static boolean isIdentityKeyIncludedInDuplicateKeys(
      GeneratedIdPropertyType<?, ?, ?> generatedIdPropertyType, String[] duplicateKeyNames) {
    if (generatedIdPropertyType == null) {
      return false;
    }
    if (generatedIdPropertyType.getGenerationType() != GenerationType.IDENTITY) {
      return false;
    }
    if (duplicateKeyNames.length == 0) {
      return true;
    }
    return Arrays.stream(duplicateKeyNames)
        .anyMatch(name -> name.equals(generatedIdPropertyType.getName()));
  }
}
