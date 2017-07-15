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
package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.util.ClassUtil;

/**
 * @author nakamura
 *
 */
public class CodeSpec implements CharSequence {

    private final String qualifiedName;

    private final String packageName;

    private final String simpleName;

    private final String typeParamsName;

    private final CodeSpec parent;

    public CodeSpec(String qualifiedName, String typeParamsName) {
        this(qualifiedName, typeParamsName, null);
    }

    public CodeSpec(String qualifiedName, String typeParamsName, CodeSpec parentCodeSpec) {
        assertNotNull(qualifiedName, typeParamsName);
        this.qualifiedName = qualifiedName;
        this.packageName = ClassUtil.getPackageName(qualifiedName);
        this.simpleName = ClassUtil.getSimpleName(qualifiedName);
        this.typeParamsName = typeParamsName;
        this.parent = parentCodeSpec;
    }

    public char charAt(int arg0) {
        return qualifiedName.charAt(arg0);
    }

    public int length() {
        return qualifiedName.length();
    }

    public CharSequence subSequence(int arg0, int arg1) {
        return qualifiedName.subSequence(arg0, arg1);
    }

    public String toString() {
        return qualifiedName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getTypeParamsName() {
        return typeParamsName;
    }

    public boolean isParameterized() {
        return !typeParamsName.isEmpty();
    }

    public CodeSpec getParent() {
        return parent;
    }
}
