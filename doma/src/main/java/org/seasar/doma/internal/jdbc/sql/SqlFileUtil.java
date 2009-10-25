/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.File;

/**
 * @author taedium
 * 
 */
public final class SqlFileUtil {

    public static String buildPath(String className, String methodName) {
        assertNotNull(className, methodName);
        String path = buildPath(className);
        path += "/" + methodName + ".sql";
        return path;
    }

    public static String buildPath(String className) {
        assertNotNull(className);
        int pos = className.lastIndexOf(".");
        String packageName = pos > 0 ? className.substring(0, pos) : null;
        String simpleName = pos > 0 ? className.substring(pos + 1) : className;
        String path = "META-INF";
        if (pos > 0) {
            path += "/" + packageName.replace(".", "/");
        }
        path += "/" + simpleName;
        return path;
    }

    public static boolean isSqlFile(File file, String methodName) {
        if (!file.isFile()) {
            return false;
        }
        String fileName = file.getName();
        return fileName.equals(methodName + ".sql")
                || fileName.startsWith(methodName + "-")
                && fileName.endsWith(".sql");
    }

}
