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
package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.SimpleElementVisitor8;

import org.seasar.doma.ParameterName;

public class Elements implements javax.lang.model.util.Elements {

    private final Context ctx;

    private final javax.lang.model.util.Elements elementUtls;

    public Elements(Context ctx) {
        this.ctx = ctx;
        this.elementUtls = ctx.getEnv().getElementUtils();
    }

    public String getPackageName(Element element) {
        PackageElement packageElement = getPackageOf(element);
        return packageElement.getQualifiedName().toString();
    }

    public Name getBinaryName(TypeElement typeElement) {
        return elementUtls.getBinaryName(typeElement);
    }

    public String getPackageExcludedBinaryName(TypeElement typeElement) {
        String binaryName = getBinaryName(typeElement)
                .toString();
        int pos = binaryName.lastIndexOf('.');
        if (pos < 0) {
            return binaryName;
        }
        return binaryName.substring(pos + 1);
    }

    public String getParameterName(VariableElement variableElement) {
        assertNotNull(variableElement);
        ParameterName parameterName = variableElement
                .getAnnotation(ParameterName.class);
        if (parameterName != null && !parameterName.value().isEmpty()) {
            return parameterName.value();
        }
        return variableElement.getSimpleName().toString();
    }

    public TypeElement toTypeElement(Element element) {
        assertNotNull(element);
        return element.accept(new SimpleElementVisitor8<TypeElement, Void>() {

            @Override
            public TypeElement visitType(TypeElement e, Void p) {
                return e;
            }

        }, null);
    }

    public TypeElement getTypeElement(CharSequence className) {
        assertNotNull(className);
        String[] parts = className.toString().split("\\$");
        if (parts.length > 1) {
            TypeElement topElement = getTypeElement(parts[0]);
            if (topElement == null) {
                return null;
            }
            return getEnclosedTypeElement(topElement,
                    Arrays.asList(parts).subList(1, parts.length));
        }
        try {
            return elementUtls.getTypeElement(className);
        } catch (NullPointerException ignored) {
            return null;
        }
    }

    public TypeElement getTypeElement(Class<?> clazz) {
        assertNotNull(clazz);
        return getTypeElement(clazz.getCanonicalName());
    }

    public TypeElement getEnclosedTypeElement(TypeElement typeElement,
            List<String> enclosedNames) {
        TypeElement enclosing = typeElement;
        for (String enclosedName : enclosedNames) {
            for (TypeElement enclosed : ElementFilter
                    .typesIn(enclosing.getEnclosedElements())) {
                if (enclosed.getSimpleName().contentEquals(enclosedName)) {
                    enclosing = enclosed;
                    break;
                }
            }
        }
        return typeElement != enclosing ? enclosing : null;
    }

    public AnnotationMirror getAnnotationMirror(Element element,
            Class<? extends Annotation> annotationClass) {
        return getAnnotationMirrorInternal(element,
                type -> ctx.getTypes().isSameType(type, annotationClass));
    }

    public AnnotationMirror getAnnotationMirror(Element element,
            String annotationClassName) {
        return getAnnotationMirrorInternal(element, type -> {
            TypeElement typeElement = ctx.getTypes().toTypeElement(type);
            if (typeElement == null) {
                return false;
            }
            return typeElement.getQualifiedName()
                    .contentEquals(annotationClassName);
        });
    }

    private AnnotationMirror getAnnotationMirrorInternal(
            Element element, 
            Predicate<DeclaredType> predicate) {
        for (AnnotationMirror annotationMirror : element
                .getAnnotationMirrors()) {
            DeclaredType annotationType = annotationMirror.getAnnotationType();
            if (predicate.test(annotationType)) {
                return annotationMirror;
            }
        }
        return null;
    }

    public ExecutableElement getNoArgConstructor(
            TypeElement typeElement) {
        for (ExecutableElement constructor : ElementFilter
                .constructorsIn(typeElement.getEnclosedElements())) {
            if (constructor.getParameters().isEmpty()) {
                return constructor;
            }
        }
        return null;
    }

    public Map<String, AnnotationValue> getValuesWithDefaults(
            AnnotationMirror annotationMirror) {
        Map<String, AnnotationValue> map = new HashMap<>();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : getElementValuesWithDefaults(
                annotationMirror).entrySet()) {
            String key = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            map.put(key, value);
        }
        return Collections.unmodifiableMap(map);
    }

    public Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValuesWithDefaults(
            AnnotationMirror annotationMirror) {
        return elementUtls.getElementValuesWithDefaults(annotationMirror);
    }

    public List<? extends AnnotationMirror> getAllAnnotationMirrors(
            Element element) {
        return elementUtls.getAllAnnotationMirrors(element);
    }

    public List<? extends Element> getAllMembers(TypeElement typeElement) {
        return elementUtls.getAllMembers(typeElement);
    }

    public String getConstantExpression(Object value) {
        return elementUtls.getConstantExpression(value);
    }

    public String getDocComment(Element element) {
        return elementUtls.getDocComment(element);
    }

    public Name getName(CharSequence cs) {
        return elementUtls.getName(cs);
    }

    public PackageElement getPackageElement(CharSequence name) {
        return elementUtls.getPackageElement(name);
    }

    public PackageElement getPackageOf(Element element) {
        return elementUtls.getPackageOf(element);
    }

    public boolean hides(Element hider, Element hidden) {
        return elementUtls.hides(hider, hidden);
    }

    public boolean isDeprecated(Element element) {
        return elementUtls.isDeprecated(element);
    }

    public boolean isFunctionalInterface(TypeElement typeElement) {
        return elementUtls.isFunctionalInterface(typeElement);
    }

    public boolean overrides(ExecutableElement overrider,
            ExecutableElement overridden, TypeElement typeElement) {
        return elementUtls.overrides(overrider, overridden, typeElement);
    }

    public void printElements(Writer writer, Element... elements) {
        elementUtls.printElements(writer, elements);
    }

}
