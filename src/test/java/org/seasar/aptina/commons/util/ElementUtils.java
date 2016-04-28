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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

import static org.seasar.aptina.commons.util.AssertionUtils.*;
import static org.seasar.aptina.commons.util.ClassUtils.*;
import static org.seasar.aptina.commons.util.CollectionUtils.*;
import static org.seasar.aptina.commons.util.StringUtils.*;
import static org.seasar.aptina.commons.util.TypeMirrorUtils.*;

/*
 * {@link Element} のインスタンスを手に入れる都合により，このクラスのテストケースは Aptina Unit に存在します．
 */
/**
 * {@link Element} を扱うユーティリティです．
 * 
 * @author koichik
 */
public class ElementUtils {

    private ElementUtils() {
    }

    /**
     * クラスに対応する {@link TypeElement} を返します．
     * <p>
     * コンパイルのコンテキスト外でこのメソッドが返す {@link TypeElement} およびその
     * {@link Element#getEnclosedElements()} が返す {@link Element} から，
     * {@link Elements#getDocComment(Element)} を使って Javadoc コメントを取得することはできません．
     * </p>
     * 
     * @param elementUtils
     *            {@link Elements}
     * @param clazz
     *            クラス
     * @return クラスに対応する{@link TypeElement}， 存在しない場合は {@literal null}
     */
    public static TypeElement getTypeElement(final Elements elementUtils,
            final Class<?> clazz) {
        assertNotNull("elements", elementUtils);
        assertNotNull("clazz", clazz);
        return getTypeElement(elementUtils, clazz.getName());
    }

    /**
     * クラス名に対応する {@link TypeElement} を返します．
     * <p>
     * コンパイルのコンテキスト外でこのメソッドが返す {@link TypeElement} およびその
     * {@link Element#getEnclosedElements()} が返す {@link Element} から，
     * {@link Elements#getDocComment(Element)} を使って Javadoc コメントを取得することはできません．
     * </p>
     * 
     * @param elementUtils
     *            {@link Elements}
     * @param className
     *            クラスの完全限定名
     * @return クラスに対応する{@link TypeElement}， 存在しない場合は {@literal null}
     */
    public static TypeElement getTypeElement(final Elements elementUtils,
            final String className) {
        assertNotEmpty("className", className);
        try {
            // 仕様では存在しないクラスを引数に Elements#getTypeElement(String) を呼び出すと
            // null が返されるはずだが， コンパイラの実行が終わった後だと NPE がスローされる
            return elementUtils.getTypeElement(className);
        } catch (final NullPointerException e) {
            return null;
        }
    }

    /**
     * 型エレメントに定義されたフィールドの変数エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @param field
     *            フィールド
     * @return 型エレメントに定義されたフィールドの変数エレメント． 存在しない場合は {@code null}
     */
    public static VariableElement getFieldElement(
            final TypeElement typeElement, final Field field) {
        assertNotNull("typeElement", typeElement);
        assertNotNull("field", field);
        return getFieldElement(typeElement, field.getName());
    }

    /**
     * 型エレメントに定義されたフィールドの変数エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @param fieldName
     *            フィールド名
     * @return 型エレメントに定義されたフィールドの変数エレメント． 存在しない場合は {@code null}
     */
    public static VariableElement getFieldElement(
            final TypeElement typeElement, final String fieldName) {
        assertNotNull("typeElement", typeElement);
        assertNotEmpty("fieldName", fieldName);
        for (final VariableElement variableElement : ElementFilter
            .fieldsIn(typeElement.getEnclosedElements())) {
            if (fieldName.equals(variableElement.getSimpleName().toString())) {
                return variableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたデフォルトコンストラクタの実行可能エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @return 型エレメントに定義されたデフォルトコンストラクタの実行可能エレメント． 存在しない場合は {@code null}
     */
    public static ExecutableElement getConstructorElement(
            final TypeElement typeElement) {
        assertNotNull("typeElement", typeElement);
        for (final ExecutableElement executableElement : ElementFilter
            .constructorsIn(typeElement.getEnclosedElements())) {
            if (executableElement.getParameters().size() == 0) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたコンストラクタの実行可能エレメントを返します．
     * <p>
     * 引数型が型引数を持つ場合は {@link #getConstructorElement(TypeElement, String...)}
     * を使用してください．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param parameterTypes
     *            引数型の並び
     * @return 型エレメントに定義されたコンストラクタの実行可能エレメント． 存在しない場合は {@code null}
     */
    public static ExecutableElement getConstructorElement(
            final TypeElement typeElement, final Class<?>... parameterTypes) {
        assertNotNull("typeElement", typeElement);
        assertNotNull("parameterTypes", parameterTypes);
        for (final ExecutableElement executableElement : ElementFilter
            .constructorsIn(typeElement.getEnclosedElements())) {
            if (isSameTypes(parameterTypes, executableElement.getParameters())) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたコンストラクタの実行可能エレメントを返します．
     * <p>
     * 引数がの型が配列の場合は， 要素型の名前の後に {@code []} を連ねる形式と， {@code [[LString;}
     * のような形式のどちらでも指定することができます．
     * </p>
     * <p>
     * 引数型が型引数を持つ場合は {@code "java.util.List&lt;T&gt;"} のようにそのまま指定します．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param parameterTypeNames
     *            引数の型名の並び
     * @return 型エレメントに定義されたコンストラクタの実行可能エレメント． 存在しない場合は {@code null}
     */
    public static ExecutableElement getConstructorElement(
            final TypeElement typeElement, final String... parameterTypeNames) {
        assertNotNull("typeElement", typeElement);
        assertNotNull("parameterTypeNames", parameterTypeNames);
        for (final ExecutableElement executableElement : ElementFilter
            .constructorsIn(typeElement.getEnclosedElements())) {
            if (isSameTypes(parameterTypeNames, executableElement
                .getParameters())) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたメソッドの実行可能エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @param methodName
     *            メソッド名
     * @return 型エレメントに定義されたメソッドの実行可能エレメント．存在しない場合は {@code null}
     */
    public static ExecutableElement getMethodElement(
            final TypeElement typeElement, final String methodName) {
        assertNotNull("typeElement", typeElement);
        assertNotEmpty("methodName", methodName);
        for (final ExecutableElement executableElement : ElementFilter
            .methodsIn(typeElement.getEnclosedElements())) {
            if (!methodName
                .equals(executableElement.getSimpleName().toString())) {
                continue;
            }
            if (executableElement.getParameters().size() == 0) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたメソッドの実行可能エレメントを返します．
     * <p>
     * 引数型が型引数を持つ場合は {@link #getMethodElement(TypeElement, String, String...)}
     * を使用してください．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param methodName
     *            メソッド名
     * @param parameterTypes
     *            引数型の並び
     * @return 型エレメントに定義されたメソッドの実行可能エレメント． 存在しない場合は {@code null}
     */
    public static ExecutableElement getMethodElement(
            final TypeElement typeElement, final String methodName,
            final Class<?>... parameterTypes) {
        assertNotNull("typeElement", typeElement);
        assertNotEmpty("methodName", methodName);
        assertNotNull("parameterTypes", parameterTypes);
        for (final ExecutableElement executableElement : ElementFilter
            .methodsIn(typeElement.getEnclosedElements())) {
            if (!methodName
                .equals(executableElement.getSimpleName().toString())) {
                continue;
            }
            if (isSameTypes(parameterTypes, executableElement.getParameters())) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * 型エレメントに定義されたメソッドの実行可能エレメントを返します．
     * <p>
     * 引数がの型が配列の場合は， 要素型の名前の後に {@code []} を連ねる形式と， {@code [[LString;}
     * のような形式のどちらでも指定することができます．
     * </p>
     * <p>
     * 引数型が型引数を持つ場合は {@code "java.util.List&lt;T&gt;"} のようにそのまま指定します．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param methodName
     *            メソッド名
     * @param parameterTypeNames
     *            引数の型名の並び
     * @return 型エレメントに定義されたメソッドの実行可能エレメント． 存在しない場合は {@code null}
     */
    public static ExecutableElement getMethodElement(
            final TypeElement typeElement, final String methodName,
            final String... parameterTypeNames) {
        assertNotNull("typeElement", typeElement);
        assertNotEmpty("methodName", methodName);
        assertNotNull("parameterTypeNames", parameterTypeNames);
        for (final ExecutableElement executableElement : ElementFilter
            .methodsIn(typeElement.getEnclosedElements())) {
            if (!methodName
                .equals(executableElement.getSimpleName().toString())) {
                continue;
            }
            if (isSameTypes(parameterTypeNames, executableElement
                .getParameters())) {
                return executableElement;
            }
        }
        return null;
    }

    /**
     * {@link Element} に付けられた指定の {@link AnnotationMirror} を返します．
     * 
     * @param element
     *            注釈の付けられた {@link Element}
     * @param annotationClass
     *            アノテーションのクラス
     * @return {@link Element} に付けられた指定の {@link AnnotationMirror}
     */
    public static AnnotationMirror getAnnotationMirror(final Element element,
            final Class<? extends Annotation> annotationClass) {
        return getAnnotationMirror(element, annotationClass.getName());
    }

    /**
     * {@link Element} に付けられた指定の {@link AnnotationMirror} を返します．
     * 
     * @param element
     *            注釈の付けられた {@link Element}
     * @param annotationClassName
     *            アノテーションのクラス名
     * @return {@link Element} に付けられた指定の {@link AnnotationMirror}
     */
    public static AnnotationMirror getAnnotationMirror(final Element element,
            final String annotationClassName) {
        for (final AnnotationMirror annotationMirror : element
            .getAnnotationMirrors()) {
            final DeclaredType annotationType = annotationMirror
                .getAnnotationType();
            if (annotationType.toString().equals(annotationClassName)) {
                return annotationMirror;
            }
        }
        return null;
    }

    /**
     * 引数型の配列と{@link VariableElement}のリストの， それぞれの要素の型名が等しければ {@code true} を返します．
     * 
     * @param parameterTypes
     *            引数型の配列
     * @param variableElements
     * @return 二つのリストのそれぞれの要素の型がマッチすれば {@code true}
     */
    public static boolean isSameTypes(final Class<?>[] parameterTypes,
            final List<? extends VariableElement> variableElements) {
        assertNotNull("parameterTypes", parameterTypes);
        assertNotNull("variableElements", variableElements);
        return isSameTypes(
            getQualifiedNameArray(parameterTypes),
            variableElements);
    }

    /**
     * 型名の配列と{@link VariableElement}のリストの， それぞれの要素の型名が等しければ {@code true} を返します．
     * 
     * @param typeNames
     *            型名の配列
     * @param variableElements
     * @return 二つのリストのそれぞれの要素の型がマッチすれば {@code true}
     */
    public static boolean isSameTypes(final String[] typeNames,
            final List<? extends VariableElement> variableElements) {
        assertNotNull("typeNames", typeNames);
        assertNotNull("variableElements", variableElements);
        if (typeNames.length != variableElements.size()) {
            return false;
        }
        for (int i = 0; i < typeNames.length; ++i) {
            if (!typeNames[i].equals(variableElements
                .get(i)
                .asType()
                .toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@link Element#getSimpleName()} が返す名前の {@link List} を返します．
     * 
     * @param elements
     *            {@link Element} の {@link List}
     * @return {@link Element#getSimpleName()} が返す名前の {@link List}
     */
    public static List<String> toSimpleNameList(
            final List<? extends Element> elements) {
        final List<String> result = newArrayList();
        for (final Element element : elements) {
            result.add(element.getSimpleName().toString());
        }
        return result;
    }

    /**
     * 型引数宣言の完全な文字列表現を返します．
     * 
     * @param typeParameters
     *            型引数の {@link List}
     * @return 型引数宣言の完全な文字列表現
     */
    public static String toStringOfTypeParameterDecl(
            final List<? extends TypeParameterElement> typeParameters) {
        if (typeParameters.isEmpty()) {
            return "";
        }
        final StringBuilder buf = new StringBuilder(64);
        String parameterPrefix = "<";
        for (final TypeParameterElement typeParameter : typeParameters) {
            final List<? extends TypeMirror> bounds = typeParameter.getBounds();
            buf.append(parameterPrefix).append(typeParameter.getSimpleName());
            if (bounds.size() > 1
                    || bounds.get(0).toString() == "java.lang.Object") {
                buf.append(" extends ").append(
                    join(toTypeNameList(bounds), " & "));
            }
            parameterPrefix = ", ";
        }
        buf.append('>');
        return new String(buf);
    }

    /**
     * 型引数宣言の変数名の文字列表現を返します．
     * 
     * @param typeParameters
     *            型引数の {@link List}
     * @return 型引数名の変数名の文字列表現
     */
    public static String toStringOfTypeParameterNames(
            final List<? extends TypeParameterElement> typeParameters) {
        if (typeParameters.isEmpty()) {
            return "";
        }
        final StringBuilder buf = new StringBuilder(64);
        buf
            .append("<")
            .append(join(toSimpleNameList(typeParameters), ", "))
            .append(">");
        return new String(buf);
    }

}
