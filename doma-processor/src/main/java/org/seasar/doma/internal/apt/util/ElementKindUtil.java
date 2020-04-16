package org.seasar.doma.internal.apt.util;

import java.util.Arrays;
import javax.lang.model.element.ElementKind;

public final class ElementKindUtil {

  private static final ElementKind RECORD =
      Arrays.stream(ElementKind.values())
          .filter(k -> "RECORD".equals(k.name()))
          .findAny()
          .orElse(null);

  public static boolean isRecord(ElementKind kind) {
    if (RECORD == null) {
      return false;
    }
    return RECORD == kind;
  }
}
