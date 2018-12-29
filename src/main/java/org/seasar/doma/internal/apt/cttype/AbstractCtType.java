package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.MetaUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

/** @author taedium */
public abstract class AbstractCtType implements CtType {

  protected final TypeMirror typeMirror;

  protected final ProcessingEnvironment env;

  protected final String typeName;

  protected final String boxedTypeName;

  protected final String metaTypeName;

  protected final TypeElement typeElement;

  protected final String packageName;

  protected final String packageExcludedBinaryName;

  protected final String qualifiedName;

  protected AbstractCtType(TypeMirror typeMirror, ProcessingEnvironment env) {
    assertNotNull(typeMirror, env);
    this.typeMirror = typeMirror;
    this.env = env;
    this.typeName = TypeMirrorUtil.getTypeName(typeMirror, env);
    this.boxedTypeName = TypeMirrorUtil.getBoxedTypeName(typeMirror, env);
    this.metaTypeName = getMetaTypeName(typeMirror, env);
    this.typeElement = TypeMirrorUtil.toTypeElement(typeMirror, env);
    if (typeElement != null) {
      qualifiedName = typeElement.getQualifiedName().toString();
      packageName = ElementUtil.getPackageName(typeElement, env);
      packageExcludedBinaryName = ElementUtil.getPackageExcludedBinaryName(typeElement, env);
    } else {
      qualifiedName = typeName;
      packageName = "";
      packageExcludedBinaryName = typeName;
    }
  }

  private static String getMetaTypeName(TypeMirror typeMirror, ProcessingEnvironment env) {
    assertNotNull(typeMirror, env);
    String typeName = TypeMirrorUtil.getTypeName(typeMirror, env);
    TypeElement typeElement = TypeMirrorUtil.toTypeElement(typeMirror, env);
    if (typeElement == null) {
      return typeName;
    }
    return MetaUtil.toFullMetaName(typeElement, env) + makeTypeParamDecl(typeName);
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
