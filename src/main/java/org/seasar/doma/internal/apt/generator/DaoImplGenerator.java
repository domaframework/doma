package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.sql.Connection;
import javax.lang.model.element.TypeElement;
import javax.sql.DataSource;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.TypeName;
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

  public DaoImplGenerator(Context ctx, TypeName typeName, Printer printer, DaoMeta daoMeta) {
    super(ctx, typeName, printer);
    assertNotNull(daoMeta);
    this.daoMeta = daoMeta;
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
    CharSequence parentClassName = AbstractDao.class.getName();
    ParentDaoMeta parentDaoMeta = daoMeta.getParentDaoMeta();
    if (parentDaoMeta != null) {
      TypeElement parentDaoElement = parentDaoMeta.getDaoElement();
      parentClassName = ctx.getTypeNames().newDaoImplTypeName(parentDaoElement).getClassName();
    }
    iprint(
        "%4$s class %1$s extends %2$s implements %3$s {%n",
        /* 1 */ simpleName,
        /* 2 */ parentClassName,
        /* 3 */ daoMeta.getDaoType(),
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

  private void printStaticFields() {
    int i = 0;
    for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
      QueryKind kind = queryMeta.getQueryKind();
      if (kind != QueryKind.DEFAULT) {
        iprint(
            "private static final %1$s __method%2$s = %3$s.getDeclaredMethod(%4$s.class, \"%5$s\"",
            Method.class.getName(),
            i,
            AbstractDao.class.getName(),
            daoMeta.getDaoType(),
            queryMeta.getName());
        for (QueryParameterMeta parameterMeta : queryMeta.getParameterMetas()) {
          print(", %1$s.class", parameterMeta.getQualifiedName());
        }
        print(");%n");
        print("%n");
      }
      i++;
    }
  }

  private void printConstructors() {
    if (daoMeta.hasUserDefinedConfig()) {
      String singletonMethodName = daoMeta.getSingletonMethodName();
      String singletonFieldName = daoMeta.getSingletonFieldName();

      iprint("/** */%n");
      iprint("public %1$s() {%n", simpleName);
      indent();
      if (singletonMethodName == null) {
        if (singletonFieldName == null) {
          iprint("super(new %1$s());%n", daoMeta.getConfigType());
        } else {
          iprint("super(%1$s.%2$s);%n", daoMeta.getConfigType(), singletonFieldName);
        }
      } else {
        iprint("super(%1$s.%2$s());%n", daoMeta.getConfigType(), singletonMethodName);
      }
      unindent();
      iprint("}%n");
      print("%n");
      if (daoMeta.getAnnotateWithAnnot() == null) {
        ParentDaoMeta parentDaoMeta = daoMeta.getParentDaoMeta();
        boolean jdbcConstructorsNecessary =
            parentDaoMeta == null || parentDaoMeta.hasUserDefinedConfig();
        if (jdbcConstructorsNecessary) {
          iprint("/**%n");
          iprint(" * @param connection the connection%n");
          iprint(" */%n");
          iprint("public %1$s(%2$s connection) {%n", simpleName, Connection.class.getName());
          indent();
          if (singletonMethodName == null) {
            if (singletonFieldName == null) {
              iprint("super(new %1$s(), connection);%n", daoMeta.getConfigType());
            } else {
              iprint(
                  "super(%1$s.%2$s, connection);%n", daoMeta.getConfigType(), singletonFieldName);
            }
          } else {
            iprint(
                "super(%1$s.%2$s(), connection);%n", daoMeta.getConfigType(), singletonMethodName);
          }
          unindent();
          iprint("}%n");
          print("%n");
          iprint("/**%n");
          iprint(" * @param dataSource the dataSource%n");
          iprint(" */%n");
          iprint("public %1$s(%2$s dataSource) {%n", simpleName, DataSource.class.getName());
          indent();
          if (singletonMethodName == null) {
            if (singletonFieldName == null) {
              iprint("super(new %1$s(), dataSource);%n", daoMeta.getConfigType());
            } else {
              iprint(
                  "super(%1$s.%2$s, dataSource);%n", daoMeta.getConfigType(), singletonFieldName);
            }
          } else {
            iprint(
                "super(%1$s.%2$s(), dataSource);%n", daoMeta.getConfigType(), singletonMethodName);
          }
          unindent();
          iprint("}%n");
          print("%n");
        }
        iprint("/**%n");
        iprint(" * @param config the configuration%n");
        iprint(" */%n");
        iprint("protected %1$s(%2$s config) {%n", simpleName, Config.class.getName());
        indent();
        iprint("super(config);%n");
        unindent();
        iprint("}%n");
        print("%n");
        if (jdbcConstructorsNecessary) {
          iprint("/**%n");
          iprint(" * @param config the configuration%n");
          iprint(" * @param connection the connection%n");
          iprint(" */%n");
          iprint(
              "protected %1$s(%2$s config, %3$s connection) {%n",
              simpleName, Config.class.getName(), Connection.class.getName());
          indent();
          iprint("super(config, connection);%n");
          unindent();
          iprint("}%n");
          print("%n");
          iprint("/**%n");
          iprint(" * @param config the configuration%n");
          iprint(" * @param dataSource the dataSource%n");
          iprint(" */%n");
          iprint(
              "protected %1$s(%2$s config, %3$s dataSource) {%n",
              simpleName, Config.class.getName(), DataSource.class.getName());
          indent();
          iprint("super(config, dataSource);%n");
          unindent();
          iprint("}%n");
          print("%n");
        }
      }
    }
    if (!daoMeta.hasUserDefinedConfig() || daoMeta.getAnnotateWithAnnot() != null) {
      iprint("/**%n");
      iprint(" * @param config the config%n");
      iprint(" */%n");
      for (AnnotationAnnot annotation :
          daoMeta.getAnnotationMirrors(AnnotationTarget.CONSTRUCTOR)) {
        iprint("@%1$s(%2$s)%n", annotation.getTypeValue(), annotation.getElementsValue());
      }
      iprint("public %1$s(", simpleName);
      for (AnnotationAnnot annotation :
          daoMeta.getAnnotationMirrors(AnnotationTarget.CONSTRUCTOR_PARAMETER)) {
        print("@%1$s(%2$s) ", annotation.getTypeValue(), annotation.getElementsValue());
      }
      print("%1$s config) {%n", Config.class.getName());
      indent();
      iprint("super(config);%n");
      unindent();
      iprint("}%n");
      print("%n");
    }
  }

  private void printMethods() {
    int index = 0;
    for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
      DaoImplMethodGenerator generator =
          new DaoImplMethodGenerator(ctx, typeName, printer, daoMeta, queryMeta, index);
      generator.generate();
      index++;
    }
  }
}
