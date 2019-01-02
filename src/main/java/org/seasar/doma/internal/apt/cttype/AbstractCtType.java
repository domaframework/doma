package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.util.MetaUtil;

public abstract class AbstractCtType implements CtType {

  protected final Context ctx;

  protected final TypeMirror type;

  private final String typeName;

  private final String boxedTypeName;

  protected final String metaTypeName;

  protected final TypeElement typeElement;

  private final String packageName;

  private final String qualifiedName;

  protected AbstractCtType(Context ctx, TypeMirror type) {
    assertNotNull(ctx, type);
    this.ctx = ctx;
    this.type = type;
    this.typeName = ctx.getTypes().getTypeName(type);
    this.boxedTypeName = ctx.getTypes().getBoxedTypeName(type);
    this.metaTypeName = getMetaTypeName(type, ctx);
    this.typeElement = ctx.getTypes().toTypeElement(type);
    if (typeElement != null) {
      qualifiedName = typeElement.getQualifiedName().toString();
      packageName = ctx.getElements().getPackageName(typeElement);
    } else {
      qualifiedName = typeName;
      packageName = "";
    }
  }

  private static String getMetaTypeName(TypeMirror typeMirror, Context ctx) {
    assertNotNull(typeMirror, ctx);
    String typeName = ctx.getTypes().getTypeName(typeMirror);
    TypeElement typeElement = ctx.getTypes().toTypeElement(typeMirror);
    if (typeElement == null) {
      return typeName;
    }
    return MetaUtil.toFullMetaName(typeElement, ctx) + makeTypeParamDecl(typeName);
  }

  private static String makeTypeParamDecl(String typeName) {
    int pos = typeName.indexOf("<");
    if (pos == -1) {
      return "";
    }
    return typeName.substring(pos);
  }

  @Override
  public TypeMirror getType() {
    return type;
  }

  @Override
  public TypeElement getTypeElement() {
    return typeElement;
  }

  @Override
  public String getTypeName() {
    return typeName;
  }

  @Override
  public String getBoxedTypeName() {
    return boxedTypeName;
  }

  @Override
  public String getMetaTypeName() {
    return metaTypeName;
  }

  @Override
  public String getQualifiedName() {
    return qualifiedName;
  }

  @Override
  public String getPackageName() {
    return packageName;
  }

  @Override
  public boolean isEnum() {
    return typeElement != null && typeElement.getKind() == ElementKind.ENUM;
  }

  @Override
  public boolean isPrimitive() {
    return type.getKind().isPrimitive();
  }

  @Override
  public boolean isNone() {
    return type.getKind() == TypeKind.NONE;
  }

  @Override
  public boolean isWildcard() {
    return type.getKind() == TypeKind.WILDCARD;
  }

  @Override
  public boolean isTypevar() {
    return type.getKind() == TypeKind.TYPEVAR;
  }
}
