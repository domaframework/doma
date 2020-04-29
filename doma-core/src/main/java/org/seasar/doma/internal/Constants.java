package org.seasar.doma.internal;

import java.nio.charset.Charset;

public final class Constants {

  public static final String ROWNUMBER_COLUMN_NAME = "doma_rownumber_";

  public static final Charset UTF_8 = Charset.forName("UTF-8");

  public static final String SQL_PATH_PREFIX = "META-INF/";

  public static final String SQL_PATH_SUFFIX = ".sql";

  public static final String SCRIPT_PATH_PREFIX = "META-INF/";

  public static final String SCRIPT_PATH_SUFFIX = ".script";

  public static final String TYPE_PREFIX = "_";

  public static final String EXTERNAL_DOMAIN_TYPE_ROOT_PACKAGE = "__";

  public static final String EXTERNAL_DOMAIN_TYPE_ARRAY_SUFFIX = "__ARRAY__";

  public static final String TYPE_NAME_DELIMITER = "__";

  public static final String BINARY_NAME_DELIMITER = "$";

  public static final String RESERVED_IDENTIFIER_PREFIX = "__";
}
