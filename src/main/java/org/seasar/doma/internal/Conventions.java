package org.seasar.doma.internal;

import static java.util.stream.Collectors.joining;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.util.ClassUtil;

public class Conventions {

  public static String createDescClassName(CharSequence originalBinaryName) {
    return toFullDescClassName(originalBinaryName);
  }

  public static String createExternalDescClassName(CharSequence originalBinaryName) {
    return Constants.EXTERNAL_HOLDER_DESC_ROOT_PACKAGE
        + "."
        + toFullDescClassName(originalBinaryName);
  }

  protected static String toFullDescClassName(CharSequence originalBinaryName) {
    assertNotNull(originalBinaryName);
    var binaryName = normalizeBinaryName(originalBinaryName.toString());
    var packageName = ClassUtil.getPackageName(binaryName);
    var simpleName = ClassUtil.getSimpleName(binaryName);
    var base = "";
    if (packageName.length() > 0) {
      base = packageName + ".";
    }
    return base + Constants.DESC_PREFIX + simpleName;
  }

  protected static String normalizeBinaryName(String binaryName) {
    assertNotNull(binaryName);
    var packageName = ClassUtil.getPackageName(binaryName);
    var enclosingNames = ClassUtil.getEnclosingNames(binaryName);
    var simpleName = ClassUtil.getSimpleName(binaryName);
    var base = "";
    if (packageName.length() > 0) {
      base = packageName + ".";
    }
    return base
        + enclosingNames.stream().map(n -> n + Constants.DESC_NAME_DELIMITER).collect(joining())
        + simpleName;
  }
}
