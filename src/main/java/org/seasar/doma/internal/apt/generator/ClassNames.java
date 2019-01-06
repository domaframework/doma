package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.util.ClassUtil;

public class ClassNames {

  private final Context ctx;

  public ClassNames(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  public ClassName newDaoImplClassName(TypeElement typeElement) {
    assertNotNull(typeElement);
    return new DaoImplClassNameBuilder(typeElement).build();
  }

  public ClassName newDomainMetaTypeClassName(TypeElement typeElement) {
    assertNotNull(typeElement);
    return new MetaTypeClassNameBuilder(typeElement).build();
  }

  public ClassName newEmbeddableMetaTypeClassName(TypeElement typeElement) {
    assertNotNull(typeElement);
    return new MetaTypeClassNameBuilder(typeElement).build();
  }

  public ClassName newEntityMetaTypeClassName(TypeElement typeElement) {
    assertNotNull(typeElement);
    return new MetaTypeClassNameBuilder(typeElement).build();
  }

  public ClassName newExternalDomainMetaTypeClassName(TypeElement typeElement) {
    assertNotNull(typeElement);
    return new ExternalDomainMetaTypeClassNameBuilder(typeElement).build();
  }

  private class MetaTypeClassNameBuilder {
    protected final TypeElement typeElement;

    private MetaTypeClassNameBuilder(TypeElement typeElement) {
      this.typeElement = typeElement;
    }

    protected String prefix() {
      String packageName = ctx.getElements().getPackageName(typeElement);
      String prefix = "";
      if (packageName != null && packageName.length() > 0) {
        prefix = packageName + ".";
      }
      return prefix;
    }

    protected String infix() {
      return Constants.METATYPE_PREFIX;
    }

    protected String suffix() {
      String binaryName =
          Conventions.normalizeBinaryName(ctx.getElements().getBinaryNameAsString(typeElement));
      return ClassUtil.getSimpleName(binaryName);
    }

    public ClassName build() {
      return new ClassName(prefix() + infix() + suffix());
    }
  }

  private class ExternalDomainMetaTypeClassNameBuilder extends MetaTypeClassNameBuilder {

    private ExternalDomainMetaTypeClassNameBuilder(TypeElement typeElement) {
      super(typeElement);
    }

    @Override
    protected String prefix() {
      return Constants.EXTERNAL_DOMAIN_METATYPE_ROOT_PACKAGE
          + "."
          + ctx.getElements().getPackageName(typeElement)
          + ".";
    }
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
      String binaryName =
          Conventions.normalizeBinaryName(ctx.getElements().getBinaryNameAsString(typeElement));
      return ClassUtil.getSimpleName(binaryName);
    }

    protected String suffix() {
      return ctx.getOptions().getDaoSuffix();
    }

    public ClassName build() {
      return new ClassName(prefix() + infix() + suffix());
    }
  }
}
