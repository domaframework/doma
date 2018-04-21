package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.generator.DaoAbstractImplGenerator;
import org.seasar.doma.internal.apt.generator.DaoInterfaceImplGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.dao.DaoMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.Dao"})
@SupportedOptions({
  Options.TEST,
  Options.DEBUG,
  Options.DAO_PACKAGE,
  Options.DAO_SUBPACKAGE,
  Options.DAO_SUFFIX,
  Options.EXPR_FUNCTIONS,
  Options.SQL_VALIDATION,
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR
})
public class DaoProcessor extends AbstractGeneratingProcessor<DaoMeta> {

  public DaoProcessor() {
    super(Dao.class);
  }

  @Override
  protected TypeElementMetaFactory<DaoMeta> createTypeElementMetaFactory(TypeElement typeElement) {
    return new DaoMetaFactory(ctx, typeElement);
  }

  @Override
  protected CodeSpec createCodeSpec(DaoMeta meta) {
    var parentDaoMeta = meta.getParentDaoMeta();
    TypeElement parentDaoElement = null;
    if (parentDaoMeta != null) {
      parentDaoElement = parentDaoMeta.getDaoElement();
    }
    return ctx.getCodeSpecs().newDaoImplCodeSpec(meta.getDaoElement(), parentDaoElement);
  }

  @Override
  protected Generator createGenerator(CodeSpec codeSpec, Printer printer, DaoMeta meta) {
    assertNotNull(meta, codeSpec, printer);
    if (meta.isInterface()) {
      return new DaoInterfaceImplGenerator(codeSpec, printer, meta);
    }
    return new DaoAbstractImplGenerator(codeSpec, printer, meta, ctx);
  }
}
