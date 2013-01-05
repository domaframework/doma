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
package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author taedium
 * @since 1.27.1
 */
public final class GenericsUtil {

    public static Class<?> inferTypeArgument(Class<?> clazz,
            TypeVariable<?> typeVariable) {
        assertNotNull(clazz, typeVariable);

        TypeArgumentInferrer inferrer = new TypeArgumentInferrer(clazz,
                typeVariable);
        Type arg = inferrer.infer();
        if (arg instanceof Class) {
            return (Class<?>) arg;
        }
        return null;
    }

    protected static class TypeArgumentInferrer {

        protected final Class<?> clazz;

        protected final GenericDeclaration genericDeclaration;

        protected final int index;

        public TypeArgumentInferrer(Class<?> clazz, TypeVariable<?> typeVariable) {
            this.clazz = clazz;
            this.genericDeclaration = typeVariable.getGenericDeclaration();
            this.index = getTypeParameterIndex(genericDeclaration, typeVariable);
        }

        private int getTypeParameterIndex(
                GenericDeclaration genericDeclaration,
                TypeVariable<?> typeVariable) {
            Type[] types = genericDeclaration.getTypeParameters();
            for (int i = 0, len = types.length; i < len; i++) {
                if (types[i] == typeVariable) {
                    return i;
                }
            }
            return 0;
        }

        public Type infer() {
            Class<?> superclass = clazz.getSuperclass();
            Type superclassType = clazz.getGenericSuperclass();
            if (superclass != null) {
                Type arg = getTypeArgumentRecursive(superclass, superclassType);
                if (arg != null) {
                    return arg;
                }
            }

            Class<?>[] interfaces = clazz.getInterfaces();
            Type[] interfaceTypes = clazz.getGenericInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                Type arg = getTypeArgumentRecursive(interfaces[i],
                        interfaceTypes[i]);
                if (arg != null) {
                    return arg;
                }
            }

            return null;
        }

        protected Type getTypeArgumentRecursive(Class<?> clazz, Type type) {
            if (clazz == null) {
                return null;
            }

            Type arg = getTypeArgument(clazz, type);
            if (arg != null) {
                return arg;
            }

            Class<?> superclass = clazz.getSuperclass();
            Type superclassType = clazz.getGenericSuperclass();
            if (superclass != null) {
                arg = getTypeArgumentRecursive(superclass, superclassType);
                if (arg != null) {
                    return arg;
                }
            }

            Class<?>[] interfaces = clazz.getInterfaces();
            Type[] interfaceTypes = clazz.getGenericInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                arg = getTypeArgumentRecursive(interfaces[i], interfaceTypes[i]);
                if (arg != null) {
                    return arg;
                }
            }

            return null;
        }

        protected Type getTypeArgument(Class<?> clazz, Type type) {
            if (genericDeclaration == clazz
                    && type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] args = parameterizedType.getActualTypeArguments();
                if (index < args.length) {
                    return args[index];
                }
            }
            return null;
        }

    }
}
