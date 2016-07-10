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
package org.seasar.doma.internal;

import static java.util.stream.Collectors.joining;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;

import org.seasar.doma.internal.util.ClassUtil;

/**
 * @author nakamura-to
 *
 */
public class Conventions {

    public static String normalizeBinaryName(String binaryName) {
        assertNotNull(binaryName);
        String packageName = ClassUtil.getPackageName(binaryName);
        List<String> enclosingNames = ClassUtil.getEnclosingNames(binaryName);
        String simpleName = ClassUtil.getSimpleName(binaryName);
        String base = "";
        if (packageName != null && packageName.length() > 0) {
            base = packageName + ".";
        }
        return base
                + enclosingNames.stream()
                        .map(n -> n + Constants.METATYPE_NAME_DELIMITER)
                        .collect(joining()) + simpleName;
    }

    public static String toFullMetaName(String originalBinaryName) {
        assertNotNull(originalBinaryName);
        String binaryName = normalizeBinaryName(originalBinaryName);
        String packageName = ClassUtil.getPackageName(binaryName);
        String simpleName = ClassUtil.getSimpleName(binaryName);
        String base = "";
        if (packageName != null && packageName.length() > 0) {
            base = packageName + ".";
        }
        return base + createSimpleMetaName(simpleName);
    }

    public static String toSimpleMetaName(String originalBinaryName) {
        assertNotNull(originalBinaryName);
        String binaryName = normalizeBinaryName(originalBinaryName);
        String simpleName = ClassUtil.getSimpleName(binaryName);
        return createSimpleMetaName(simpleName);
    }

    private static String createSimpleMetaName(String originalSimpleName) {
        return Constants.METATYPE_PREFIX + originalSimpleName;
    }

}
