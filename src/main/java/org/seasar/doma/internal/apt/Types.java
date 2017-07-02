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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleElementVisitor8;
import javax.lang.model.util.SimpleTypeVisitor8;
import javax.lang.model.util.TypeKindVisitor8;

import org.seasar.doma.internal.util.Pair;

public class Types implements javax.lang.model.util.Types {

    private final Context ctx;

    private final javax.lang.model.util.Types typeUtils;

    public Types(Context ctx) {
        this.ctx = ctx;
        this.typeUtils = ctx.getEnv().getTypeUtils();
    }

    public TypeElement toTypeElement(TypeMirror type) {
        assertNotNull(type);
        Element element = asElement(type);
        if (element == null) {
            return null;
        }
        return element.accept(new SimpleElementVisitor8<TypeElement, Void>() {

            @Override
            public TypeElement visitType(TypeElement e, Void p) {
                return e;
            }

        }, null);
    }

    public DeclaredType toDeclaredType(TypeMirror type) {
        assertNotNull(type);
        return type.accept(new SimpleTypeVisitor8<DeclaredType, Void>() {

            @Override
            public DeclaredType visitDeclared(DeclaredType t, Void p) {
                return t;
            }

        }, null);
    }

    public TypeVariable toTypeVariable(TypeMirror type) {
        assertNotNull(type);
        return type.accept(new SimpleTypeVisitor8<TypeVariable, Void>() {

            @Override
            public TypeVariable visitTypeVariable(TypeVariable t, Void p) {
                return t;
            }

        }, null);
    }

    public boolean isAssignable(TypeMirror lhs, Class<?> rhs) {
        assertNotNull(lhs, rhs);
        TypeElement typeElement = ctx.getElements().getTypeElement(rhs);
        if (typeElement == null) {
            return false;
        }
        return isAssignable(lhs, typeElement.asType());
    }

    public boolean isAssignable(TypeMirror lhs, TypeMirror rhs) {
        assertNotNull(lhs, rhs);
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
        TypeMirror t1 = erasure(lhs);
        TypeMirror t2 = erasure(rhs);
        if (isSameType(t1, t2) || t1.equals(t2)) {
            return true;
        }
        for (TypeMirror supertype : directSupertypes(t1)) {
            if (isAssignable(supertype, t2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAssignable(Pair<TypeMirror, TypeMirror> pair) {
        assertNotNull(pair);
        return isAssignable(pair.fst, pair.snd);
    }

    public boolean isSameType(TypeMirror type, Class<?> clazz) {
        assertNotNull(type, clazz);
        if (type.getKind() == TypeKind.VOID) {
            return clazz == void.class;
        }
        TypeElement typeElement = ctx.getElements().getTypeElement(clazz);
        if (typeElement == null) {
            return false;
        }
        return isSameType(type, typeElement.asType());
    }

    public boolean isSameType(TypeMirror t1, TypeMirror t2) {
        assertNotNull(t1, t2);
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
        TypeMirror erasuredType1 = erasure(t1);
        TypeMirror erasuredType2 = erasure(t2);
        return typeUtils.isSameType(erasuredType1, erasuredType2)
                || erasuredType1.equals(erasuredType2);
    }

    public boolean isSameType(Pair<TypeMirror, TypeMirror> pair) {
        assertNotNull(pair);
        return isSameType(pair.fst, pair.snd);
    }

    public String getTypeName(TypeMirror type) {
        assertNotNull(type);
        StringBuilder p = new StringBuilder();
        type.accept(new TypeKindVisitor8<Void, StringBuilder>() {

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
                TypeElement e = toTypeElement(t);
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
                p.append("?");
                TypeMirror extendsBound = t.getExtendsBound();
                if (extendsBound != null) {
                    p.append(" extends ");
                    extendsBound.accept(this, p);
                }
                TypeMirror superBound = t.getSuperBound();
                if (superBound != null) {
                    p.append(" super ");
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

    public String getBoxedTypeName(TypeMirror type) {
        assertNotNull(type);
        switch (type.getKind()) {
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
            return getTypeName(type);
        }
    }

    public String getTypeParameterName(TypeMirror type) {
        assertNotNull(type);
        StringBuilder p = new StringBuilder();
        type.accept(new TypeKindVisitor8<Void, StringBuilder>() {

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
                    TypeElement e = boxedClass(t);
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
                TypeElement e = toTypeElement(t);
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
                String upperBoundName = ctx.getTypes().getTypeName(upperBound);
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

    public TypeMirror boxIfPrimitive(TypeMirror type) {
        assertNotNull(type);
        return type.accept(new TypeKindVisitor8<TypeMirror, Void>() {

            @Override
            public TypeMirror visitPrimitive(PrimitiveType t, Void p) {
                return boxedClass(t).asType();
            }

            @Override
            protected TypeMirror defaultAction(TypeMirror e, Void p) {
                return e;
            }

        }, null);
    }

    public TypeMirror getType(Class<?> clazz) {
        assertNotNull(clazz);
        if (clazz == void.class) {
            return getNoType(TypeKind.VOID);
        }
        if (clazz == boolean.class) {
            return getPrimitiveType(TypeKind.BOOLEAN);
        }
        if (clazz == char.class) {
            return getPrimitiveType(TypeKind.CHAR);
        }
        if (clazz == byte.class) {
            return getPrimitiveType(TypeKind.BYTE);
        }
        if (clazz == short.class) {
            return getPrimitiveType(TypeKind.SHORT);
        }
        if (clazz == int.class) {
            return getPrimitiveType(TypeKind.INT);
        }
        if (clazz == long.class) {
            return getPrimitiveType(TypeKind.LONG);
        }
        if (clazz == float.class) {
            return getPrimitiveType(TypeKind.FLOAT);
        }
        if (clazz == double.class) {
            return getPrimitiveType(TypeKind.DOUBLE);
        }
        TypeElement typeElement = ctx.getElements().getTypeElement(clazz);
        if (typeElement == null) {
            throw new AptIllegalStateException(clazz.getName());
        }
        return typeElement.asType();
    }

    public TypeMirror getSupertype(TypeMirror type,
            Class<?> superclass) {
        assertNotNull(type, superclass);
        if (isSameType(type, superclass)) {
            return type;
        }
        switch (type.getKind()) {
        case NONE:
        case NULL:
        case VOID:
            return null;
        default:
            for (TypeMirror t : directSupertypes(type)) {
                if (isSameType(t, superclass)) {
                    return t;
                }
                TypeMirror candidate = getSupertype(t, superclass);
                if (candidate != null) {
                    return candidate;
                }
            }
            return null;
        }
    }

    public Element asElement(TypeMirror type) {
        return typeUtils.asElement(type);
    }

    public TypeMirror asMemberOf(DeclaredType containing, Element element) {
        return typeUtils.asMemberOf(containing, element);
    }

    public TypeElement boxedClass(PrimitiveType primitiveType) {
        return typeUtils.boxedClass(primitiveType);
    }

    public TypeMirror capture(TypeMirror type) {
        return typeUtils.capture(type);
    }

    public boolean contains(TypeMirror t1, TypeMirror t2) {
        return typeUtils.contains(t1, t2);
    }

    public List<? extends TypeMirror> directSupertypes(TypeMirror type) {
        return typeUtils.directSupertypes(type);
    }

    public TypeMirror erasure(TypeMirror type) {
        return typeUtils.erasure(type);
    }

    public ArrayType getArrayType(TypeMirror componentType) {
        return typeUtils.getArrayType(componentType);
    }

    public DeclaredType getDeclaredType(DeclaredType containing,
            TypeElement typeElem, TypeMirror... typeArgs) {
        return typeUtils.getDeclaredType(containing, typeElem, typeArgs);
    }

    public DeclaredType getDeclaredType(TypeElement typeElem,
            TypeMirror... typeArgs) {
        return typeUtils.getDeclaredType(typeElem, typeArgs);
    }

    public NoType getNoType(TypeKind kind) {
        return typeUtils.getNoType(kind);
    }

    public NullType getNullType() {
        return typeUtils.getNullType();
    }

    public PrimitiveType getPrimitiveType(TypeKind kind) {
        return typeUtils.getPrimitiveType(kind);
    }

    public WildcardType getWildcardType(TypeMirror extendsBound,
            TypeMirror superBound) {
        return typeUtils.getWildcardType(extendsBound, superBound);
    }

    public boolean isSubsignature(ExecutableType m1, ExecutableType m2) {
        return typeUtils.isSubsignature(m1, m2);
    }

    public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
        return typeUtils.isSubtype(t1, t2);
    }

    public PrimitiveType unboxedType(TypeMirror type) {
        return typeUtils.unboxedType(type);
    }

    public Collection<TypeMirror> supertypes(TypeMirror type) {
        Map<String, TypeMirror> map = new HashMap<>();
        gatherSupertypes(type, map);
        return map.values();
    }

    private void gatherSupertypes(TypeMirror type,
            Map<String, TypeMirror> map) {
        for (TypeMirror supertype : directSupertypes(type)) {
            TypeElement typeElement = toTypeElement(supertype);
            if (typeElement == null) {
                continue;
            }
            String key = ctx.getElements().getBinaryName(typeElement)
                    .toString();
            map.put(key, supertype);
            gatherSupertypes(supertype, map);
        }
    }

}
