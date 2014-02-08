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

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;

import org.seasar.doma.ParameterName;

/**
 * @author taedium
 * 
 */
public final class ElementUtil {

    public static String getPackageName(Element element,
            ProcessingEnvironment env) {
        PackageElement packageElement = env.getElementUtils().getPackageOf(
                element);
        return packageElement.getQualifiedName().toString();
    }

    public static String getBinaryName(TypeElement typeElement,
            ProcessingEnvironment env) {
        return env.getElementUtils().getBinaryName(typeElement).toString();
    }

    public static String getPackageExcludedBinaryName(TypeElement typeElement,
            ProcessingEnvironment env) {
        String binaryName = env.getElementUtils().getBinaryName(typeElement)
                .toString();
        int pos = binaryName.lastIndexOf('.');
        if (pos < 0) {
            return binaryName;
        }
        return binaryName.substring(pos + 1);
    }

    public static String getParameterName(VariableElement variableElement) {
        assertNotNull(variableElement);
        ParameterName parameterName = variableElement
                .getAnnotation(ParameterName.class);
        if (parameterName != null && !parameterName.value().isEmpty()) {
            return parameterName.value();
        }
        return variableElement.getSimpleName().toString();
    }

    public static boolean isEnclosing(Element enclosingElement,
            Element enclosedElement) {
        assertNotNull(enclosingElement, enclosedElement);
        if (enclosingElement.equals(enclosedElement)) {
            return true;
        }
        for (Element e = enclosedElement; e != null; e = e
                .getEnclosingElement()) {
            if (enclosingElement.equals(e)) {
                return true;
            }
        }
        return false;
    }

    public static ExecutableType toExecutableType(ExecutableElement element,
            ProcessingEnvironment env) {
        assertNotNull(element, env);
        return element.asType().accept(
                new SimpleTypeVisitor6<ExecutableType, Void>() {

                    @Override
                    public ExecutableType visitExecutable(ExecutableType t,
                            Void p) {
                        return t;
                    }
                }, null);
    }

    public static TypeElement toTypeElement(Element element,
            ProcessingEnvironment env) {
        assertNotNull(element, env);
        return element.accept(new SimpleElementVisitor6<TypeElement, Void>() {

            @Override
            public TypeElement visitType(TypeElement e, Void p) {
                return e;
            }

        }, null);
    }

    /**
     * バイナリ名に対応する 型要素 を返します。
     * 
     * @param className
     *            バイナリ名
     * @param env
     *            環境
     * @return 型要素、見つからない場合 {@code null}
     */
    public static TypeElement getTypeElement(String className,
            ProcessingEnvironment env) {
        assertNotNull(className, env);
        String[] parts = className.split("\\$");
        if (parts.length > 1) {
            TypeElement topElement = getTypeElement(parts[0], env);
            if (topElement == null) {
                return null;
            }
            return getEnclosedTypeElement(topElement, Arrays.asList(parts)
                    .subList(1, parts.length), env);
        }
        // Class<?> clazz = null;
        // try {
        // clazz = Class.forName(className);
        // return getTypeElement(clazz, env);
        // } catch (ClassNotFoundException ignored) {
        // }
        Elements elements = env.getElementUtils();
        try {
            return elements.getTypeElement(className);
        } catch (NullPointerException ignored) {
            return null;
        }
    }

    public static TypeElement getTypeElement(Class<?> clazz,
            ProcessingEnvironment env) {
        assertNotNull(clazz, env);
        return env.getElementUtils().getTypeElement(clazz.getCanonicalName());
    }

    public static TypeElement getEnclosedTypeElement(TypeElement typeElement,
            List<String> enclosedNames, ProcessingEnvironment env) {
        TypeElement enclosing = typeElement;
        for (String enclosedName : enclosedNames) {
            for (TypeElement enclosed : ElementFilter.typesIn(enclosing
                    .getEnclosedElements())) {
                if (enclosed.getSimpleName().contentEquals(enclosedName)) {
                    enclosing = enclosed;
                    break;
                }
            }
        }
        return typeElement != enclosing ? enclosing : null;
    }

    public static AnnotationMirror getAnnotationMirror(Element element,
            Class<? extends Annotation> annotationClass,
            ProcessingEnvironment env) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            DeclaredType annotationType = annotationMirror.getAnnotationType();
            if (TypeMirrorUtil.isSameType(annotationType, annotationClass, env)) {
                return annotationMirror;
            }
        }
        return null;
    }

    public static ExecutableElement getNoArgConstructor(
            TypeElement typeElement, ProcessingEnvironment env) {
        for (ExecutableElement constructor : ElementFilter
                .constructorsIn(typeElement.getEnclosedElements())) {
            if (constructor.getParameters().isEmpty()) {
                return constructor;
            }
        }
        return null;
    }
}
