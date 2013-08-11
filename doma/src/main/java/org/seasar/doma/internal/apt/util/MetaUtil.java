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
package org.seasar.doma.internal.apt.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class MetaUtil {

    public static String getMetaTypeName(String typeName) {
        assertNotNull(typeName);
        String qualifiedName = typeName;
        String suffix = "";
        int pos = typeName.indexOf('<');
        if (pos == -1) {
            qualifiedName = typeName;
        } else {
            qualifiedName = typeName.substring(0, pos);
            suffix = typeName.substring(pos);
        }
        String packageName = ClassUtil.getPackageName(qualifiedName);
        String simpleName = ClassUtil.getSimpleName(qualifiedName);
        String base = "";
        if (packageName != null && packageName.length() > 0) {
            base = packageName + ".";
        }
        return base + Constants.METATYPE_PREFIX + simpleName + suffix;
    }

    public static String getSimpleMetaTypeName(String typeName) {
        assertNotNull(typeName);
        String qualifiedName = typeName;
        String suffix = "";
        int pos = typeName.indexOf('<');
        if (pos == -1) {
            qualifiedName = typeName;
        } else {
            qualifiedName = typeName.substring(0, pos);
            suffix = typeName.substring(pos);
        }
        String simpleName = ClassUtil.getSimpleName(qualifiedName);
        return Constants.METATYPE_PREFIX + simpleName + suffix;
    }
}
