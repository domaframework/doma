package org.seasar.doma.internal;

import java.nio.charset.Charset;

/**
 * @author taedium
 * 
 */
public final class Constants {

    public static final String ROWNUMBER_COLUMN_NAME = "doma_rownumber_";

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static final String SQL_PATH_PREFIX = "META-INF/";

    public static final String SQL_PATH_SUFFIX = ".sql";

    public static final String SCRIPT_PATH_PREFIX = "META-INF/";

    public static final String SCRIPT_PATH_SUFFIX = ".script";

    public static final String DESC_PREFIX = "_";

    public static final String EXTERNAL_HOLDER_DESC_ROOT_PACKAGE = "__";

    public static final String DESC_NAME_DELIMITER = "__";

    public static final String BINARY_NAME_DELIMITER = "$";

    public static final String RESERVED_VARIABLE_NAME_PREFIX = "__";

}
