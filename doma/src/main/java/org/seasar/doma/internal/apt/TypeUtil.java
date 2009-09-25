/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.TypeKindVisitor6;

import org.seasar.doma.Domain;
import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;

/**
 * @author taedium
 * 
 */
public final class TypeUtil {

    public static TypeElement toTypeElement(TypeMirror typeMirror,
            final ProcessingEnvironment env) {
        assertNotNull(typeMirror, env);
        if (typeMirror.getKind().isPrimitive()) {
            return typeMirror.accept(
                    new SimpleTypeVisitor6<TypeElement, Void>() {

                        @Override
                        public TypeElement visitPrimitive(PrimitiveType t,
                                Void p) {
                            return env.getTypeUtils().boxedClass(t);
                        }

                    }, null);
        }
        Element element = env.getTypeUtils().asElement(typeMirror);
        if (element == null) {
            return null;
        }
        return element.accept(new SimpleElementVisitor6<TypeElement, Void>() {

            @Override
            public TypeElement visitType(TypeElement e, Void p) {
                return e;
            }

        }, null);
    }

    public static DeclaredType toDeclaredType(TypeMirror typeMirror,
            ProcessingEnvironment env) {
        assertNotNull(typeMirror, env);
        return typeMirror.accept(new SimpleTypeVisitor6<DeclaredType, Void>() {

            @Override
            public DeclaredType visitDeclared(DeclaredType t, Void p) {
                return t;
            }

            @Override
            protected DeclaredType defaultAction(TypeMirror e, Void p) {
                throw new AptIllegalStateException();
            }

        }, null);
    }

    public static boolean isAssignable(TypeMirror typeMirror, Class<?> clazz,
            ProcessingEnvironment env) {
        assertNotNull(typeMirror, clazz, env);
        TypeElement typeElement = env.getElementUtils().getTypeElement(
                clazz.getName());
        if (typeElement == null) {
            return false;
        }
        return isAssignable(typeMirror, typeElement.asType(), env);
    }

    public static boolean isSameType(TypeMirror typeMirror, Class<?> clazz,
            ProcessingEnvironment env) {
        assertNotNull(typeMirror, clazz, env);
        TypeElement typeElement = env.getElementUtils().getTypeElement(
                clazz.getName());
        if (typeElement == null) {
            return false;
        }
        DeclaredType declaredType = env.getTypeUtils().getDeclaredType(
                typeElement);
        return env.getTypeUtils().isSameType(
                env.getTypeUtils().erasure(typeMirror), declaredType);
    }

    public static boolean isAssignable(TypeMirror typeMirror1,
            TypeMirror typeMirror2, ProcessingEnvironment env) {
        assertNotNull(typeMirror1, typeMirror2, env);
        TypeMirror t1 = env.getTypeUtils().erasure(typeMirror1);
        TypeMirror t2 = env.getTypeUtils().erasure(typeMirror2);
        if (t1.equals(t2)) {
            return true;
        }
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(t1)) {
            if (isAssignable(supertype, t2, env)) {
                return true;
            }
        }
        return false;
    }

    public static String getTypeName(TypeMirror typeMirror,
            ProcessingEnvironment env) {
        assertNotNull(typeMirror, env);
        return getTypeName(typeMirror, Collections
                .<TypeMirror, TypeMirror> emptyMap(), env);
    }

    public static String getTypeName(TypeMirror typeMirror,
            final Map<TypeMirror, TypeMirror> typeParameterMap,
            final ProcessingEnvironment env) {
        assertNotNull(typeMirror, typeParameterMap, env);
        StringBuilder p = new StringBuilder();
        typeMirror.accept(new TypeKindVisitor6<Void, StringBuilder>() {

            @Override
            public Void visitNoTypeAsVoid(NoType t, StringBuilder p) {
                p.append("void");
                return null;
            }

            @Override
            public Void visitPrimitive(PrimitiveType t, StringBuilder p) {
                if (p.length() == 0) {
                    p.append(t);
                } else {
                    TypeElement e = env.getTypeUtils().boxedClass(t);
                    p.append(e.getSimpleName());
                }
                return null;
            }

            @Override
            public Void visitArray(ArrayType t, StringBuilder p) {
                t.getComponentType().accept(this, p);
                p.append("[]");
                return null;
            }

            @Override
            public Void visitDeclared(DeclaredType t, StringBuilder p) {
                TypeElement e = toTypeElement(t, env);
                if (e != null) {
                    p.append(e.getQualifiedName());
                }
                if (!t.getTypeArguments().isEmpty()) {
                    p.append("<");
                    for (TypeMirror arg : t.getTypeArguments()) {
                        arg.accept(this, p);
                        p.append(", ");
                    }
                    p.setLength(p.length() - 2);
                    p.append(">");
                }
                return null;
            }

            @Override
            public Void visitTypeVariable(TypeVariable t, StringBuilder p) {
                p.append(resolveTypeVariable(t));
                TypeMirror upperBound = t.getUpperBound();
                String upperBoundName = TypeUtil.getTypeName(upperBound,
                        typeParameterMap, env);
                if (!Object.class.getName().equals(upperBoundName)) {
                    p.append(" extends ");
                    upperBound.accept(this, p);
                } else {
                    TypeMirror lowerBound = t.getLowerBound();
                    if (lowerBound.getKind() != TypeKind.NULL) {
                        p.append(" super ");
                        lowerBound.accept(this, p);
                    }
                }
                return null;
            }

            protected TypeMirror resolveTypeVariable(TypeVariable t) {
                if (typeParameterMap.containsKey(t)) {
                    return typeParameterMap.get(t);
                }
                return t;
            }

            @Override
            public Void visitWildcard(WildcardType t, StringBuilder p) {
                TypeMirror extendsBound = t.getExtendsBound();
                if (extendsBound != null) {
                    p.append("? extends ");
                    extendsBound.accept(this, p);
                }
                TypeMirror superBound = t.getSuperBound();
                if (superBound != null) {
                    p.append("? super ");
                    superBound.accept(this, p);
                }
                return null;
            }

            @Override
            protected Void defaultAction(TypeMirror e, StringBuilder p) {
                p.append(e);
                throw new IllegalArgumentException(p.toString());
            }

        }, p);

        return p.toString();
    }

    public static Map<TypeMirror, TypeMirror> createTypeParameterMap(
            TypeElement typeElement, TypeMirror typeMirror,
            ProcessingEnvironment env) {
        assertNotNull(typeElement, typeMirror, env);
        Map<TypeMirror, TypeMirror> typeParameterMap = new HashMap<TypeMirror, TypeMirror>();
        Iterator<? extends TypeParameterElement> formalParams = typeElement
                .getTypeParameters().iterator();
        DeclaredType declaredType = TypeUtil.toDeclaredType(typeMirror, env);
        Iterator<? extends TypeMirror> actualParams = declaredType
                .getTypeArguments().iterator();
        for (; formalParams.hasNext() && actualParams.hasNext();) {
            TypeMirror key = formalParams.next().asType();
            TypeMirror value = actualParams.next();
            typeParameterMap.put(key, value);
        }
        return Collections.unmodifiableMap(typeParameterMap);
    }

    public static TypeMirror resolveTypeParameter(
            Map<TypeMirror, TypeMirror> typeParameterMap,
            TypeMirror formalTypeParam) {
        assertNotNull(typeParameterMap, formalTypeParam);
        if (typeParameterMap.containsKey(formalTypeParam)) {
            return typeParameterMap.get(formalTypeParam);
        }
        return formalTypeParam;
    }

    public static TypeMirror toWrapperTypeIfPrimitive(TypeMirror typeMirror,
            final ProcessingEnvironment env) {
        assertNotNull(typeMirror);
        return typeMirror.accept(new TypeKindVisitor6<TypeMirror, Void>() {

            @Override
            public TypeMirror visitPrimitive(PrimitiveType t, Void p) {
                return env.getTypeUtils().boxedClass(t).asType();
            }

            @Override
            public TypeMirror visitNoTypeAsVoid(NoType t, Void p) {
                return env.getElementUtils().getTypeElement(
                        Void.class.getName()).asType();
            }

            @Override
            protected TypeMirror defaultAction(TypeMirror e, Void p) {
                return e;
            }

        }, null);
    }

    protected boolean isPrimitiveInt(TypeMirror typeMirror) {
        return typeMirror.getKind() == TypeKind.INT;
    }

    protected boolean isPrimitiveIntArray(TypeMirror typeMirror) {
        return typeMirror.accept(new TypeKindVisitor6<Boolean, Void>(false) {

            @Override
            public Boolean visitArray(ArrayType t, Void p) {
                return t.getComponentType().getKind() == TypeKind.INT;
            }
        }, null);
    }

    public static boolean isPrimitiveVoid(TypeMirror typeMirror) {
        return typeMirror.getKind() == TypeKind.VOID;
    }

    public static boolean isEntity(TypeMirror typeMirror, ProcessingEnvironment env) {
        TypeElement typeElement = TypeUtil.toTypeElement(typeMirror, env);
        return typeElement != null
                && typeElement.getAnnotation(Entity.class) != null;
    }

    public static boolean isDomain(TypeMirror typeMirror, ProcessingEnvironment env) {
        TypeElement typeElement = TypeUtil.toTypeElement(typeMirror, env);
        return typeElement != null
                && typeElement.getAnnotation(Domain.class) != null;
    }

    public static boolean isConfig(TypeMirror typeMirror, ProcessingEnvironment env) {
        return TypeUtil.isAssignable(typeMirror, Config.class, env);
    }

    public static boolean isCollection(TypeMirror typeMirror, ProcessingEnvironment env) {
        return TypeUtil.isAssignable(typeMirror, Collection.class, env);
    }

    public static boolean isSelectOptions(TypeMirror typeMirror, ProcessingEnvironment env) {
        return TypeUtil.isAssignable(typeMirror, SelectOptions.class, env);
    }

    public static boolean isIterationCallback(TypeMirror typeMirror, ProcessingEnvironment env) {
        return TypeUtil.isAssignable(typeMirror, IterationCallback.class, env);
    }

}
