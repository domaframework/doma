/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.File;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author taedium
 * 
 */
public final class SqlFileUtil {

    public static String buildPath(String className, String methodName) {
        assertNotNull(className, methodName);
        String path = buildPath(className);
        path += "/" + methodName + Constants.SQL_PATH_SUFFIX;
        return path;
    }

    public static String buildPath(String className) {
        assertNotNull(className);
        int pos = className.lastIndexOf(".");
        String packageName = pos > 0 ? className.substring(0, pos) : null;
        String simpleName = pos > 0 ? className.substring(pos + 1) : className;
        String path = Constants.SQL_PATH_PREFIX;
        if (pos > 0) {
            path += packageName.replace(".", "/") + "/";
        }
        path += simpleName;
        return path;
    }

    public static boolean isSqlFile(File file, String methodName) {
        if (!file.isFile()) {
            return false;
        }
        String fileName = file.getName();
        return fileName.equals(methodName + Constants.SQL_PATH_SUFFIX)
                || fileName.startsWith(methodName + "-")
                && fileName.endsWith(Constants.SQL_PATH_SUFFIX);
    }

    public static String getDbmsSpecificPath(String path, Dialect dialect) {
        String name = dialect.getName();
        return path.substring(0, path.length()
                - Constants.SQL_PATH_SUFFIX.length())
                + "-" + name + Constants.SQL_PATH_SUFFIX;
    }

}
