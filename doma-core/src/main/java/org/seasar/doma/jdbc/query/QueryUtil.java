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
