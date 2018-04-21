package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;

public class DaoAbstractImplGenerator extends DaoImplGenerator {

  private final Context ctx;

  public DaoAbstractImplGenerator(
      CodeSpec codeSpec, Printer printer, DaoMeta daoMeta, Context ctx) {
    super(codeSpec, printer, daoMeta);
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  @Override
  protected void printTypeDeclaration() {
    printer.iprint(
        "%3$s class %1$s extends %2$s",
        /* 1 */ codeSpec.getSimpleName(),
        /* 2 */ daoMeta.getDaoType(),
        /* 3 */ daoMeta.getAccessLevel().getModifier());
  }

  @Override
  protected void printConstructors() {
    if (daoMeta.hasUserDefinedConfig()) {
      var configMeta = daoMeta.getConfigMeta();
      printDefaultConstructor(configMeta);
    }
    if (!daoMeta.hasUserDefinedConfig() || daoMeta.getAnnotateWithAnnot() != null) {
      printInjectableConfigConstructor();
    }
  }
}
