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

import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;

final class CodeHelper {

    static String box(String name) {
        if (boolean.class.getName().equals(name)) {
            return Boolean.class.getName();
        }
        if (char.class.getName().equals(name)) {
            return Character.class.getName();
        }
        if (byte.class.getName().equals(name)) {
            return Byte.class.getName();
        }
        if (short.class.getName().equals(name)) {
            return Short.class.getName();
        }
        if (int.class.getName().equals(name)) {
            return Integer.class.getName();
        }
        if (long.class.getName().equals(name)) {
            return Long.class.getName();
        }
        if (float.class.getName().equals(name)) {
            return Float.class.getName();
        }
        if (double.class.getName().equals(name)) {
            return Double.class.getName();
        }
        return name;
    }

    static String wrapperSupplier(BasicCtType ctType) {
        if (ctType.isEnum()) {
            return String.format("() -> new %1$s(%2$s.class)", ctType.getWrapperTypeName(),
                    ctType.getQualifiedName());
        }
        return String.format("%1$s::new", ctType.getWrapperTypeName());
    }

    static String entityDesc(EntityCtType ctType) {
        return ctType.getDescClassName() + ".getSingletonInternal()";
    }

    static String embeddableDesc(EmbeddableCtType ctType) {
        return ctType.getDescClassName() + ".getSingletonInternal()";
    }

}
