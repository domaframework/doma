package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.SimpleTypeVisitor8;
import javax.lang.model.util.TypeKindVisitor8;
import javax.lang.model.util.Types;

public class MoreTypes implements Types {

  private final Context ctx;

  private final Types typeUtils;

  public MoreTypes(Context ctx, ProcessingEnvironment env) {
    assertNotNull(ctx, env);
    this.ctx = ctx;
    this.typeUtils = env.getTypeUtils();
  }

  // delegate to typeUtils
  @Override
  public Element asElement(TypeMirror t) {
    return typeUtils.asElement(t);
  }

  // delegate to typeUtils
  @Override
  public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
    return typeUtils.isSubtype(t1, t2);
  }

  // delegate to typeUtils
  @Override
  public boolean contains(TypeMirror t1, TypeMirror t2) {
    return typeUtils.contains(t1, t2);
  }

  // delegate to typeUtils
  @Override
  public boolean isSubsignature(ExecutableType m1, ExecutableType m2) {
    return typeUtils.isSubsignature(m1, m2);
  }

  // delegate to typeUtils
  @Override
  public List<? extends TypeMirror> directSupertypes(TypeMirror t) {
    return typeUtils.directSupertypes(t);
  }

  // delegate to typeUtils
  @Override
  public TypeMirror erasure(TypeMirror t) {
    return typeUtils.erasure(t);
  }

  // delegate to typeUtils
  @Override
  public TypeElement boxedClass(PrimitiveType p) {
    return typeUtils.boxedClass(p);
  }

  // delegate to typeUtils
  @Override
  public PrimitiveType unboxedType(TypeMirror t) {
    return typeUtils.unboxedType(t);
  }

  // delegate to typeUtils
  @Override
  public TypeMirror capture(TypeMirror t) {
    return typeUtils.capture(t);
  }

  // delegate to typeUtils
  @Override
  public PrimitiveType getPrimitiveType(TypeKind kind) {
    return typeUtils.getPrimitiveType(kind);
  }

  // delegate to typeUtils
  @Override
  public NullType getNullType() {
    return typeUtils.getNullType();
  }

  // delegate to typeUtils
  @Override
  public NoType getNoType(TypeKind kind) {
    return typeUtils.getNoType(kind);
  }

  // delegate to typeUtils
  @Override
  public ArrayType getArrayType(TypeMirror componentType) {
    return typeUtils.getArrayType(componentType);
  }

  // delegate to typeUtils
  @Override
  public WildcardType getWildcardType(TypeMirror extendsBound, TypeMirror superBound) {
    return typeUtils.getWildcardType(extendsBound, superBound);
  }

  // delegate to typeUtils
  @Override
  public DeclaredType getDeclaredType(TypeElement typeElem, TypeMirror... typeArgs) {
    return typeUtils.getDeclaredType(typeElem, typeArgs);
  }

  // delegate to typeUtils
  @Override
  public DeclaredType getDeclaredType(
      DeclaredType containing, TypeElement typeElem, TypeMirror... typeArgs) {
    return typeUtils.getDeclaredType(containing, typeElem, typeArgs);
  }

  // delegate to typeUtils
  @Override
  public TypeMirror asMemberOf(DeclaredType containing, Element element) {
    return typeUtils.asMemberOf(containing, element);
  }

  // delegate to typeUtils
  @Override
  public boolean isAssignable(TypeMirror t1, TypeMirror t2) {
    return typeUtils.isAssignable(t1, t2);
  }

  // delegate to typeUtils
  @Override
  public boolean isSameType(TypeMirror t1, TypeMirror t2) {
    return typeUtils.isSameType(t1, t2);
  }

  public TypeElement toTypeElement(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
    Element element = typeUtils.asElement(typeMirror);
    if (element == null) {
      return null;
    }
    return ctx.getMoreElements().toTypeElement(element);
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

  public ArrayType toArrayType(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
    return typeMirror.accept(
        new SimpleTypeVisitor6<ArrayType, Void>() {

          public ArrayType visitArray(ArrayType t, Void p) {
            return t;
          }
        },
        null);
  }

  public boolean isAssignableWithErasure(TypeMirror lhs, Class<?> rhs) {
    assertNotNull(lhs, rhs);
    TypeElement typeElement = ctx.getMoreElements().getTypeElement(rhs);
    if (typeElement == null) {
      return false;
    }
    return isAssignableWithErasure(lhs, typeElement.asType());
  }

  public boolean isAssignableWithErasure(TypeMirror lhs, TypeMirror rhs) {
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
      if (isAssignableWithErasure(supertype, t2)) {
        return true;
      }
    }
    return false;
  }

  public boolean isSameTypeWithErasure(TypeMirror typeMirror, Class<?> clazz) {
    assertNotNull(typeMirror, clazz);
    if (clazz.isPrimitive() || clazz.isArray()) {
      return typeUtils.isSameType(typeMirror, getTypeMirror(clazz));
    }
    TypeElement typeElement = ctx.getMoreElements().getTypeElement(clazz);
    if (typeElement == null) {
      return false;
    }
    return isSameTypeWithErasure(typeMirror, typeElement.asType());
  }

  public boolean isSameTypeWithErasure(TypeMirror t1, TypeMirror t2) {
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
    TypeMirror erasureType1 = typeUtils.erasure(t1);
    TypeMirror erasureType2 = typeUtils.erasure(t2);
    return typeUtils.isSameType(erasureType1, erasureType2) || erasureType1.equals(erasureType2);
  }

  public boolean isArray(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
    return typeMirror.getKind() == TypeKind.ARRAY;
  }

  public String getTypeName(TypeMirror typeMirror) {
    assertNotNull(typeMirror);
    StringBuilder p = new StringBuilder();
    typeMirror.accept(new TypeNameBuilder(), p);
    return p.toString();
  }

  List<String> getTypeParameterNames(List<TypeMirror> typeMirrors) {
    assertNotNull(typeMirrors);
    TypeParameterNameBuilder builder = new TypeParameterNameBuilder();
    List<String> names = new ArrayList<>();
    for (TypeMirror t : typeMirrors) {
      StringBuilder p = new StringBuilder();
      t.accept(builder, p);
      names.add(p.toString());
    }
    return names;
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
    if (clazz.isArray()) {
      TypeMirror componentType = getTypeMirror(clazz.getComponentType());
      return typeUtils.getArrayType(componentType);
    }
    TypeElement typeElement = ctx.getMoreElements().getTypeElement(clazz);
    if (typeElement == null) {
      throw new AptIllegalStateException(clazz.getName());
    }
    return typeElement.asType();
  }

  private class TypeNameBuilder extends TypeKindVisitor8<Void, StringBuilder> {

    public Void visitNoTypeAsVoid(NoType t, StringBuilder p) {
      p.append("void");
      return null;
    }

    @Override
    public Void visitNoTypeAsNone(NoType t, StringBuilder stringBuilder) {
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
      return null;
    }
  }

  private class TypeParameterNameBuilder extends TypeNameBuilder {

    private Set<TypeVariable> processedVariables = new HashSet<>();

    @Override
    public Void visitPrimitive(PrimitiveType t, StringBuilder p) {
      if (p.length() == 0) {
        p.append(t);
      } else {
        TypeElement e = typeUtils.boxedClass(t);
        p.append(e.getSimpleName());
      }
      return null;
    }

    @Override
    public Void visitTypeVariable(TypeVariable t, StringBuilder p) {
      p.append(t);
      if (processedVariables.contains(t)) {
        return null;
      }
      processedVariables.add(t);
      TypeParameterElement typeParameterElement =
          ctx.getMoreElements().toTypeParameterElement(t.asElement());
      if (typeParameterElement == null) {
        return null;
      }
      // We use typeParameterElement.getBounds() instead of t.getUpperBound()
      // because t.getUpperBound() returns an invalid value in Eclipse.
      List<? extends TypeMirror> bounds = typeParameterElement.getBounds();
      if (bounds.isEmpty()) {
        return null;
      }
      Iterator<? extends TypeMirror> it = bounds.iterator();
      TypeMirror first = it.next();
      if (bounds.size() == 1 && ctx.getMoreTypes().isSameTypeWithErasure(first, Object.class)) {
        return null;
      }
      p.append(" extends ");
      first.accept(this, p);
      for (; it.hasNext(); ) {
        p.append("&");
        TypeMirror bound = it.next();
        bound.accept(this, p);
      }
      return null;
    }

    @Override
    public Void visitIntersection(IntersectionType t, StringBuilder p) {
      for (Iterator<? extends TypeMirror> it = t.getBounds().iterator(); it.hasNext(); ) {
        TypeMirror bound = it.next();
        bound.accept(this, p);
        if (it.hasNext()) {
          p.append("&");
        }
      }
      return null;
    }
  }
}
