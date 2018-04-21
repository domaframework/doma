package org.seasar.doma.internal.apt.generator;

import java.sql.Connection;
import javax.sql.DataSource;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.meta.dao.ConfigMeta;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.jdbc.AbstractDao;
import org.seasar.doma.jdbc.Config;

public class DaoInterfaceImplGenerator extends DaoImplGenerator {

  public DaoInterfaceImplGenerator(CodeSpec codeSpec, Printer printer, DaoMeta daoMeta) {
    super(codeSpec, printer, daoMeta);
  }

  @Override
  protected void printTypeDeclaration() {
    var parentClassName = AbstractDao.class.getName();
    if (codeSpec.getParent() != null) {
      parentClassName = codeSpec.getParent().getQualifiedName();
    }
    printer.iprint(
        "%4$s class %1$s extends %2$s implements %3$s",
        /* 1 */ codeSpec.getSimpleName(),
        /* 2 */ parentClassName,
        /* 3 */ daoMeta.getDaoType(),
        /* 4 */ daoMeta.getAccessLevel().getModifier());
  }

  protected void printConstructors() {
    if (daoMeta.hasUserDefinedConfig()) {
      var configMeta = daoMeta.getConfigMeta();
      printDefaultConstructor(configMeta);
      if (daoMeta.getAnnotateWithAnnot() == null) {
        var parentDaoMeta = daoMeta.getParentDaoMeta();
        var jdbcConstructorsNecessary =
            parentDaoMeta == null || parentDaoMeta.hasUserDefinedConfig();
        if (jdbcConstructorsNecessary) {
          printJdbcConstructor(configMeta, Connection.class, "connection");
          printJdbcConstructor(configMeta, DataSource.class, "dataSource");
        }
        printConfigConstructor();
        if (jdbcConstructorsNecessary) {
          printConfigAndJdbcConstructor(Connection.class, "connection");
          printConfigAndJdbcConstructor(DataSource.class, "dataSource");
        }
      }
    }
    if (!daoMeta.hasUserDefinedConfig() || daoMeta.getAnnotateWithAnnot() != null) {
      printInjectableConfigConstructor();
    }
  }

  private void printConfigConstructor() {
    printer.iprint("/**%n");
    printer.iprint(" * @param config the configuration%n");
    printer.iprint(" */%n");
    printer.iprint(
        "protected %1$s(%2$s config) {%n",
        /* 1 */ codeSpec.getSimpleName(), /* 2 */ Config.class.getName());
    printer.indent();
    printer.iprint("super(config);%n");
    printer.unindent();
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printJdbcConstructor(
      ConfigMeta configMeta, Class<?> parameterClass, String parameterName) {
    printer.iprint("/**%n");
    printer.iprint(" * @param %1$s the %1$s%n", parameterName);
    printer.iprint(" */%n");
    printer.iprint(
        "public %1$s(%2$s %3$s) {%n",
        /* 1 */ codeSpec.getSimpleName(), /* 2 */ parameterClass.getName(), /* 3 */ parameterName);
    printer.indent();
    if (configMeta.getSingletonField() != null) {
      printer.iprint(
          "super(%1$s.%2$s, %3$s);%n",
          /* 1 */ configMeta.getConfigType(),
          /* 2 */ configMeta.getSingletonField().getSimpleName(),
          /* 3 */ parameterName);
    } else if (configMeta.getSingletonMethod() != null) {
      printer.iprint(
          "super(%1$s.%2$s(), %3$s);%n",
          /* 1 */ configMeta.getConfigType(),
          /* 2 */ configMeta.getSingletonMethod().getSimpleName(),
          /* 3 */ parameterName);
    } else {
      printer.iprint(
          "super(new %1$s(), %2$s);%n", /* 1 */ configMeta.getConfigType(), /* 2 */ parameterName);
    }
    printer.unindent();
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printConfigAndJdbcConstructor(Class<?> parameterClass, String parameterName) {
    printer.iprint("/**%n");
    printer.iprint(" * @param config the configuration%n");
    printer.iprint(" * @param %1$s the %1$s%n", parameterName);
    printer.iprint(" */%n");
    printer.iprint(
        "protected %1$s(%2$s config, %3$s %4$s) {%n",
        /* 1 */ codeSpec.getSimpleName(),
        /* 2 */ Config.class.getName(),
        /* 3 */ parameterClass.getName(),
        /* 4 */ parameterName);
    printer.indent();
    printer.iprint("super(config, %1$s);%n", parameterName);
    printer.unindent();
    printer.iprint("}%n");
    printer.print("%n");
  }
}
