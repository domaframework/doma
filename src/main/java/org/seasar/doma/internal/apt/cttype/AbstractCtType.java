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

  protected final TypeElement typeElement;

  private final String typeName;

  private final String packageName;

  private final String qualifiedName;

  private final String boxedTypeName;

  private final String boxedClassName;

  protected final String metaTypeName;

  protected final String metaClassName;

  protected final String typeParametersDeclaration;

  protected AbstractCtType(Context ctx, TypeMirror type) {
    assertNotNull(ctx, type);
    this.ctx = ctx;
    this.type = type;
    this.typeName = ctx.getTypes().getTypeName(type);
    this.boxedTypeName = ctx.getTypes().getBoxedTypeName(type);
    this.boxedClassName = ctx.getTypes().getBoxedClassName(type);
    this.typeElement = ctx.getTypes().toTypeElement(type);
    if (typeElement != null) {
      packageName = ctx.getElements().getPackageName(typeElement);
      qualifiedName = typeElement.getQualifiedName().toString();
      metaClassName = MetaUtil.toFullMetaName(typeElement, ctx);
    } else {
      packageName = "";
      qualifiedName = typeName;
      metaClassName = typeName;
    }
    this.typeParametersDeclaration = makeTypeParametersDeclaration(typeName);
    this.metaTypeName = metaClassName + typeParametersDeclaration;
  }

  private static String makeTypeParametersDeclaration(String typeName) {
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
  public String getMetaClassName() {
    return metaClassName;
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
  public String getBoxedClassName() {
    return boxedClassName;
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

  @Override
  public boolean isSameType(CtType other) {
    return ctx.getTypes().isSameType(type, other.getType());
  }
}
