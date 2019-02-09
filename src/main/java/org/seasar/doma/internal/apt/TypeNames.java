package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.util.ClassUtil;

public class TypeNames {

  private final Context ctx;

  TypeNames(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  public TypeName newDaoImplTypeName(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newDaoImplTypeName(typeElement, typeElement.asType());
  }

  public TypeName newDaoImplTypeName(TypeElement typeElement, TypeMirror typeMirror) {
    assertNotNull(typeElement, typeMirror);
    ClassName className = new DaoImplClassNameBuilder(typeElement).build();
    return createTypeName(className, typeMirror);
  }

  public TypeName newDomainDescTypeName(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newDomainDescTypeName(typeElement, typeElement.asType());
  }

  public TypeName newDomainDescTypeName(TypeElement typeElement, TypeMirror typeMirror) {
    assertNotNull(typeElement, typeMirror);
    String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
    ClassName className = Conventions.newDomainTypeClassName(binaryName);
    return createTypeName(className, typeMirror);
  }

  public TypeName newEmbeddableDescTypeName(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newEmbeddableDescTypeName(typeElement, typeElement.asType());
  }

  public TypeName newEmbeddableDescTypeName(TypeElement typeElement, TypeMirror typeMirror) {
    assertNotNull(typeElement, typeMirror);
    String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
    ClassName className = Conventions.newEmbeddableTypeClassName(binaryName);
    return createTypeName(className, typeMirror);
  }

  public TypeName newEntityDescTypeName(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newEntityDescTypeName(typeElement, typeElement.asType());
  }

  public TypeName newEntityDescTypeName(TypeElement typeElement, TypeMirror typeMirror) {
    assertNotNull(typeElement);
    String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
    ClassName className = Conventions.newEntityTypeClassName(binaryName);
    return createTypeName(className, typeMirror);
  }

  public TypeName newExternalDomainDescTypeName(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newExternalDomainDescTypeName(typeElement, typeElement.asType());
  }

  public TypeName newExternalDomainDescTypeName(TypeElement typeElement, TypeMirror typeMirror) {
    assertNotNull(typeElement);
    String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
    ClassName className = Conventions.newExternalDomainTypClassName(binaryName);
    return createTypeName(className, typeMirror);
  }

  private TypeName createTypeName(ClassName className, TypeMirror typeMirror) {
    String typeName = ctx.getTypes().getTypeName(typeMirror);
    String typeParameterDeclarations = makeTypeParametersDeclaration(typeName);
    return new TypeName(className, typeName, typeParameterDeclarations);
  }

  private String makeTypeParametersDeclaration(String typeName) {
    int pos = typeName.indexOf("<");
    if (pos == -1) {
      return "";
    }
    return typeName.substring(pos);
  }

  private class DaoImplClassNameBuilder {

    private final TypeElement typeElement;

    private DaoImplClassNameBuilder(TypeElement typeElement) {
      this.typeElement = typeElement;
    }

    protected String prefix() {
      String daoPackage = ctx.getOptions().getDaoPackage();
      if (daoPackage != null) {
        return daoPackage + ".";
      }
      String packageName = ctx.getElements().getPackageName(typeElement);
      String base = "";
      if (packageName != null && packageName.length() > 0) {
        base = packageName + ".";
      }
      String daoSubpackage = ctx.getOptions().getDaoSubpackage();
      if (daoSubpackage != null) {
        return base + daoSubpackage + ".";
      }
      return base;
    }

    protected String infix() {
      String normalizedName =
          Conventions.normalizeBinaryName(ctx.getElements().getBinaryNameAsString(typeElement));
      return ClassUtil.getSimpleName(normalizedName);
    }

    protected String suffix() {
      return ctx.getOptions().getDaoSuffix();
    }

    public ClassName build() {
      return new ClassName(prefix() + infix() + suffix());
    }
  }
}
