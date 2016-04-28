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
package org.seasar.aptina.commons.util;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static java.util.Collections.*;

import static org.seasar.aptina.commons.util.AssertionUtils.*;
import static org.seasar.aptina.commons.util.CollectionUtils.*;
import static org.seasar.aptina.commons.util.ElementUtils.*;

/*
 * {@link TypeMirror} のインスタンスを手に入れる都合により，このクラスのテストケースは Aptina Unit に存在します．
 */
/**
 * {@link TypeMirror} を扱うユーティリティです．
 * 
 * @author koichik
 */
public class TypeMirrorUtils {

    /** プリミティブ型の名前と {@link TypeKind} のマップです． */
    public static final Map<String, TypeKind> PRIMITIVE_TYPES;
    static {
        final Map<String, TypeKind> map = newHashMap();
        map.put(void.class.getName(), TypeKind.VOID);
        map.put(boolean.class.getName(), TypeKind.BOOLEAN);
        map.put(char.class.getName(), TypeKind.CHAR);
        map.put(byte.class.getName(), TypeKind.BYTE);
        map.put(short.class.getName(), TypeKind.SHORT);
        map.put(int.class.getName(), TypeKind.INT);
        map.put(long.class.getName(), TypeKind.LONG);
        map.put(float.class.getName(), TypeKind.FLOAT);
        map.put(double.class.getName(), TypeKind.DOUBLE);
        PRIMITIVE_TYPES = unmodifiableMap(map);
    }

    private TypeMirrorUtils() {
    }

    /**
     * クラスに対応する {@link TypeMirror} を返します．
     * 
     * @param typeUtils
     *            {@link Types}
     * @param elementUtils
     *            {@link Elements}
     * @param clazz
     *            クラス
     * @return クラスに対応する{@link TypeMirror}， クラスが存在しない場合は {@literal null}
     */
    public static TypeMirror getTypeMirror(final Types typeUtils,
            final Elements elementUtils, final Class<?> clazz) {
        assertNotNull("typeUtils", typeUtils);
        assertNotNull("elementUtils", elementUtils);
        assertNotNull("clazz", clazz);
        if (clazz.isArray()) {
            return toArrayType(typeUtils, getTypeMirror(
                typeUtils,
                elementUtils,
                clazz.getComponentType()));
        }
        return getTypeMirror(typeUtils, elementUtils, clazz.getName());
    }

    /**
     * クラス名に対応する {@link TypeMirror} を返します．
     * <p>
     * 配列の場合は要素型の名前の後に {@code []} を連ねる形式と， {@code [[LString;} のような
     * 形式のどちらでも指定することができます．
     * </p>
     * 
     * @param typeUtils
     *            {@link Types}
     * @param elementUtils
     *            {@link Elements}
     * @param className
     *            クラスの完全限定名
     * @return クラスに対応する{@link TypeMirror}， クラスが存在しない場合は {@code null}
     */
    public static TypeMirror getTypeMirror(final Types typeUtils,
            final Elements elementUtils, final String className) {
        assertNotNull("typeUtils", typeUtils);
        assertNotNull("elementUtils", elementUtils);
        assertNotEmpty("className", className);
        if (className.endsWith("[]")) {
            final String componentTypeName = className.substring(0, className
                .length() - 2);
            return toArrayType(typeUtils, getTypeMirror(
                typeUtils,
                elementUtils,
                componentTypeName));
        }
        if (className.startsWith("[") && className.endsWith(";")) {
            final int pos = className.indexOf("L");
            final String componentTypeName = className.substring(
                pos + 1,
                className.length() - 1);
            TypeMirror typeMirror = getTypeMirror(
                typeUtils,
                elementUtils,
                componentTypeName);
            for (int i = 0; i < pos; ++i) {
                typeMirror = toArrayType(typeUtils, typeMirror);
            }
            return typeMirror;
        }
        if (PRIMITIVE_TYPES.containsKey(className)) {
            final TypeKind typeKind = PRIMITIVE_TYPES.get(className);
            return typeUtils.getPrimitiveType(typeKind);
        }
        final TypeElement typeElement = getTypeElement(elementUtils, className);
        if (typeElement == null) {
            return null;
        }
        return typeElement.asType();
    }

    /**
     * 引数の型を要素とする配列の {@link TypeMirror} を返します．
     * 
     * @param typeUtils
     *            {@link Types}
     * @param componentTypeMirror
     *            配列の要素となる型
     * @return 引数の型を要素とする配列の {@link TypeMirror}
     * @see Types#getArrayType(TypeMirror)
     */
    public static TypeMirror toArrayType(final Types typeUtils,
            final TypeMirror componentTypeMirror) {
        if (componentTypeMirror == null) {
            return null;
        }
        return typeUtils.getArrayType(componentTypeMirror);
    }

    /**
     * クラスの配列を {@link TypeMirror} の配列に変換して返します．
     * 
     * @param typeUtils
     *            {@link Types}
     * @param elementUtils
     *            {@link Elements}
     * @param types
     *            クラスの配列
     * @return {@link TypeMirror} の配列
     * @throws IllegalArgumentException
     *             配列の要素のクラスに対応する {@link TypeMirror} が存在しない場合
     */
    public static List<TypeMirror> toTypeMirrors(final Types typeUtils,
            final Elements elementUtils, final Class<?>... types)
            throws IllegalArgumentException {
        assertNotNull("typeUtils", typeUtils);
        assertNotNull("elementUtils", elementUtils);
        assertNotNull("types", types);
        final List<TypeMirror> typeMirrors = newArrayList();
        for (final Class<?> type : types) {
            final TypeMirror typeMirror = getTypeMirror(
                typeUtils,
                elementUtils,
                type);
            if (typeMirror == null) {
                throw new IllegalArgumentException("unknown type : " + type);
            }
            typeMirrors.add(typeMirror);
        }
        return typeMirrors;
    }

    /**
     * クラス名の配列を {@link TypeMirror} の配列に変換して返します．
     * 
     * @param typeUtils
     *            {@link Types}
     * @param elementUtils
     *            {@link Elements}
     * @param typeNames
     *            クラス名の配列
     * @return {@link TypeMirror} の配列
     * @throws IllegalArgumentException
     *             配列の要素のクラスに対応する {@link TypeMirror} が存在しない場合
     */
    public static List<TypeMirror> toTypeMirrors(final Types typeUtils,
            final Elements elementUtils, final String... typeNames) {
        assertNotNull("typeUtils", typeUtils);
        assertNotNull("elementUtils", elementUtils);
        assertNotNull("typeNames", typeNames);
        final List<TypeMirror> typeMirrors = newArrayList();
        for (final String typeName : typeNames) {
            final TypeMirror typeMirror = getTypeMirror(
                typeUtils,
                elementUtils,
                typeName);
            if (typeMirror == null) {
                throw new IllegalArgumentException("unknown type : " + typeName);
            }
            typeMirrors.add(typeMirror);
        }
        return typeMirrors;
    }

    /**
     * {@link TypeMirror} の文字列表現の {@link List} を返します．
     * 
     * @param typeMirrors
     *            {@link TypeMirror} の {@link List}
     * @return {@link TypeMirror} の文字列表現の {@link List}
     */
    public static List<String> toTypeNameList(
            final List<? extends TypeMirror> typeMirrors) {
        final List<String> result = newArrayList();
        for (final TypeMirror typeMirror : typeMirrors) {
            result.add(typeMirror.toString());
        }
        return result;
    }

}
