package org.seasar.doma.internal.apt.generator;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.function.Function;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.sql.DataSource;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.DaoImplementation;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AnnotationAnnot;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.dao.ParentDaoMeta;
import org.seasar.doma.internal.apt.meta.query.QueryKind;
import org.seasar.doma.internal.apt.meta.query.QueryMeta;
import org.seasar.doma.internal.apt.meta.query.QueryParameterMeta;
import org.seasar.doma.internal.jdbc.dao.DaoImplSupport;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ConfigProvider;

public class DaoImplGenerator extends AbstractGenerator {

  private final DaoMeta daoMeta;

  private final ParentDaoMeta parentDaoMeta;

  private final CharSequence parentDaoClassName;

  public DaoImplGenerator(
      Context ctx,
      ClassName className,
      Printer printer,
      DaoMeta daoMeta,
      Function<TypeElement, ClassName> classNameProvider) {
    super(ctx, className, printer);
    assertNotNull(daoMeta, classNameProvider);
    this.daoMeta = daoMeta;
    parentDaoMeta = daoMeta.getParentDaoMeta();
    if (parentDaoMeta == null) {
      parentDaoClassName = null;
    } else {
      parentDaoClassName = classNameProvider.apply(parentDaoMeta.getTypeElement());
    }
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
    printDaoImplementation();
    iprint(
        "%4$s class %1$s implements %2$s, %3$s {%n",
        /* 1 */ simpleName,
        /* 2 */ daoMeta.getType(),
        /* 3 */ ConfigProvider.class,
        /* 4 */ daoMeta.getAccessLevel().getModifier());
    print("%n");
    indent();
    printValidateVersionStaticInitializer();
    printStaticFields();
    printFields();
    printConstructors();
    printMethods();
    unindent();
    print("}%n");
  }

  private void printDaoImplementation() {
    iprint("@%1$s%n", DaoImplementation.class);
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
            /* 3 */ DaoImplSupport.class,
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

  private void printFields() {
    printSupportField();
    printParentField();
  }

  private void printSupportField() {
    iprint("private final %1$s __support;%n%n", DaoImplSupport.class);
  }

  private void printParentField() {
    if (hasParentDao()) {
      iprint("private final %1$s __parent;%n%n", parentDaoClassName);
    }
  }

  private void printConstructors() {
    if (daoMeta.hasUserDefinedConfig()) {
      Code configCode = createConfigCode();
      printNoArgConstructor(configCode);
      if (daoMeta.getAnnotateWithAnnots().isEmpty()) {
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
    iprint("    __support = new %1$s(%2$s);%n", DaoImplSupport.class, configCode);
    if (hasParentDao()) {
      iprint("    __parent = new %1$s(%2$s);%n", parentDaoClassName, configCode);
    }
    iprint("}%n");
    print("%n");
  }

  private void printConnectionArgConstructor(Code configCode) {
    iprint("/**%n");
    iprint(" * @param connection the connection%n");
    iprint(" */%n");
    iprint("public %1$s(%2$s connection) {%n", simpleName, Connection.class);
    iprint("    __support = new %1$s(%2$s, connection);%n", DaoImplSupport.class, configCode);
    if (hasParentDao()) {
      iprint("    __parent = new %1$s(%2$s, connection);%n", parentDaoClassName, configCode);
    }
    iprint("}%n");
    print("%n");
  }

  private void printDataSourceArgConstructor(Code configCode) {
    iprint("/**%n");
    iprint(" * @param dataSource the dataSource%n");
    iprint(" */%n");
    iprint("public %1$s(%2$s dataSource) {%n", simpleName, DataSource.class);
    iprint("    __support = new %1$s(%2$s, dataSource);%n", DaoImplSupport.class, configCode);
    if (hasParentDao()) {
      iprint("    __parent = new %1$s(%2$s, dataSource);%n", parentDaoClassName, configCode);
    }
    iprint("}%n");
    print("%n");
  }

  private void printConfigArgConstructor() {
    iprint("/**%n");
    iprint(" * @param config the configuration%n");
    iprint(" */%n");
    iprint("protected %1$s(%2$s config) {%n", simpleName, Config.class);
    iprint("    __support = new %1$s(config);%n", DaoImplSupport.class);
    if (hasParentDao()) {
      iprint("    __parent = new %1$s(config);%n", parentDaoClassName);
    }
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
    iprint("__support = new %1$s(config, connection);%n", DaoImplSupport.class);
    if (hasParentDao()) {
      iprint("__parent = new %1$s(config, connection);%n", parentDaoClassName);
    }
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
    iprint("__support = new %1$s(config, dataSource);%n", DaoImplSupport.class);
    if (hasParentDao()) {
      iprint("__parent = new %1$s(config, dataSource);%n", parentDaoClassName);
    }
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
    iprint("__support = new %1$s(config);%n", DaoImplSupport.class);
    if (hasParentDao()) {
      iprint("__parent = new %1$s(config);%n", parentDaoClassName);
    }
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printMethods() {
    printGetConfigMethod();
    printDelegateMethods();
    printQueryMethods();
  }

  private void printGetConfigMethod() {
    iprint("@Override%n");
    iprint("public %1$s getConfig() {%n", Config.class);
    indent();
    iprint("return __support.getConfig();%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printDelegateMethods() {
    if (hasParentDao()) {
      for (ExecutableElement method : parentDaoMeta.getMethods()) {
        printDelegateMethod(method);
      }
    }
  }

  private void printDelegateMethod(ExecutableElement method) {
    iprint("/** */%n");
    iprint("@Override%n");
    iprint("public ");
    if (!method.getTypeParameters().isEmpty()) {
      print("<%1$s> ", method.getTypeParameters());
    }
    print("%1$s ", method.getReturnType());
    print(
        "%1$s(%2$s) ",
        method.getSimpleName(),
        method.getParameters().stream()
            .map(it -> it.asType() + " " + it.getSimpleName())
            .collect(toList()));
    if (!method.getThrownTypes().isEmpty()) {
      print("throws %1$s", method.getThrownTypes());
    }
    print("{%n");
    indent();
    if (method.getReturnType().getKind() == TypeKind.VOID) {
      iprint("");
    } else {
      iprint("return ");
    }
    print(
        "__parent.%1$s(%2$s);%n",
        method.getSimpleName(),
        method.getParameters().stream().map(VariableElement::getSimpleName).collect(toList()));
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printQueryMethods() {
    int index = 0;
    for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
      DaoImplQueryMethodGenerator generator =
          new DaoImplQueryMethodGenerator(ctx, className, printer, daoMeta, queryMeta, index);
      generator.generate();
      index++;
    }
  }

  private boolean hasParentDao() {
    return parentDaoMeta != null;
  }
}
