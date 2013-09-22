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
import javax.lang.model.util.Types;

import org.seasar.doma.internal.apt.AptIllegalStateException;

/**
 * @author taedium
 * 
 */
public final class TypeMirrorUtil {

    public static TypeElement toTypeElement(TypeMirror typeMirror,
            final ProcessingEnvironment env) {
        assertNotNull(typeMirror, env);
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

        }, null);
    }

    public static TypeVariable toTypeVariable(TypeMirror typeMirror,
            ProcessingEnvironment env) {
        assertNotNull(typeMirror, env);
        return typeMirror.accept(new SimpleTypeVisitor6<TypeVariable, Void>() {

            @Override
            public TypeVariable visitTypeVariable(TypeVariable t, Void p) {
                return t;
            }

        }, null);
    }

    public static boolean isAssignable(TypeMirror lhs, Class<?> rhs,
            ProcessingEnvironment env) {
        assertNotNull(lhs, rhs, env);
        TypeElement typeElement = ElementUtil.getTypeElement(rhs, env);
        if (typeElement == null) {
            return false;
        }
        return isAssignable(lhs, typeElement.asType(), env);
    }

    public static boolean isAssignable(TypeMirror lhs, TypeMirror rhs,
            ProcessingEnvironment env) {
        assertNotNull(lhs, rhs, env);
        if (lhs.getKind() == TypeKind.NONE || rhs.getKind() == TypeKind.NONE) {
            return false;
        }
        if (lhs.getKind() == TypeKind.NULL) {
            return rhs.getKind() == TypeKind.NULL;
        }
        if (rhs.getKind() == TypeKind.NULL) {
            return lhs.getKind() == TypeKind.NULL;
        }
        if (lhs.getKind() == TypeKind.VOID) {
            return rhs.getKind() == TypeKind.VOID;
        }
        if (rhs.getKind() == TypeKind.VOID) {
            return lhs.getKind() == TypeKind.VOID;
        }
        Types types = env.getTypeUtils();
        TypeMirror t1 = types.erasure(lhs);
        TypeMirror t2 = types.erasure(rhs);
        if (env.getTypeUtils().isSameType(t1, t2) || t1.equals(t2)) {
            return true;
        }
        for (TypeMirror supertype : types.directSupertypes(t1)) {
            if (isAssignable(supertype, t2, env)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSameType(TypeMirror typeMirror, Class<?> clazz,
            ProcessingEnvironment env) {
        assertNotNull(typeMirror, clazz, env);
        if (typeMirror.getKind() == TypeKind.VOID) {
            return clazz == void.class;
        }
        TypeElement typeElement = ElementUtil.getTypeElement(clazz, env);
        if (typeElement == null) {
            return false;
        }
        return isSameType(typeMirror, typeElement.asType(), env);
    }

    public static boolean isSameType(TypeMirror t1, TypeMirror t2,
            ProcessingEnvironment env) {
        assertNotNull(t1, t2, env);
        if (t1.getKind() == TypeKind.NONE || t2.getKind() == TypeKind.NONE) {
            return false;
        }
        if (t1.getKind() == TypeKind.NULL) {
            return t2.getKind() == TypeKind.NULL;
        }
        if (t2.getKind() == TypeKind.NULL) {
            return t1.getKind() == TypeKind.NULL;
        }
        if (t1.getKind() == TypeKind.VOID) {
            return t2.getKind() == TypeKind.VOID;
        }
        if (t2.getKind() == TypeKind.VOID) {
            return t1.getKind() == TypeKind.VOID;
        }
        TypeMirror erasuredType1 = env.getTypeUtils().erasure(t1);
        TypeMirror erasuredType2 = env.getTypeUtils().erasure(t2);
        return env.getTypeUtils().isSameType(erasuredType1, erasuredType2)
                || erasuredType1.equals(erasuredType2);
    }

    public static String getTypeName(TypeMirror typeMirror,
            final ProcessingEnvironment env) {
        assertNotNull(typeMirror, env);
        StringBuilder p = new StringBuilder();
        typeMirror.accept(new TypeKindVisitor6<Void, StringBuilder>() {

            @Override
            public Void visitNoTypeAsVoid(NoType t, StringBuilder p) {
                p.append("void");
                return null;
            }

            @Override
            public Void visitPrimitive(PrimitiveType t, StringBuilder p) {
                p.append(t.getKind().name().toLowerCase());
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
                p.append(t);
                return null;
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

    public static String getTypeNameAsTypeParameter(TypeMirror typeMirror,
            final ProcessingEnvironment env) {
        assertNotNull(typeMirror, env);
        switch (typeMirror.getKind()) {
        case BOOLEAN:
            return Boolean.class.getName();
        case BYTE:
            return Byte.class.getName();
        case SHORT:
            return Short.class.getName();
        case INT:
            return Integer.class.getName();
        case LONG:
            return Long.class.getName();
        case FLOAT:
            return Float.class.getName();
        case DOUBLE:
            return Double.class.getName();
        case CHAR:
            return Character.class.getName();
        default:
            return getTypeName(typeMirror, env);
        }
    }

    public static String getTypeParameterName(TypeMirror typeMirror,
            final ProcessingEnvironment env) {
        assertNotNull(typeMirror, env);
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
                p.append(t);
                TypeMirror upperBound = t.getUpperBound();
                String upperBoundName = TypeMirrorUtil.getTypeName(upperBound,
                        env);
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
        DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(typeMirror,
                env);
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

    public static TypeMirror boxIfPrimitive(TypeMirror typeMirror,
            final ProcessingEnvironment env) {
        assertNotNull(typeMirror);
        return typeMirror.accept(new TypeKindVisitor6<TypeMirror, Void>() {

            @Override
            public TypeMirror visitPrimitive(PrimitiveType t, Void p) {
                return env.getTypeUtils().boxedClass(t).asType();
            }

            @Override
            protected TypeMirror defaultAction(TypeMirror e, Void p) {
                return e;
            }

        }, null);
    }

    public static TypeMirror getTypeMirror(Class<?> clazz,
            ProcessingEnvironment env) {
        assertNotNull(clazz);
        if (clazz == void.class) {
            return env.getTypeUtils().getNoType(TypeKind.VOID);
        }
        if (clazz == boolean.class) {
            return env.getTypeUtils().getPrimitiveType(TypeKind.BOOLEAN);
        }
        if (clazz == char.class) {
            return env.getTypeUtils().getPrimitiveType(TypeKind.CHAR);
        }
        if (clazz == byte.class) {
            return env.getTypeUtils().getPrimitiveType(TypeKind.BYTE);
        }
        if (clazz == short.class) {
            return env.getTypeUtils().getPrimitiveType(TypeKind.SHORT);
        }
        if (clazz == int.class) {
            return env.getTypeUtils().getPrimitiveType(TypeKind.INT);
        }
        if (clazz == long.class) {
            return env.getTypeUtils().getPrimitiveType(TypeKind.LONG);
        }
        if (clazz == float.class) {
            return env.getTypeUtils().getPrimitiveType(TypeKind.FLOAT);
        }
        if (clazz == double.class) {
            return env.getTypeUtils().getPrimitiveType(TypeKind.DOUBLE);
        }
        TypeElement typeElement = ElementUtil.getTypeElement(clazz, env);
        if (typeElement == null) {
            throw new AptIllegalStateException(clazz.getName());
        }
        return typeElement.asType();
    }

    public static TypeMirror getSupertypeMirror(TypeMirror typeMirror,
            Class<?> superclass, ProcessingEnvironment env) {
        assertNotNull(typeMirror, superclass, env);
        if (TypeMirrorUtil.isSameType(typeMirror, superclass, env)) {
            return typeMirror;
        }
        switch (typeMirror.getKind()) {
        case NONE:
        case NULL:
        case VOID:
            return null;
        }
        for (TypeMirror t : env.getTypeUtils().directSupertypes(typeMirror)) {
            if (isSameType(t, superclass, env)) {
                return t;
            }
            TypeMirror candidate = getSupertypeMirror(t, superclass, env);
            if (candidate != null) {
                return candidate;
            }
        }
        return null;
    }
}
