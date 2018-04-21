package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import javax.lang.model.element.Modifier;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.meta.dao.ConfigMeta;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.query.QueryKind;
import org.seasar.doma.internal.apt.meta.query.QueryMeta;
import org.seasar.doma.jdbc.AbstractDao;
import org.seasar.doma.jdbc.Config;

public abstract class DaoImplGenerator implements Generator {

  protected final CodeSpec codeSpec;

  protected final Printer printer;

  protected final DaoMeta daoMeta;

  protected DaoImplGenerator(CodeSpec codeSpec, Printer printer, DaoMeta daoMeta) {
    assertNotNull(codeSpec, printer, daoMeta);
    this.codeSpec = codeSpec;
    this.printer = printer;
    this.daoMeta = daoMeta;
  }

  @Override
  public void generate() {
    printClass();
  }

  private void printClass() {
    printer.printPackage();
    printer.iprint("/** */%n");
    for (var annotation : daoMeta.getAnnotationAnnots(AnnotationTarget.CLASS)) {
      printer.iprint("@%1$s(%2$s)%n", annotation.getTypeValue(), annotation.getElementsValue());
    }
    printer.printGenerated();
    printTypeDeclaration();
    printer.print(" {%n");
    printer.print("%n");
    printer.indent();
    printer.printValidateVersionStaticInitializer();
    printStaticFields();
    printConstructors();
    printMethods();
    printer.unindent();
    printer.print("}%n");
  }

  protected abstract void printTypeDeclaration();

  private void printStaticFields() {
    var i = 0;
    for (var queryMeta : daoMeta.getQueryMetas()) {
      var kind = queryMeta.getQueryKind();
      if (kind != QueryKind.DEFAULT) {
        printer.iprint(
            "private static final %1$s __method%2$s = %3$s.getDeclaredMethod(%4$s.class, \"%5$s\"",
            /* 1 */ Method.class.getName(),
            /* 2 */ i,
            /* 3 */ AbstractDao.class.getName(),
            /* 4 */ daoMeta.getDaoType(),
            /* 5 */ queryMeta.getName());
        for (var parameterMeta : queryMeta.getParameterMetas()) {
          printer.print(", %1$s.class", parameterMeta.getQualifiedName());
        }
        printer.print(");%n");
        printer.print("%n");
      }
      i++;
    }
  }

  protected abstract void printConstructors();

  protected void printDefaultConstructor(ConfigMeta configMeta) {
    printer.iprint("/** */%n");
    printer.iprint("public %1$s() {%n", codeSpec.getSimpleName());
    printer.indent();
    if (configMeta.getSingletonField() != null) {
      printer.iprint(
          "super(%1$s.%2$s);%n",
          /* 1 */ configMeta.getConfigType(),
          /* 2 */ configMeta.getSingletonField().getSimpleName());
    } else if (configMeta.getSingletonMethod() != null) {
      printer.iprint(
          "super(%1$s.%2$s());%n",
          /* 1 */ configMeta.getConfigType(),
          /* 2 */ configMeta.getSingletonMethod().getSimpleName());
    } else {
      printer.iprint("super(new %1$s());%n", configMeta.getConfigType());
    }
    printer.unindent();
    printer.iprint("}%n");
    printer.print("%n");
  }

  protected void printInjectableConfigConstructor() {
    printer.iprint("/**%n");
    printer.iprint(" * @param config the config%n");
    printer.iprint(" */%n");
    for (var annotation : daoMeta.getAnnotationAnnots(AnnotationTarget.CONSTRUCTOR)) {
      printer.iprint(
          "@%1$s(%2$s)%n",
          /* 1 */ annotation.getTypeValue(), /* 2 */ annotation.getElementsValue());
    }
    printer.iprint("public %1$s(", codeSpec.getSimpleName());
    for (var annotation : daoMeta.getAnnotationAnnots(AnnotationTarget.CONSTRUCTOR_PARAMETER)) {
      printer.print(
          "@%1$s(%2$s) ", /* 1 */ annotation.getTypeValue(), /* 2 */ annotation.getElementsValue());
    }
    printer.print("%1$s config) {%n", Config.class.getName());
    printer.indent();
    printer.iprint("super(config);%n");
    printer.unindent();
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printMethods() {
    var generator = new DaoImplMethodBodyGenerator(codeSpec, printer, daoMeta);
    var i = 0;
    for (var queryMeta : daoMeta.getQueryMetas()) {
      printMethod(generator, queryMeta, i);
      i++;
    }
  }

  private void printMethod(DaoImplMethodBodyGenerator generator, QueryMeta m, int index) {
    var modifiers = m.getMethodElement().getModifiers();
    printer.iprint("@Override%n");
    if (modifiers.contains(Modifier.PUBLIC)) {
      printer.iprint("public ");
    } else if (modifiers.contains(Modifier.PROTECTED)) {
      printer.iprint("protected ");
    } else {
      printer.iprint("");
    }
    if (!m.getTypeParameterNames().isEmpty()) {
      printer.print("<");
      for (var it = m.getTypeParameterNames().iterator(); it.hasNext(); ) {
        printer.print("%1$s", it.next());
        if (it.hasNext()) {
          printer.print(", ");
        }
      }
      printer.print("> ");
    }
    printer.print("%1$s %2$s(", m.getReturnMeta().getTypeName(), m.getName());
    for (var it = m.getParameterMetas().iterator(); it.hasNext(); ) {
      var parameterMeta = it.next();
      var parameterTypeName = parameterMeta.getTypeName();
      if (!it.hasNext() && m.isVarArgs()) {
        parameterTypeName = parameterTypeName.replace("[]", "...");
      }
      printer.print("%1$s %2$s", parameterTypeName, parameterMeta.getName());
      if (it.hasNext()) {
        printer.print(", ");
      }
    }
    printer.print(") ");
    if (!m.getThrownTypeNames().isEmpty()) {
      printer.print("throws ");
      for (var it = m.getThrownTypeNames().iterator(); it.hasNext(); ) {
        printer.print("%1$s", it.next());
        if (it.hasNext()) {
          printer.print(", ");
        }
      }
      printer.print(" ");
    }
    printer.print("{%n");
    printer.indent();
    m.accept(generator, "__method" + index);
    printer.unindent();
    printer.iprint("}%n");
    printer.print("%n");
  }
}
