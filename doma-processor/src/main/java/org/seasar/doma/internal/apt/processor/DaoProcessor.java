package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Function;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.generator.DaoImplGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.dao.DaoMetaFactory;
import org.seasar.doma.internal.util.ClassUtil;

@SupportedAnnotationTypes({"org.seasar.doma.Dao"})
@SupportedOptions({
  Options.TEST,
  Options.TRACE,
  Options.DEBUG,
  Options.DAO_PACKAGE,
  Options.DAO_SUBPACKAGE,
  Options.DAO_SUFFIX,
  Options.EXPR_FUNCTIONS,
  Options.SQL_VALIDATION,
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.CONFIG_PATH,
})
public class DaoProcessor extends AbstractGeneratingProcessor<DaoMeta> {

  private Function<TypeElement, ClassName> classNameProvider;

  public DaoProcessor() {
    super(Dao.class);
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    classNameProvider = createClassNameProvider();
  }

  @Override
  protected TypeElementMetaFactory<DaoMeta> createTypeElementMetaFactory() {
    return new DaoMetaFactory(ctx);
  }

  @Override
  protected ClassName createClassName(TypeElement typeElement, DaoMeta meta) {
    assertNotNull(typeElement, meta);
    return classNameProvider.apply(typeElement);
  }

  @Override
  protected Generator createGenerator(ClassName className, Printer printer, DaoMeta meta) {
    assertNotNull(className, meta, printer);
    return new DaoImplGenerator(ctx, className, printer, meta, classNameProvider);
  }

  private Function<TypeElement, ClassName> createClassNameProvider() {
    return typeElement -> {
      DaoImplClassNameBuilder builder = new DaoImplClassNameBuilder(typeElement);
      return builder.build();
    };
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
      PackageElement packageElement = ctx.getMoreElements().getPackageOf(typeElement);
      Name packageName = packageElement.getQualifiedName();
      String base = "";
      if (packageName.length() > 0) {
        base = packageName + ".";
      }
      String daoSubpackage = ctx.getOptions().getDaoSubpackage();
      if (daoSubpackage != null) {
        return base + daoSubpackage + ".";
      }
      return base;
    }

    protected String infix() {
      Name binaryName = ctx.getMoreElements().getBinaryName(typeElement);
      String normalizedName = ClassNames.normalizeBinaryName(binaryName);
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
