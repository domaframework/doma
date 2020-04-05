package org.seasar.doma.internal.jdbc.util;

import java.io.File;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.jdbc.dialect.Dialect;

public final class ScriptFileUtil {

  private static final String PREFIX = Constants.SCRIPT_PATH_PREFIX;

  private static final String SUFFIX = Constants.SCRIPT_PATH_SUFFIX;

  public static String buildPath(String className, String methodName) {
    return FileUtil.buildPath(PREFIX, SUFFIX, className, methodName);
  }

  public static String buildPath(String className) {
    return FileUtil.buildPath(PREFIX, SUFFIX, className);
  }

  public static boolean isScriptFile(File file, String methodName) {
    return FileUtil.isFile(PREFIX, SUFFIX, file, methodName);
  }

  public static String convertToDbmsSpecificPath(String path, Dialect dialect) {
    return FileUtil.convertToDbmsSpecificPath(PREFIX, SUFFIX, path, dialect);
  }
}
