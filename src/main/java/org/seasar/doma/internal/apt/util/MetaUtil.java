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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.Conventions;

/**
 * @author taedium
 * 
 */
public class MetaUtil {

    public static String toFullMetaName(TypeElement originalTypeElement,
            ProcessingEnvironment env) {
        assertNotNull(originalTypeElement, env);
        String binaryName = ElementUtil.getBinaryName(originalTypeElement, env);
        return Conventions.toFullMetaName(binaryName);
    }

    public static String toFullMetaName(String originalBinaryName) {
        assertNotNull(originalBinaryName);
        return Conventions.toFullMetaName(originalBinaryName);
    }

    public static String toSimpleMetaName(TypeElement originalTypeElement,
            ProcessingEnvironment env) {
        assertNotNull(originalTypeElement, env);
        return Conventions.toSimpleMetaName(ElementUtil.getBinaryName(
                originalTypeElement, env));
    }

    public static String toSimpleMetaName(String originalBinaryName) {
        assertNotNull(originalBinaryName);
        return Conventions.toSimpleMetaName(originalBinaryName);
    }

}
