package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public final class SqlFiles {

    public static String buildPath(String className, String methodName) {
        assertNotNull(className, methodName);
        int pos = className.lastIndexOf(".");
        String packageName = pos > 0 ? className.substring(0, pos) : null;
        String simpleName = pos > 0 ? className.substring(pos + 1) : className;
        String path = "META-INF";
        if (pos > 0) {
            path += "/" + packageName.replace(".", "/");
        }
        path += "/" + simpleName + "/" + methodName + ".sql";
        return path;
    }

}
