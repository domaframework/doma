package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.function.Function;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.sql.DataSource;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AnnotationAnnot;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.dao.ParentDaoMeta;
import org.seasar.doma.internal.apt.meta.query.QueryKind;
import org.seasar.doma.internal.apt.meta.query.QueryMeta;
import org.seasar.doma.internal.apt.meta.query.QueryParameterMeta;
import org.seasar.doma.internal.jdbc.dao.AbstractDao;
import org.seasar.doma.jdbc.Config;

public class DaoImplGenerator extends AbstractGenerator {

  private final DaoMeta daoMeta;

  private final Function<TypeElement, ClassName> classNameProvider;

  public DaoImplGenerator(
      Context ctx,
      ClassName className,
      Printer printer,
      DaoMeta daoMeta,
      Function<TypeElement, ClassName> classNameProvider) {
    super(ctx, className, printer);
    assertNotNull(daoMeta, classNameProvider);
    this.daoMeta = daoMeta;
    this.classNameProvider = classNameProvider;
  }

  @Override
  public void generate() {
    printPackage();
    printClass();
  }

  private void printPackage() {
    if (!packageName.isEmpty()) {
      iprint("package %1$s;%n", packageName);
      iprint("%n");
    }
  }

  private void printClass() {
    iprint("/** */%n");
    for (AnnotationAnnot annotation : daoMeta.getAnnotationMirrors(AnnotationTarget.CLASS)) {
      iprint("@%1$s(%2$s)%n", annotation.getTypeValue(), annotation.getElementsValue());
    }
    printGenerated();
    iprint(
        "%4$s class %1$s extends %2$s implements %3$s {%n",
        /* 1 */ simpleName,
        /* 2 */ getParentClassName(),
        /* 3 */ daoMeta.getType(),
        /* 4 */ daoMeta.getAccessLevel().getModifier());
    print("%n");
    indent();
    printValidateVersionStaticInitializer();
    printStaticFields();
    printConstructors();
    printMethods();
    unindent();
    print("}%n");
  }

  private CharSequence getParentClassName() {
    ParentDaoMeta parentDaoMeta = daoMeta.getParentDaoMeta();
    return parentDaoMeta == null
        ? AbstractDao.class.getName()
        : classNameProvider.apply(parentDaoMeta.getTypeElement());
  }

  private void printStaticFields() {
    int index = 0;
    for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
      QueryKind kind = queryMeta.getQueryKind();
      if (kind != QueryKind.DEFAULT) {
        iprint(
            "private static final %1$s __method%2$s = %3$s.getDeclaredMethod(%4$s.class, \"%5$s\"",
            /* 1 */ Method.class,
            /* 2 */ index,
            /* 3 */ AbstractDao.class,
            /* 4 */ daoMeta.getTypeElement(),
            /* 5 */ queryMeta.getName());
        for (QueryParameterMeta parameterMeta : queryMeta.getParameterMetas()) {
          print(", %1$s.class", parameterMeta.getQualifiedName());
        }
        print(");%n");
        print("%n");
      }
      index++;
    }
  }

  private void printConstructors() {
    if (daoMeta.hasUserDefinedConfig()) {
      Code configCode = createConfigCode();
      printNoArgConstructor(configCode);
      if (daoMeta.getAnnotateWithAnnot() == null) {
        boolean required = areJdbcConstructorsRequired();
        if (required) {
          printConnectionArgConstructor(configCode);
          printDataSourceArgConstructor(configCode);
        }
        printConfigArgConstructor();
        if (required) {
          printConfigAndConnectionArgsConstructor();
          printConfigAndDataSourceArgsConstructor();
        }
      } else {
        printAnnotatedConstructor();
      }
    } else {
      printAnnotatedConstructor();
    }
  }

  private boolean areJdbcConstructorsRequired() {
    ParentDaoMeta parentDaoMeta = daoMeta.getParentDaoMeta();
    return parentDaoMeta == null || parentDaoMeta.hasUserDefinedConfig();
  }

  private Code createConfigCode() {
    TypeMirror type = daoMeta.getConfigType();
    String method = daoMeta.getSingletonMethodName();
    String field = daoMeta.getSingletonFieldName();
    return new Code(
        p -> {
          if (method == null) {
            if (field == null) {
              p.print("new %1$s()", type);
            } else {
              p.print("%1$s.%2$s", type, field);
            }
          } else {
            p.print("%1$s.%2$s()", type, method);
          }
        });
  }

  private void printNoArgConstructor(Code configCode) {
    iprint("/** */%n");
    iprint("public %1$s() {%n", simpleName);
    iprint("    super(%1$s);%n", configCode);
    iprint("}%n");
    print("%n");
  }

  private void printConnectionArgConstructor(Code configCode) {
    iprint("/**%n");
    iprint(" * @param connection the connection%n");
    iprint(" */%n");
    iprint("public %1$s(%2$s connection) {%n", simpleName, Connection.class);
    iprint("    super(%1$s, connection);%n", configCode);
    iprint("}%n");
    print("%n");
  }

  private void printDataSourceArgConstructor(Code configCode) {
    iprint("/**%n");
    iprint(" * @param dataSource the dataSource%n");
    iprint(" */%n");
    iprint("public %1$s(%2$s dataSource) {%n", simpleName, DataSource.class);
    iprint("    super(%1$s, dataSource);%n", configCode);
    iprint("}%n");
    print("%n");
  }

  private void printConfigArgConstructor() {
    iprint("/**%n");
    iprint(" * @param config the configuration%n");
    iprint(" */%n");
    iprint("protected %1$s(%2$s config) {%n", simpleName, Config.class);
    iprint("    super(config);%n");
    iprint("}%n");
    print("%n");
  }

  private void printConfigAndConnectionArgsConstructor() {
    iprint("/**%n");
    iprint(" * @param config the configuration%n");
    iprint(" * @param connection the connection%n");
    iprint(" */%n");
    iprint(
        "protected %1$s(%2$s config, %3$s connection) {%n",
        simpleName, Config.class, Connection.class);
    indent();
    iprint("super(config, connection);%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printConfigAndDataSourceArgsConstructor() {
    iprint("/**%n");
    iprint(" * @param config the configuration%n");
    iprint(" * @param dataSource the dataSource%n");
    iprint(" */%n");
    iprint(
        "protected %1$s(%2$s config, %3$s dataSource) {%n",
        simpleName, Config.class, DataSource.class);
    indent();
    iprint("super(config, dataSource);%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printAnnotatedConstructor() {
    iprint("/**%n");
    iprint(" * @param config the config%n");
    iprint(" */%n");
    for (AnnotationAnnot annotation : daoMeta.getAnnotationMirrors(AnnotationTarget.CONSTRUCTOR)) {
      iprint("@%1$s(%2$s)%n", annotation.getTypeValue(), annotation.getElementsValue());
    }
    iprint("public %1$s(", simpleName);
    for (AnnotationAnnot annotation :
        daoMeta.getAnnotationMirrors(AnnotationTarget.CONSTRUCTOR_PARAMETER)) {
      print("@%1$s(%2$s) ", annotation.getTypeValue(), annotation.getElementsValue());
    }
    print("%1$s config) {%n", Config.class);
    indent();
    iprint("super(config);%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printMethods() {
    int index = 0;
    for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
      DaoImplMethodGenerator generator =
          new DaoImplMethodGenerator(ctx, className, printer, daoMeta, queryMeta, index);
      generator.generate();
      index++;
    }
  }
}
