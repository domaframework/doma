package org.seasar.doma.internal.jdbc.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.File;
import org.seasar.doma.jdbc.dialect.Dialect;

final class FileUtil {

  static String buildPath(String prefix, String suffix, String className, String methodName) {
    assertNotNull(prefix, suffix, className, methodName);
    String path = buildPath(prefix, suffix, className);
    path += "/" + methodName + suffix;
    return path;
  }

  static String buildPath(String prefix, String suffix, String className) {
    assertNotNull(prefix, suffix, className);
    int pos = className.lastIndexOf(".");
    String packageName = pos > 0 ? className.substring(0, pos) : null;
    String simpleName = pos > 0 ? className.substring(pos + 1) : className;
    String path = prefix;
    if (pos > 0) {
      path += packageName.replace(".", "/") + "/";
    }
    path += simpleName;
    return path;
  }

  static boolean isFile(String prefix, String suffix, File file, String methodName) {
    assertNotNull(prefix, suffix, file, methodName);
    if (!file.isFile()) {
      return false;
    }
    String fileName = file.getName();
    return fileName.equals(methodName + suffix)
        || fileName.startsWith(methodName + "-") && fileName.endsWith(suffix);
  }

  static String convertToDbmsSpecificPath(
      String prefix, String suffix, String path, Dialect dialect) {
    assertNotNull(prefix, suffix, path, dialect);
    String name = dialect.getName();
    return path.substring(0, path.length() - suffix.length()) + "-" + name + suffix;
  }
}
