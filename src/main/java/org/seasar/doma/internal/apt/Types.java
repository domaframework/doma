package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.*;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleElementVisitor8;
import javax.lang.model.util.SimpleTypeVisitor8;
import javax.lang.model.util.TypeKindVisitor8;

public class Types {

  private final Context ctx;

  private final ProcessingEnvironment env;

  private final javax.lang.model.util.Types typeUtils;

  public Types(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
    this.env = ctx.getEnv();
    this.typeUtils = env.getTypeUtils();
  }

  // delegate to typeUtils
  public Element asElement(TypeMirror t) {
    return typeUtils.asElement(t);
  }

  // delegate to typeUtils
  public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
    return typeUtils.isSubtype(t1, t2);
  }

  // delegate to typeUtils
  public boolean contains(TypeMirror t1, TypeMirror t2) {
    return typeUtils.contains(t1, t2);
  }

  // delegate to typeUtils
  public boolean isSubsignature(ExecutableType m1, ExecutableType m2) {
    return typeUtils.isSubsignature(m1, m2);
  }

  // delegate to typeUtils
  public List<? extends TypeMirror> directSupertypes(TypeMirror t) {
    return typeUtils.directSupertypes(t);
  }

  // delegate to typeUtils
  public TypeMirror erasure(TypeMirror t) {
    return typeUtils.erasure(t);
  }

  // delegate to typeUtils
  public TypeElement boxedClass(PrimitiveType p) {
    return typeUtils.boxedClass(p);
  }

  // delegate to typeUtils
  public PrimitiveType unboxedType(TypeMirror t) {
    return typeUtils.unboxedType(t);
  }

  // delegate to typeUtils
  public TypeMirror capture(TypeMirror t) {
    return typeUtils.capture(t);
  }

  // delegate to typeUtils
  public PrimitiveType getPrimitiveType(TypeKind kind) {
    return typeUtils.getPrimitiveType(kind);
  }

  // delegate to typeUtils
  public NullType getNullType() {
    return typeUtils.getNullType();
  }

  // delegate to typeUtils
  public NoType getNoType(TypeKind kind) {
    return typeUtils.getNoType(kind);
  }

  // delegate to typeUtils
  public ArrayType getArrayType(TypeMirror componentType) {
    return typeUtils.getArrayType(componentType);
  }

  // delegate to typeUtils
  public WildcardType getWildcardType(TypeMirror extendsBound, TypeMirror superBound) {
    return typeUtils.getWildcardType(extendsBound, superBound);
  }

  // delegate to typeUtils
  public DeclaredType getDeclaredType(TypeElement typeElem, TypeMirror... typeArgs) {
    return typeUtils.getDeclaredType(typeElem, typeArgs);
  }

  // delegate to typeUtils
  public DeclaredType getDeclaredType(
      DeclaredType containing, TypeElement typeElem, TypeMirror... typeArgs) {
    return typeUtils.getDeclaredType(containing, typeElem, typeArgs);
  }

  // delegate to typeUtils
  public TypeMirror asMemberOf(DeclaredType containing, Element element) {
    return typeUtils.asMemberOf(containing, element);
  }

  public TypeElement toTypeElement(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
    Element element = typeUtils.asElement(typeMirror);
    if (element == null) {
      return null;
    }
    return element.accept(
        new SimpleElementVisitor8<TypeElement, Void>() {

          public TypeElement visitType(TypeElement e, Void p) {
            return e;
          }
        },
        null);
  }

  public DeclaredType toDeclaredType(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
    return typeMirror.accept(
        new SimpleTypeVisitor8<DeclaredType, Void>() {

          public DeclaredType visitDeclared(DeclaredType t, Void p) {
            return t;
          }
        },
        null);
  }

  public TypeVariable toTypeVariable(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
    return typeMirror.accept(
        new SimpleTypeVisitor8<TypeVariable, Void>() {

          public TypeVariable visitTypeVariable(TypeVariable t, Void p) {
            return t;
          }
        },
        null);
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
    TypeMirror t1 = typeUtils.erasure(lhs);
    TypeMirror t2 = typeUtils.erasure(rhs);
    if (typeUtils.isSameType(t1, t2) || t1.equals(t2)) {
      return true;
    }
    for (TypeMirror supertype : typeUtils.directSupertypes(t1)) {
      if (isAssignable(supertype, t2)) {
        return true;
      }
    }
    return false;
  }

  public boolean isSameType(TypeMirror typeMirror, Class<?> clazz) {
    assertNotNull(typeMirror, clazz);
    if (typeMirror.getKind() == TypeKind.VOID) {
      return clazz == void.class;
    }
    TypeElement typeElement = ctx.getElements().getTypeElement(clazz);
    if (typeElement == null) {
      return false;
    }
    return isSameType(typeMirror, typeElement.asType());
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
    TypeMirror erasuredType1 = typeUtils.erasure(t1);
    TypeMirror erasuredType2 = typeUtils.erasure(t2);
    return typeUtils.isSameType(erasuredType1, erasuredType2)
        || erasuredType1.equals(erasuredType2);
  }

  public String getTypeName(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
    StringBuilder p = new StringBuilder();
    typeMirror.accept(
        new TypeKindVisitor8<Void, StringBuilder>() {

          public Void visitNoTypeAsVoid(NoType t, StringBuilder p) {
            p.append("void");
            return null;
          }

          public Void visitPrimitive(PrimitiveType t, StringBuilder p) {
            p.append(t.getKind().name().toLowerCase());
            return null;
          }

          public Void visitArray(ArrayType t, StringBuilder p) {
            t.getComponentType().accept(this, p);
            p.append("[]");
            return null;
          }

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

          public Void visitTypeVariable(TypeVariable t, StringBuilder p) {
            p.append(t);
            return null;
          }

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

          protected Void defaultAction(TypeMirror e, StringBuilder p) {
            p.append(e);
            throw new IllegalArgumentException(p.toString());
          }
        },
        p);

    return p.toString();
  }

  public String getBoxedTypeName(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
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
        return getTypeName(typeMirror);
    }
  }

  public String getTypeParameterName(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
    StringBuilder p = new StringBuilder();
    typeMirror.accept(
        new TypeKindVisitor8<Void, StringBuilder>() {

          public Void visitNoTypeAsVoid(NoType t, StringBuilder p) {
            p.append("void");
            return null;
          }

          public Void visitPrimitive(PrimitiveType t, StringBuilder p) {
            if (p.length() == 0) {
              p.append(t);
            } else {
              TypeElement e = typeUtils.boxedClass(t);
              p.append(e.getSimpleName());
            }
            return null;
          }

          public Void visitArray(ArrayType t, StringBuilder p) {
            t.getComponentType().accept(this, p);
            p.append("[]");
            return null;
          }

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

          public Void visitTypeVariable(TypeVariable t, StringBuilder p) {
            p.append(t);
            TypeMirror upperBound = t.getUpperBound();
            String upperBoundName = getTypeName(upperBound);
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

          protected Void defaultAction(TypeMirror e, StringBuilder p) {
            p.append(e);
            throw new IllegalArgumentException(p.toString());
          }
        },
        p);

    return p.toString();
  }

  public Map<TypeMirror, TypeMirror> createTypeParameterMap(
      TypeElement typeElement, TypeMirror typeMirror) {
    assertNotNull(typeElement, typeMirror, ctx);
    Map<TypeMirror, TypeMirror> typeParameterMap = new HashMap<TypeMirror, TypeMirror>();
    Iterator<? extends TypeParameterElement> formalParams =
        typeElement.getTypeParameters().iterator();
    DeclaredType declaredType = toDeclaredType(typeMirror);
    Iterator<? extends TypeMirror> actualParams = declaredType.getTypeArguments().iterator();
    for (; formalParams.hasNext() && actualParams.hasNext(); ) {
      TypeMirror key = formalParams.next().asType();
      TypeMirror value = actualParams.next();
      typeParameterMap.put(key, value);
    }
    return Collections.unmodifiableMap(typeParameterMap);
  }

  public TypeMirror resolveTypeParameter(
      Map<TypeMirror, TypeMirror> typeParameterMap, TypeMirror formalTypeParam) {
    assertNotNull(typeParameterMap, formalTypeParam);
    if (typeParameterMap.containsKey(formalTypeParam)) {
      return typeParameterMap.get(formalTypeParam);
    }
    return formalTypeParam;
  }

  public TypeMirror boxIfPrimitive(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
    return typeMirror.accept(
        new TypeKindVisitor8<TypeMirror, Void>() {

          public TypeMirror visitPrimitive(PrimitiveType t, Void p) {
            return typeUtils.boxedClass(t).asType();
          }

          protected TypeMirror defaultAction(TypeMirror e, Void p) {
            return e;
          }
        },
        null);
  }

  public TypeMirror getTypeMirror(Class<?> clazz) {
    assertNotNull(clazz);
    if (clazz == void.class) {
      return typeUtils.getNoType(TypeKind.VOID);
    }
    if (clazz == boolean.class) {
      return typeUtils.getPrimitiveType(TypeKind.BOOLEAN);
    }
    if (clazz == char.class) {
      return typeUtils.getPrimitiveType(TypeKind.CHAR);
    }
    if (clazz == byte.class) {
      return typeUtils.getPrimitiveType(TypeKind.BYTE);
    }
    if (clazz == short.class) {
      return typeUtils.getPrimitiveType(TypeKind.SHORT);
    }
    if (clazz == int.class) {
      return typeUtils.getPrimitiveType(TypeKind.INT);
    }
    if (clazz == long.class) {
      return typeUtils.getPrimitiveType(TypeKind.LONG);
    }
    if (clazz == float.class) {
      return typeUtils.getPrimitiveType(TypeKind.FLOAT);
    }
    if (clazz == double.class) {
      return typeUtils.getPrimitiveType(TypeKind.DOUBLE);
    }
    TypeElement typeElement = ctx.getElements().getTypeElement(clazz);
    if (typeElement == null) {
      throw new AptIllegalStateException(clazz.getName());
    }
    return typeElement.asType();
  }

  public TypeMirror getSupertypeMirror(TypeMirror typeMirror, Class<?> superclass) {
    assertNotNull(typeMirror, superclass);
    if (isSameType(typeMirror, superclass)) {
      return typeMirror;
    }
    switch (typeMirror.getKind()) {
      case NONE:
      case NULL:
      case VOID:
        return null;
      default:
        for (TypeMirror t : typeUtils.directSupertypes(typeMirror)) {
          if (isSameType(t, superclass)) {
            return t;
          }
          TypeMirror candidate = getSupertypeMirror(t, superclass);
          if (candidate != null) {
            return candidate;
          }
        }
        return null;
    }
  }

  public String getClassName(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
    StringBuilder p = new StringBuilder();
    typeMirror.accept(
        new TypeKindVisitor8<Void, StringBuilder>() {

          public Void visitNoTypeAsVoid(NoType t, StringBuilder p) {
            p.append("void");
            return null;
          }

          public Void visitPrimitive(PrimitiveType t, StringBuilder p) {
            p.append(t.getKind().name().toLowerCase());
            return null;
          }

          public Void visitArray(ArrayType t, StringBuilder p) {
            t.getComponentType().accept(this, p);
            p.append("[]");
            return null;
          }

          public Void visitDeclared(DeclaredType t, StringBuilder p) {
            TypeElement e = toTypeElement(t);
            if (e != null) {
              p.append(e.getQualifiedName());
            }
            return null;
          }
        },
        p);

    return p.length() > 0 ? p.toString() : Object.class.getName();
  }

  public String getBoxedClassName(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
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
        return getClassName(typeMirror);
    }
  }
}
