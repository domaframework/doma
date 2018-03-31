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
    var buf = new StringBuilder();
    if (catalogName != null && !catalogName.isEmpty()) {
      buf.append(quoteFunction.apply(catalogName)).append(".");
    }
    if (schemaName != null && !schemaName.isEmpty()) {
      buf.append(quoteFunction.apply(schemaName)).append(".");
    }
    return buf.append(quoteFunction.apply(simpleName)).toString();
  }
}
