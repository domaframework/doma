package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.util.MetaUtil;

public abstract class AbstractCtType implements CtType {

  protected final TypeMirror typeMirror;

  protected final Context ctx;

  protected final String typeName;

  protected final String boxedTypeName;

  protected final String metaTypeName;

  protected final TypeElement typeElement;

  protected final String packageName;

  protected final String packageExcludedBinaryName;

  protected final String qualifiedName;

  protected AbstractCtType(TypeMirror typeMirror, Context ctx) {
    assertNotNull(typeMirror, ctx);
    this.typeMirror = typeMirror;
    this.ctx = ctx;
    this.typeName = ctx.getTypes().getTypeName(typeMirror);
    this.boxedTypeName = ctx.getTypes().getBoxedTypeName(typeMirror);
    this.metaTypeName = getMetaTypeName(typeMirror, ctx);
    this.typeElement = ctx.getTypes().toTypeElement(typeMirror);
    if (typeElement != null) {
      qualifiedName = typeElement.getQualifiedName().toString();
      packageName = ctx.getElements().getPackageName(typeElement);
      packageExcludedBinaryName = ctx.getElements().getPackageExcludedBinaryName(typeElement);
    } else {
      qualifiedName = typeName;
      packageName = "";
      packageExcludedBinaryName = typeName;
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
  public TypeMirror getTypeMirror() {
    return typeMirror;
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
  public String getPackageExcludedBinaryName() {
    return packageExcludedBinaryName;
  }

  @Override
  public boolean isEnum() {
    return typeElement != null && typeElement.getKind() == ElementKind.ENUM;
  }

  @Override
  public boolean isPrimitive() {
    return typeMirror.getKind().isPrimitive();
  }
}
