package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.sql.DataSource;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.FetchType;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CollectorCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.EnumWrapperCtType;
import org.seasar.doma.internal.apt.cttype.FunctionCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.StreamCtType;
import org.seasar.doma.internal.apt.cttype.WrapperCtType;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.dao.ParentDaoMeta;
import org.seasar.doma.internal.apt.meta.parameter.BasicInOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.BasicInParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.BasicListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.BasicOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.BasicResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.BasicSingleResultParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.CallableSqlParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.CallableSqlParameterMetaVisitor;
import org.seasar.doma.internal.apt.meta.parameter.DomainInOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.DomainInParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.DomainListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.DomainOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.DomainResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.DomainSingleResultParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.EntityListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.EntityResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.MapListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.MapResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalBasicInOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalBasicInParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalBasicListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalBasicOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalBasicResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalBasicSingleResultParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDomainInOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDomainInParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDomainListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDomainOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDomainResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDomainSingleResultParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDoubleInOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDoubleInParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDoubleListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDoubleOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDoubleResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalDoubleSingleResultParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalIntInOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalIntInParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalIntListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalIntOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalIntResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalIntSingleResultParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalLongInOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalLongInParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalLongListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalLongOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalLongResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.OptionalLongSingleResultParameterMeta;
import org.seasar.doma.internal.apt.meta.query.AbstractCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.query.ArrayCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoFunctionQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoModuleQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoProcedureQueryMeta;
import org.seasar.doma.internal.apt.meta.query.DefaultQueryMeta;
import org.seasar.doma.internal.apt.meta.query.QueryKind;
import org.seasar.doma.internal.apt.meta.query.QueryMeta;
import org.seasar.doma.internal.apt.meta.query.QueryMetaVisitor;
import org.seasar.doma.internal.apt.meta.query.QueryParameterMeta;
import org.seasar.doma.internal.apt.meta.query.QueryReturnMeta;
import org.seasar.doma.internal.apt.meta.query.SqlFileBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlFileModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlFileScriptQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlFileSelectQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlProcessorQueryMeta;
import org.seasar.doma.internal.apt.mirror.AnnotationMirror;
import org.seasar.doma.internal.jdbc.command.BasicCollectorHandler;
import org.seasar.doma.internal.jdbc.command.BasicResultListHandler;
import org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.BasicStreamHandler;
import org.seasar.doma.internal.jdbc.command.DomainCollectorHandler;
import org.seasar.doma.internal.jdbc.command.DomainResultListHandler;
import org.seasar.doma.internal.jdbc.command.DomainSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.DomainStreamHandler;
import org.seasar.doma.internal.jdbc.command.EntityCollectorHandler;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityStreamHandler;
import org.seasar.doma.internal.jdbc.command.MapCollectorHandler;
import org.seasar.doma.internal.jdbc.command.MapResultListHandler;
import org.seasar.doma.internal.jdbc.command.MapSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.MapStreamHandler;
import org.seasar.doma.internal.jdbc.command.OptionalBasicCollectorHandler;
import org.seasar.doma.internal.jdbc.command.OptionalBasicResultListHandler;
import org.seasar.doma.internal.jdbc.command.OptionalBasicSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.OptionalBasicStreamHandler;
import org.seasar.doma.internal.jdbc.command.OptionalDomainCollectorHandler;
import org.seasar.doma.internal.jdbc.command.OptionalDomainResultListHandler;
import org.seasar.doma.internal.jdbc.command.OptionalDomainSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.OptionalDomainStreamHandler;
import org.seasar.doma.internal.jdbc.command.OptionalDoubleCollectorHandler;
import org.seasar.doma.internal.jdbc.command.OptionalDoubleResultListHandler;
import org.seasar.doma.internal.jdbc.command.OptionalDoubleSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.OptionalDoubleStreamHandler;
import org.seasar.doma.internal.jdbc.command.OptionalEntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.OptionalIntCollectorHandler;
import org.seasar.doma.internal.jdbc.command.OptionalIntResultListHandler;
import org.seasar.doma.internal.jdbc.command.OptionalIntSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.OptionalIntStreamHandler;
import org.seasar.doma.internal.jdbc.command.OptionalLongCollectorHandler;
import org.seasar.doma.internal.jdbc.command.OptionalLongResultListHandler;
import org.seasar.doma.internal.jdbc.command.OptionalLongSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.OptionalLongStreamHandler;
import org.seasar.doma.internal.jdbc.command.OptionalMapSingleResultHandler;
import org.seasar.doma.internal.jdbc.dao.AbstractDao;
import org.seasar.doma.internal.jdbc.sql.BasicInOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicInParameter;
import org.seasar.doma.internal.jdbc.sql.BasicListParameter;
import org.seasar.doma.internal.jdbc.sql.BasicOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicResultListParameter;
import org.seasar.doma.internal.jdbc.sql.BasicSingleResultParameter;
import org.seasar.doma.internal.jdbc.sql.DomainInOutParameter;
import org.seasar.doma.internal.jdbc.sql.DomainInParameter;
import org.seasar.doma.internal.jdbc.sql.DomainListParameter;
import org.seasar.doma.internal.jdbc.sql.DomainOutParameter;
import org.seasar.doma.internal.jdbc.sql.DomainResultListParameter;
import org.seasar.doma.internal.jdbc.sql.DomainSingleResultParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListParameter;
import org.seasar.doma.internal.jdbc.sql.EntityResultListParameter;
import org.seasar.doma.internal.jdbc.sql.MapListParameter;
import org.seasar.doma.internal.jdbc.sql.MapResultListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicInOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicInParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicResultListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicSingleResultParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainInOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainInParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainResultListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainSingleResultParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDoubleInOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDoubleInParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDoubleListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDoubleOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDoubleResultListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDoubleSingleResultParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalIntInOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalIntInParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalIntListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalIntOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalIntResultListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalIntSingleResultParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalLongInOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalLongInParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalLongListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalLongOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalLongResultListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalLongSingleResultParameter;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.query.FunctionQuery;
import org.seasar.doma.jdbc.query.ProcedureQuery;
import org.seasar.doma.jdbc.query.SqlFileSelectQuery;

public class DaoGenerator extends AbstractGenerator {

  protected final DaoMeta daoMeta;

  public DaoGenerator(ProcessingEnvironment env, TypeElement daoElement, DaoMeta daoMeta)
      throws IOException {
    super(
        env,
        daoElement,
        Options.getDaoPackage(env),
        Options.getDaoSubpackage(env),
        "",
        Options.getDaoSuffix(env));
    assertNotNull(daoMeta);
    this.daoMeta = daoMeta;
  }

  @Override
  public void generate() {
    printPackage();
    printClass();
  }

  protected void printPackage() {
    if (!packageName.isEmpty()) {
      iprint("package %1$s;%n", packageName);
      iprint("%n");
    }
  }

  protected void printClass() {
    iprint("/** */%n");
    for (AnnotationMirror annotation : daoMeta.getAnnotationMirrors(AnnotationTarget.CLASS)) {
      iprint("@%1$s(%2$s)%n", annotation.getTypeValue(), annotation.getElementsValue());
    }
    printGenerated();
    String parentClassName = AbstractDao.class.getName();
    ParentDaoMeta parentDaoMeta = daoMeta.getParentDaoMeta();
    if (parentDaoMeta != null) {
      TypeElement parentDaotElement = parentDaoMeta.getDaoElement();
      parentClassName =
          createCanonicalName(env, parentDaotElement, fullpackage, subpackage, prefix, suffix);
    }
    iprint(
        "%4$s class %1$s extends %2$s implements %3$s {%n",
        /* 1 */ simpleName, /* 2 */
        parentClassName,
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

  protected void printStaticFields() {
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

  protected void printConstructors() {
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
      if (daoMeta.getAnnotateWithMirror() == null) {
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
    if (!daoMeta.hasUserDefinedConfig() || daoMeta.getAnnotateWithMirror() != null) {
      iprint("/**%n");
      iprint(" * @param config the config%n");
      iprint(" */%n");
      for (AnnotationMirror annotation :
          daoMeta.getAnnotationMirrors(AnnotationTarget.CONSTRUCTOR)) {
        iprint("@%1$s(%2$s)%n", annotation.getTypeValue(), annotation.getElementsValue());
      }
      iprint("public %1$s(", simpleName);
      for (AnnotationMirror annotation :
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

  protected boolean isJdbcConstructoNecessary() {
    ParentDaoMeta parentDaoMeta = daoMeta.getParentDaoMeta();
    return parentDaoMeta == null || parentDaoMeta.hasUserDefinedConfig();
  }

  protected String toCSVFormat(List<String> values) {
    final StringBuilder buf = new StringBuilder();
    if (values.size() > 0) {
      for (String value : values) {
        buf.append("\"");
        buf.append(value);
        buf.append("\", ");
      }
      buf.setLength(buf.length() - 2);
    }
    return buf.toString();
  }

  protected void printMethods() {
    MethodBodyGenerator generator = new MethodBodyGenerator();
    int i = 0;
    for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
      printMethod(generator, queryMeta, i);
      i++;
    }
  }

  protected void printMethod(MethodBodyGenerator generator, QueryMeta m, int index) {
    iprint("@Override%n");
    iprint("public ");
    if (!m.getTypeParameterNames().isEmpty()) {
      print("<");
      for (Iterator<String> it = m.getTypeParameterNames().iterator(); it.hasNext(); ) {
        print("%1$s", it.next());
        if (it.hasNext()) {
          print(", ");
        }
      }
      print("> ");
    }
    print("%1$s %2$s(", m.getReturnMeta().getTypeName(), m.getName());
    for (Iterator<QueryParameterMeta> it = m.getParameterMetas().iterator(); it.hasNext(); ) {
      QueryParameterMeta parameterMeta = it.next();
      String parameterTypeName = parameterMeta.getTypeName();
      if (!it.hasNext() && m.isVarArgs()) {
        parameterTypeName = parameterTypeName.replace("[]", "...");
      }
      print("%1$s %2$s", parameterTypeName, parameterMeta.getName());
      if (it.hasNext()) {
        print(", ");
      }
    }
    print(") ");
    if (!m.getThrownTypeNames().isEmpty()) {
      print("throws ");
      for (Iterator<String> it = m.getThrownTypeNames().iterator(); it.hasNext(); ) {
        print("%1$s", it.next());
        if (it.hasNext()) {
          print(", ");
        }
      }
      print(" ");
    }
    print("{%n");
    indent();
    m.accept(generator, "__method" + index);
    unindent();
    iprint("}%n");
    print("%n");
  }

  /**
   * メソッドボディのコードを生成します。
   *
   * @author nakamura-to
   */
  protected class MethodBodyGenerator implements QueryMetaVisitor<Void, String> {

    @Override
    public Void visitSqlFileSelectQueryMeta(final SqlFileSelectQueryMeta m, String methodName) {
      printEnteringStatements(m);
      printPrerequisiteStatements(m);

      iprint(
          "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
          m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
      iprint("__query.setMethod(%1$s);%n", methodName);
      iprint("__query.setConfig(__config);%n");
      iprint(
          "__query.setSqlFilePath(\"%1$s\");%n",
          SqlFileUtil.buildPath(
              daoMeta.getDaoElement().getQualifiedName().toString(), m.getName()));
      if (m.getSelectOptionsCtType() != null) {
        iprint("__query.setOptions(%1$s);%n", m.getSelectOptionsParameterName());
      }
      if (m.getEntityCtType() != null) {
        iprint(
            "__query.setEntityType(%1$s.getSingletonInternal());%n",
            m.getEntityCtType().getMetaTypeName());
      }

      printAddParameterStatements(m.getParameterMetas());

      iprint("__query.setCallerClassName(\"%1$s\");%n", canonicalName);
      iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
      iprint("__query.setResultEnsured(%1$s);%n", m.getEnsureResult());
      iprint("__query.setResultMappingEnsured(%1$s);%n", m.getEnsureResultMapping());
      if (m.getSelectStrategyType() == SelectType.RETURN) {
        iprint("__query.setFetchType(%1$s.%2$s);%n", FetchType.class.getName(), FetchType.LAZY);
      } else {
        iprint("__query.setFetchType(%1$s.%2$s);%n", FetchType.class.getName(), m.getFetchType());
      }
      iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
      iprint("__query.setMaxRows(%1$s);%n", m.getMaxRows());
      iprint("__query.setFetchSize(%1$s);%n", m.getFetchSize());
      iprint(
          "__query.setSqlLogType(%1$s.%2$s);%n",
          m.getSqlLogType().getClass().getName(), m.getSqlLogType());
      if (m.isResultStream()) {
        iprint("__query.setResultStream(true);%n");
      }
      iprint("__query.prepare();%n");

      QueryReturnMeta returnMeta = m.getReturnMeta();

      if (m.getSelectStrategyType() == SelectType.RETURN) {
        CtType returnCtType = returnMeta.getCtType();
        returnCtType.accept(new SqlFileSelectQueryReturnCtTypeVisitor(m, methodName), false);
        iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
        iprint("__query.complete();%n");
        iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", canonicalName, m.getName());
        iprint("return __result;%n");
      } else {
        if (m.getSelectStrategyType() == SelectType.STREAM) {
          FunctionCtType functionCtType = m.getFunctionCtType();
          functionCtType
              .getTargetCtType()
              .accept(new SqlFileSelectQueryFunctionCtTypeVisitor(m, methodName), null);
        } else if (m.getSelectStrategyType() == SelectType.COLLECT) {
          CollectorCtType collectorCtType = m.getCollectorCtType();
          collectorCtType
              .getTargetCtType()
              .accept(new SqlFileSelectQueryCollectorCtTypeVisitor(m, methodName), false);
        }
        if ("void".equals(returnMeta.getTypeName())) {
          iprint("__command.execute();%n");
          iprint("__query.complete();%n");
          iprint("exiting(\"%1$s\", \"%2$s\", null);%n", canonicalName, m.getName());
        } else {
          iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
          iprint("__query.complete();%n");
          iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", canonicalName, m.getName());
          iprint("return __result;%n");
        }
      }

      printThrowingStatements(m);
      return null;
    }

    @Override
    public Void visitSqlFileScriptQueryMeta(SqlFileScriptQueryMeta m, String methodName) {
      printEnteringStatements(m);
      printPrerequisiteStatements(m);

      iprint(
          "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
          m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
      iprint("__query.setMethod(%1$s);%n", methodName);
      iprint("__query.setConfig(__config);%n");
      iprint(
          "__query.setScriptFilePath(\"%1$s\");%n",
          ScriptFileUtil.buildPath(
              daoMeta.getDaoElement().getQualifiedName().toString(), m.getName()));
      iprint("__query.setCallerClassName(\"%1$s\");%n", canonicalName);
      iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
      iprint("__query.setBlockDelimiter(\"%1$s\");%n", m.getBlockDelimiter());
      iprint("__query.setHaltOnError(%1$s);%n", m.getHaltOnError());
      iprint(
          "__query.setSqlLogType(%1$s.%2$s);%n",
          m.getSqlLogType().getClass().getName(), m.getSqlLogType());
      iprint("__query.prepare();%n");
      iprint(
          "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
          /* 1 */ m.getCommandClass().getName(),
          /* 2 */ m.getCommandClass().getSimpleName(),
          /* 3 */ methodName);
      iprint("__command.execute();%n");
      iprint("__query.complete();%n");
      iprint("exiting(\"%1$s\", \"%2$s\", null);%n", canonicalName, m.getName());

      printThrowingStatements(m);
      return null;
    }

    @Override
    public Void visitAutoModifyQueryMeta(AutoModifyQueryMeta m, String methodName) {
      printEnteringStatements(m);
      printPrerequisiteStatements(m);

      iprint(
          "%1$s<%2$s> __query = getQueryImplementors().create%4$s(%5$s, %3$s.getSingletonInternal());%n",
          /* 1 */ m.getQueryClass().getName(),
          /* 2 */ m.getEntityCtType().getTypeName(),
          /* 3 */ m.getEntityCtType().getMetaTypeName(),
          /* 4 */ m.getQueryClass().getSimpleName(),
          /* 5 */ methodName);
      iprint("__query.setMethod(%1$s);%n", methodName);
      iprint("__query.setConfig(__config);%n");
      iprint("__query.setEntity(%1$s);%n", m.getEntityParameterName());
      iprint("__query.setCallerClassName(\"%1$s\");%n", canonicalName);
      iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
      iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
      iprint(
          "__query.setSqlLogType(%1$s.%2$s);%n",
          m.getSqlLogType().getClass().getName(), m.getSqlLogType());

      Boolean excludeNull = m.getExcludeNull();
      if (excludeNull != null) {
        iprint("__query.setNullExcluded(%1$s);%n", excludeNull);
      }

      Boolean ignoreVersion = m.getIgnoreVersion();
      if (ignoreVersion != null) {
        iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
      }

      List<String> include = m.getInclude();
      if (include != null) {
        iprint("__query.setIncludedPropertyNames(%1$s);%n", toCSVFormat(include));
      }

      List<String> exclude = m.getExclude();
      if (exclude != null) {
        iprint("__query.setExcludedPropertyNames(%1$s);%n", toCSVFormat(m.getExclude()));
      }

      Boolean includeUnchanged = m.getIncludeUnchanged();
      if (includeUnchanged != null) {
        iprint("__query.setUnchangedPropertyIncluded(%1$s);%n", includeUnchanged);
      }

      Boolean suppressOptimisticLockException = m.getSuppressOptimisticLockException();
      if (suppressOptimisticLockException != null) {
        iprint(
            "__query.setOptimisticLockExceptionSuppressed(%1$s);%n",
            suppressOptimisticLockException);
      }

      iprint("__query.prepare();%n");
      iprint(
          "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
          m.getCommandClass().getName(), m.getCommandClass().getSimpleName(), methodName);

      EntityCtType entityCtType = m.getEntityCtType();
      if (entityCtType != null && entityCtType.isImmutable()) {
        iprint("int __count = __command.execute();%n");
        iprint("__query.complete();%n");
        iprint(
            "%1$s __result = new %1$s(__count, __query.getEntity());%n",
            m.getReturnMeta().getTypeName());
      } else {
        iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getTypeName());
        iprint("__query.complete();%n");
      }

      iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", canonicalName, m.getName());
      iprint("return __result;%n");

      printThrowingStatements(m);
      return null;
    }

    @Override
    public Void visitSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m, String methodName) {
      printEnteringStatements(m);
      printPrerequisiteStatements(m);

      iprint(
          "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
          /* 1 */ m.getQueryClass().getName(),
          /* 2 */ m.getQueryClass().getSimpleName(),
          /* 3 */ methodName);
      iprint("__query.setMethod(%1$s);%n", methodName);
      iprint("__query.setConfig(__config);%n");
      iprint(
          "__query.setSqlFilePath(\"%1$s\");%n",
          SqlFileUtil.buildPath(
              daoMeta.getDaoElement().getQualifiedName().toString(), m.getName()));

      printAddParameterStatements(m.getParameterMetas());

      iprint("__query.setCallerClassName(\"%1$s\");%n", canonicalName);
      iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
      iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
      iprint(
          "__query.setSqlLogType(%1$s.%2$s);%n",
          m.getSqlLogType().getClass().getName(), m.getSqlLogType());

      if (m.getEntityParameterName() != null && m.getEntityCtType() != null) {
        iprint(
            "__query.setEntityAndEntityType(\"%1$s\", %2$s, %3$s.getSingletonInternal());%n",
            m.getEntityParameterName(),
            m.getEntityParameterName(),
            m.getEntityCtType().getMetaTypeName());
      }

      Boolean excludeNull = m.getExcludeNull();
      if (excludeNull != null) {
        iprint("__query.setNullExcluded(%1$s);%n", excludeNull);
      }

      Boolean ignoreVersion = m.getIgnoreVersion();
      if (ignoreVersion != null) {
        iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
      }

      List<String> include = m.getInclude();
      if (include != null) {
        iprint("__query.setIncludedPropertyNames(%1$s);%n", toCSVFormat(include));
      }

      List<String> exclude = m.getExclude();
      if (exclude != null) {
        iprint("__query.setExcludedPropertyNames(%1$s);%n", toCSVFormat(m.getExclude()));
      }

      Boolean includeUnchanged = m.getIncludeUnchanged();
      if (includeUnchanged != null) {
        iprint("__query.setUnchangedPropertyIncluded(%1$s);%n", includeUnchanged);
      }

      Boolean suppressOptimisticLockException = m.getSuppressOptimisticLockException();
      if (suppressOptimisticLockException != null) {
        iprint(
            "__query.setOptimisticLockExceptionSuppressed(%1$s);%n",
            suppressOptimisticLockException);
      }

      iprint("__query.prepare();%n");
      iprint(
          "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
          /* 1 */ m.getCommandClass().getName(),
          /* 2 */ m.getCommandClass().getSimpleName(),
          /* 3 */ methodName);

      EntityCtType entityCtType = m.getEntityCtType();
      if (entityCtType != null && entityCtType.isImmutable()) {
        iprint("int __count = __command.execute();%n");
        iprint("__query.complete();%n");
        iprint(
            "%1$s __result = new %1$s(__count, __query.getEntity(%2$s.class));%n",
            m.getReturnMeta().getTypeName(), entityCtType.getBoxedTypeName());
      } else {
        iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getTypeName());
        iprint("__query.complete();%n");
      }

      iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", canonicalName, m.getName());
      iprint("return __result;%n");

      printThrowingStatements(m);
      return null;
    }

    @Override
    public Void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m, String methodName) {
      printEnteringStatements(m);
      printPrerequisiteStatements(m);

      iprint(
          "%1$s<%2$s> __query = getQueryImplementors().create%4$s(%5$s, %3$s.getSingletonInternal());%n",
          /* 1 */ m.getQueryClass().getName(),
          /* 2 */ m.getEntityCtType().getTypeName(),
          /* 3 */ m.getEntityCtType().getMetaTypeName(),
          /* 4 */ m.getQueryClass().getSimpleName(),
          /* 5 */ methodName);
      iprint("__query.setMethod(%1$s);%n", methodName);
      iprint("__query.setConfig(__config);%n");
      iprint("__query.setEntities(%1$s);%n", m.getEntitiesParameterName());
      iprint("__query.setCallerClassName(\"%1$s\");%n", canonicalName);
      iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
      iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
      iprint("__query.setBatchSize(%1$s);%n", m.getBatchSize());
      iprint(
          "__query.setSqlLogType(%1$s.%2$s);%n",
          m.getSqlLogType().getClass().getName(), m.getSqlLogType());

      Boolean ignoreVersion = m.getIgnoreVersion();
      if (ignoreVersion != null) {
        iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
      }

      List<String> include = m.getInclude();
      if (include != null) {
        iprint("__query.setIncludedPropertyNames(%1$s);%n", toCSVFormat(include));
      }

      List<String> exclude = m.getExclude();
      if (exclude != null) {
        iprint("__query.setExcludedPropertyNames(%1$s);%n", toCSVFormat(exclude));
      }

      Boolean suppressOptimisticLockException = m.getSuppressOptimisticLockException();
      if (suppressOptimisticLockException != null) {
        iprint(
            "__query.setOptimisticLockExceptionSuppressed(%1$s);%n",
            suppressOptimisticLockException);
      }

      iprint("__query.prepare();%n");
      iprint(
          "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
          /* 1 */ m.getCommandClass().getName(),
          /* 2 */ m.getCommandClass().getSimpleName(),
          /* 3 */ methodName);

      EntityCtType entityCtType = m.getEntityCtType();
      if (entityCtType != null && entityCtType.isImmutable()) {
        iprint("int[] __counts = __command.execute();%n");
        iprint("__query.complete();%n");
        iprint(
            "%1$s __result = new %1$s(__counts, __query.getEntities());%n",
            m.getReturnMeta().getTypeName());
      } else {
        iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getTypeName());
        iprint("__query.complete();%n");
      }

      iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", canonicalName, m.getName());
      iprint("return __result;%n");

      printThrowingStatements(m);
      return null;
    }

    @Override
    public Void visitSqlFileBatchModifyQueryMeta(SqlFileBatchModifyQueryMeta m, String methodName) {
      printEnteringStatements(m);
      printPrerequisiteStatements(m);

      iprint(
          "%1$s<%2$s> __query = getQueryImplementors().create%4$s(%5$s, %3$s.class);%n",
          /* 1 */ m.getQueryClass().getName(),
          /* 2 */ m.getElementCtType().getBoxedTypeName(),
          /* 3 */ m.getElementCtType().getQualifiedName(),
          /* 4 */ m.getQueryClass().getSimpleName(),
          /* 5 */ methodName);
      iprint("__query.setMethod(%1$s);%n", methodName);
      iprint("__query.setConfig(__config);%n");
      iprint("__query.setElements(%1$s);%n", m.getElementsParameterName());
      iprint(
          "__query.setSqlFilePath(\"%1$s\");%n",
          SqlFileUtil.buildPath(
              daoMeta.getDaoElement().getQualifiedName().toString(), m.getName()));
      iprint("__query.setParameterName(\"%1$s\");%n", m.getElementsParameterName());
      iprint("__query.setCallerClassName(\"%1$s\");%n", canonicalName);
      iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
      iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
      iprint("__query.setBatchSize(%1$s);%n", m.getBatchSize());
      iprint(
          "__query.setSqlLogType(%1$s.%2$s);%n",
          m.getSqlLogType().getClass().getName(), m.getSqlLogType());

      if (m.getEntityType() != null) {
        iprint(
            "__query.setEntityType(%1$s.getSingletonInternal());%n",
            m.getEntityType().getMetaTypeName());
      }

      Boolean ignoreVersion = m.getIgnoreVersion();
      if (ignoreVersion != null) {
        iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
      }

      List<String> include = m.getInclude();
      if (include != null) {
        iprint("__query.setIncludedPropertyNames(%1$s);%n", toCSVFormat(include));
      }

      List<String> exclude = m.getExclude();
      if (exclude != null) {
        iprint("__query.setExcludedPropertyNames(%1$s);%n", toCSVFormat(exclude));
      }

      Boolean suppressOptimisticLockException = m.getSuppressOptimisticLockException();
      if (suppressOptimisticLockException != null) {
        iprint(
            "__query.setOptimisticLockExceptionSuppressed(%1$s);%n",
            suppressOptimisticLockException);
      }

      iprint("__query.prepare();%n");
      iprint(
          "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
          /* 1 */ m.getCommandClass().getName(),
          /* 2 */ m.getCommandClass().getSimpleName(),
          /* 3 */ methodName);

      EntityCtType entityCtType = m.getEntityType();
      if (entityCtType != null && entityCtType.isImmutable()) {
        iprint("int[] __counts = __command.execute();%n");
        iprint("__query.complete();%n");
        iprint(
            "%1$s __result = new %1$s(__counts, __query.getEntities());%n",
            m.getReturnMeta().getTypeName());
      } else {
        iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getTypeName());
        iprint("__query.complete();%n");
      }

      iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", canonicalName, m.getName());
      iprint("return __result;%n");

      printThrowingStatements(m);
      return null;
    }

    @Override
    public Void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m, String methodName) {
      printEnteringStatements(m);
      printPrerequisiteStatements(m);

      QueryReturnMeta returnMeta = m.getReturnMeta();
      iprint(
          "%1$s<%2$s> __query = getQueryImplementors().create%3$s(%4$s);%n",
          /* 1 */ m.getQueryClass().getName(),
          /* 2 */ returnMeta.getBoxedTypeName(),
          /* 3 */ m.getQueryClass().getSimpleName(),
          /* 4 */ methodName);
      iprint("__query.setMethod(%1$s);%n", methodName);
      iprint("__query.setConfig(__config);%n");
      iprint("__query.setCatalogName(\"%1$s\");%n", m.getCatalogName());
      iprint("__query.setSchemaName(\"%1$s\");%n", m.getSchemaName());
      iprint("__query.setFunctionName(\"%1$s\");%n", m.getFunctionName());
      iprint("__query.setQuoteRequired(%1$s);%n", m.isQuoteRequired());
      CallableSqlParameterStatementGenerator parameterGenerator =
          new CallableSqlParameterStatementGenerator();
      m.getResultParameterMeta().accept(parameterGenerator, m);
      for (CallableSqlParameterMeta parameterMeta : m.getCallableSqlParameterMetas()) {
        parameterMeta.accept(parameterGenerator, m);
      }
      iprint("__query.setCallerClassName(\"%1$s\");%n", canonicalName);
      iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
      iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
      iprint(
          "__query.setSqlLogType(%1$s.%2$s);%n",
          m.getSqlLogType().getClass().getName(), m.getSqlLogType());
      iprint("__query.prepare();%n");
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%3$s(%4$s, __query);%n",
          /* 1 */ m.getCommandClass().getName(),
          /* 2 */ returnMeta.getBoxedTypeName(),
          /* 3 */ m.getCommandClass().getSimpleName(),
          /* 4 */ methodName);
      iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
      iprint("__query.complete();%n");
      iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", canonicalName, m.getName());
      iprint("return __result;%n");

      printThrowingStatements(m);
      return null;
    }

    @Override
    public Void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m, String methodName) {
      printEnteringStatements(m);
      printPrerequisiteStatements(m);

      iprint(
          "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
          m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
      iprint("__query.setMethod(%1$s);%n", methodName);
      iprint("__query.setConfig(__config);%n");
      iprint("__query.setCatalogName(\"%1$s\");%n", m.getCatalogName());
      iprint("__query.setSchemaName(\"%1$s\");%n", m.getSchemaName());
      iprint("__query.setProcedureName(\"%1$s\");%n", m.getProcedureName());
      iprint("__query.setQuoteRequired(%1$s);%n", m.isQuoteRequired());
      CallableSqlParameterStatementGenerator parameterGenerator =
          new CallableSqlParameterStatementGenerator();
      for (CallableSqlParameterMeta parameterMeta : m.getCallableSqlParameterMetas()) {
        parameterMeta.accept(parameterGenerator, m);
      }
      iprint("__query.setCallerClassName(\"%1$s\");%n", canonicalName);
      iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
      iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
      iprint(
          "__query.setSqlLogType(%1$s.%2$s);%n",
          m.getSqlLogType().getClass().getName(), m.getSqlLogType());
      iprint("__query.prepare();%n");
      iprint(
          "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
          /* 1 */ m.getCommandClass().getName(),
          /* 2 */ m.getCommandClass().getSimpleName(),
          /* 3 */ methodName);
      iprint("__command.execute();%n");
      iprint("__query.complete();%n");
      iprint("exiting(\"%1$s\", \"%2$s\", null);%n", canonicalName, m.getName());

      printThrowingStatements(m);
      return null;
    }

    @Override
    public Void visitAbstractCreateQueryMeta(AbstractCreateQueryMeta m, String methodName) {
      printEnteringStatements(m);
      printPrerequisiteStatements(m);

      QueryReturnMeta resultMeta = m.getReturnMeta();
      iprint(
          "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
          /* 1 */ m.getQueryClass().getName(),
          /* 2 */ m.getQueryClass().getSimpleName(),
          /* 3 */ methodName);
      iprint("__query.setMethod(%1$s);%n", methodName);
      iprint("__query.setConfig(__config);%n");
      iprint("__query.setCallerClassName(\"%1$s\");%n", canonicalName);
      iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
      iprint("__query.prepare();%n");
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%3$s(%4$s, __query);%n",
          /* 1 */ m.getCommandClass().getName(),
          /* 2 */ resultMeta.getTypeName(),
          /* 3 */ m.getCommandClass().getSimpleName(),
          /* 4 */ methodName);
      iprint("%1$s __result = __command.execute();%n", resultMeta.getTypeName());
      iprint("__query.complete();%n");
      iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", canonicalName, m.getName());
      iprint("return __result;%n");

      printThrowingStatements(m);
      return null;
    }

    @Override
    public Void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m, String methodName) {
      printArrayCreateEnteringStatements(m);
      printPrerequisiteStatements(m);

      QueryReturnMeta resultMeta = m.getReturnMeta();
      iprint(
          "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
          /* 1 */ m.getQueryClass().getName(),
          /* 2 */ m.getQueryClass().getSimpleName(),
          /* 3 */ methodName);
      iprint("__query.setMethod(%1$s);%n", methodName);
      iprint("__query.setConfig(__config);%n");
      iprint("__query.setCallerClassName(\"%1$s\");%n", canonicalName);
      iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
      iprint("__query.setTypeName(\"%1$s\");%n", m.getArrayTypeName());
      iprint("__query.setElements(%1$s);%n", m.getParameterName());
      iprint("__query.prepare();%n");
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%3$s(%4$s, __query);%n",
          /* 1 */ m.getCommandClass().getName(),
          /* 2 */ resultMeta.getBoxedTypeName(),
          /* 3 */ m.getCommandClass().getSimpleName(),
          /* 4 */ methodName);
      iprint("%1$s __result = __command.execute();%n", resultMeta.getTypeName());
      iprint("__query.complete();%n");
      iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", canonicalName, m.getName());
      iprint("return __result;%n");

      printThrowingStatements(m);
      return null;
    }

    @Override
    public Void visitDefaultQueryMeta(DefaultQueryMeta m, String methodName) {
      printEnteringStatements(m);

      QueryReturnMeta resultMeta = m.getReturnMeta();
      if ("void".equals(resultMeta.getTypeName())) {
        iprint("Object __result = null;%n");
        iprint("");
      } else {
        iprint("%1$s __result = ", resultMeta.getTypeName());
      }
      print("%1$s.super.%2$s(", daoMeta.getDaoElement().getQualifiedName(), m.getName());
      for (Iterator<QueryParameterMeta> it = m.getParameterMetas().iterator(); it.hasNext(); ) {
        QueryParameterMeta parameterMeta = it.next();
        print("%1$s", parameterMeta.getName());
        if (it.hasNext()) {
          print(", ");
        }
      }
      print(");%n");
      iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", canonicalName, m.getName());
      if (!"void".equals(resultMeta.getTypeName())) {
        iprint("return __result;%n");
      }

      printThrowingStatements(m);
      return null;
    }

    @Override
    public Void visitSqlProcessorQueryMeta(SqlProcessorQueryMeta m, String methodName) {
      printEnteringStatements(m);
      printPrerequisiteStatements(m);

      iprint(
          "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
          m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
      iprint("__query.setMethod(%1$s);%n", methodName);
      iprint("__query.setConfig(__config);%n");
      iprint(
          "__query.setSqlFilePath(\"%1$s\");%n",
          SqlFileUtil.buildPath(
              daoMeta.getDaoElement().getQualifiedName().toString(), m.getName()));

      printAddParameterStatements(m.getParameterMetas());

      iprint("__query.setCallerClassName(\"%1$s\");%n", canonicalName);
      iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
      iprint("__query.prepare();%n");

      QueryReturnMeta returnMeta = m.getReturnMeta();
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%3$s(%4$s, __query, %5$s);%n",
          /* 1 */ m.getCommandClass().getName(),
          /* 2 */ m.getBiFunctionCtType().getResultCtType().getBoxedTypeName(),
          /* 3 */ m.getCommandClass().getSimpleName(),
          /* 4 */ methodName, /* 5 */
          m.getBiFunctionParameterName());

      if ("void".equals(returnMeta.getTypeName())) {
        iprint("__command.execute();%n");
        iprint("__query.complete();%n");
        iprint("exiting(\"%1$s\", \"%2$s\", null);%n", canonicalName, m.getName());
      } else {
        iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
        iprint("__query.complete();%n");
        iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", canonicalName, m.getName());
        iprint("return __result;%n");
      }

      printThrowingStatements(m);
      return null;
    }

    protected void printEnteringStatements(QueryMeta m) {
      iprint("entering(\"%1$s\", \"%2$s\"", canonicalName, m.getName());
      for (Iterator<QueryParameterMeta> it = m.getParameterMetas().iterator(); it.hasNext(); ) {
        QueryParameterMeta parameterMeta = it.next();
        print(", %1$s", parameterMeta.getName());
      }
      print(");%n");
      iprint("try {%n");
      indent();
    }

    protected void printArrayCreateEnteringStatements(ArrayCreateQueryMeta m) {
      iprint(
          "entering(\"%1$s\", \"%2$s\", (Object)%3$s);%n",
          canonicalName, m.getName(), m.getParameterName());
      iprint("try {%n");
      indent();
    }

    protected void printThrowingStatements(QueryMeta m) {
      unindent();
      iprint("} catch (%1$s __e) {%n", RuntimeException.class.getName());
      indent();
      iprint("throwing(\"%1$s\", \"%2$s\", __e);%n", canonicalName, m.getName());
      iprint("throw __e;%n");
      unindent();
      iprint("}%n");
    }

    protected void printPrerequisiteStatements(QueryMeta m) {
      for (Iterator<QueryParameterMeta> it = m.getParameterMetas().iterator(); it.hasNext(); ) {
        QueryParameterMeta parameterMeta = it.next();
        if (parameterMeta.isNullable()) {
          continue;
        }
        String paramName = parameterMeta.getName();
        iprint("if (%1$s == null) {%n", paramName);
        iprint(
            "    throw new %1$s(\"%2$s\");%n", DomaNullPointerException.class.getName(), paramName);
        iprint("}%n");
      }
    }

    protected void printAddParameterStatements(List<QueryParameterMeta> ParameterMetas) {
      for (Iterator<QueryParameterMeta> it = ParameterMetas.iterator(); it.hasNext(); ) {
        QueryParameterMeta parameterMeta = it.next();
        if (parameterMeta.isBindable()) {
          CtType ctType = parameterMeta.getCtType();
          ctType.accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
                  iprint(
                      "__query.addParameter(\"%1$s\", %2$s.class, %1$s);%n",
                      /* 1 */ parameterMeta.getName(), /* 2 */ ctType.getQualifiedName());
                  return null;
                }

                @Override
                public Void visitOptionalCtType(OptionalCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(\"%1$s\", %2$s.class, %1$s.orElse(null));%n",
                      /* 1 */ parameterMeta.getName(),
                      /* 2 */ ctType.getElementCtType().getQualifiedName());
                  return null;
                }

                @Override
                public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(\"%1$s\", %2$s.class, %1$s.isPresent() ? %1$s.getAsInt() : null);%n",
                      /* 1 */ parameterMeta.getName(), /* 2 */ Integer.class.getName());
                  return null;
                }

                @Override
                public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(\"%1$s\", %2$s.class, %1$s.isPresent() ? %1$s.getAsLong() : null);%n",
                      /* 1 */ parameterMeta.getName(), /* 2 */ Long.class.getName());
                  return null;
                }

                @Override
                public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(\"%1$s\", %2$s.class, %1$s.isPresent() ? %1$s.getAsDouble() : null);%n",
                      /* 1 */ parameterMeta.getName(), /* 2 */ Double.class.getName());
                  return null;
                }
              },
              null);
        }
      }
    }
  }

  /**
   * {@link ProcedureQuery } や {@link FunctionQuery} のパラメータに関するコードを生成します。
   *
   * @author nakamura-to
   */
  protected class CallableSqlParameterStatementGenerator
      implements CallableSqlParameterMetaVisitor<Void, AutoModuleQueryMeta> {

    @Override
    public Void visitBasicListParameterMeta(final BasicListParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(%4$s.class), %5$s, \"%5$s\"));%n",
                      /* 1 */ BasicListParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ ctType.getBasicCtType().getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(%3$s::new, %4$s, \"%4$s\"));%n",
                      /* 1 */ BasicListParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ m.getName());
                  return null;
                }
              },
              null);
      return null;
    }

    @Override
    public Void visitDomainListParameterMeta(DomainListParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s, \"%5$s\"));%n",
          /* 1 */ DomainListParameter.class.getName(),
          /* 2 */ basicCtType.getTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitEntityListParameterMeta(EntityListParameterMeta m, AutoModuleQueryMeta p) {
      EntityCtType entityCtType = m.getEntityCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s>(%3$s.getSingletonInternal(), %4$s, \"%4$s\", %5$s));%n",
          /* 1 */ EntityListParameter.class.getName(),
          /* 2 */ entityCtType.getTypeName(),
          /* 3 */ entityCtType.getMetaTypeName(), /* 4 */
          m.getName(),
          /* 5 */ m.getEnsureResultMapping());
      return null;
    }

    @Override
    public Void visitMapListParameterMeta(MapListParameterMeta m, AutoModuleQueryMeta p) {
      MapKeyNamingType namingType = p.getMapKeyNamingType();
      iprint(
          "__query.addParameter(new %1$s(%2$s.%3$s, %4$s, \"%4$s\"));%n",
          /* 1 */ MapListParameter.class.getName(),
          /* 2 */ namingType.getDeclaringClass().getName(),
          /* 3 */ namingType.name(),
          m.getName());
      return null;
    }

    @Override
    public Void visitBasicInOutParameterMeta(
        final BasicInOutParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(%4$s.class), %5$s));%n",
                      /* 1 */ BasicInOutParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ ctType.getBasicCtType().getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(%3$s::new, %4$s));%n",
                      /* 1 */ BasicInOutParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ m.getName());
                  return null;
                }
              },
              null);
      return null;
    }

    @Override
    public Void visitDomainInOutParameterMeta(DomainInOutParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
          /* 1 */ DomainInOutParameter.class.getName(),
          /* 2 */ basicCtType.getBoxedTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitBasicOutParameterMeta(final BasicOutParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(%4$s.class), %5$s));%n",
                      /* 1 */ BasicOutParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ ctType.getBasicCtType().getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(%3$s::new, %4$s));%n",
                      /* 1 */ BasicOutParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ m.getName());
                  return null;
                }
              },
              null);
      return null;
    }

    @Override
    public Void visitDomainOutParameterMeta(DomainOutParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
          /* 1 */ DomainOutParameter.class.getName(),
          /* 2 */ basicCtType.getTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitBasicInParameterMeta(final BasicInParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%5$s>(() -> new %2$s(%3$s.class, %4$s)));%n",
                      /* 1 */ BasicInParameter.class.getName(),
                      /* 2 */ ctType.getTypeName(),
                      /* 3 */ ctType.getBasicCtType().getQualifiedName(),
                      /* 4 */ m.getName(),
                      /* 5 */ ctType.getBasicCtType().getBoxedTypeName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%4$s>(%2$s::new, %3$s));%n",
                      /* 1 */ BasicInParameter.class.getName(),
                      /* 2 */ ctType.getTypeName(),
                      /* 3 */ m.getName(),
                      /* 4 */ ctType.getBasicCtType().getBoxedTypeName());
                  return null;
                }
              },
              null);
      return null;
    }

    @Override
    public Void visitDomainInParameterMeta(DomainInParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
          /* 1 */ DomainInParameter.class.getName(),
          /* 2 */ basicCtType.getTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitBasicResultListParameterMeta(
        BasicResultListParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(() -> new %3$s(%4$s.class)));%n",
                      /* 1 */ BasicResultListParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ ctType.getBasicCtType().getQualifiedName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(%3$s::new));%n",
                      /* 1 */ BasicResultListParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName());
                  return null;
                }
              },
              null);
      return null;
    }

    @Override
    public Void visitDomainResultListParameterMeta(
        DomainResultListParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s, %3$s>(%4$s));%n",
          /* 1 */ DomainResultListParameter.class.getName(),
          /* 2 */ basicCtType.getTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand());
      return null;
    }

    @Override
    public Void visitEntityResultListParameterMeta(
        EntityResultListParameterMeta m, AutoModuleQueryMeta p) {
      EntityCtType entityCtType = m.getEntityCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s>(%3$s.getSingletonInternal(), %4$s));%n",
          /* 1 */ EntityResultListParameter.class.getName(),
          /* 2 */ entityCtType.getTypeName(),
          /* 3 */ entityCtType.getMetaTypeName(),
          /* 4 */ m.getEnsureResultMapping());
      return null;
    }

    @Override
    public Void visitMapResultListParameterMeta(
        MapResultListParameterMeta m, AutoModuleQueryMeta p) {
      MapKeyNamingType namingType = p.getMapKeyNamingType();
      iprint(
          "__query.setResultParameter(new %1$s(%2$s.%3$s));%n",
          /* 1 */ MapResultListParameter.class.getName(),
          /* 2 */ namingType.getDeclaringClass().getName(),
          /* 3 */ namingType.name());
      return null;
    }

    @Override
    public Void visitBasicSingleResultParameterMeta(
        BasicSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      final BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(() -> new %3$s(%4$s.class), false));%n",
                      /* 1 */ BasicSingleResultParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ ctType.getBasicCtType().getQualifiedName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(%3$s::new, %4$s));%n",
                      /* 1 */ BasicSingleResultParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ basicCtType.isPrimitive());
                  return null;
                }
              },
              null);
      return null;
    }

    @Override
    public Void visitDomainSingleResultParameterMeta(
        DomainSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s, %3$s>(%4$s));%n",
          /* 1 */ DomainSingleResultParameter.class.getName(),
          /* 2 */ basicCtType.getTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand());
      return null;
    }

    @Override
    public Void visitOptionalBasicInParameterMeta(
        OptionalBasicInParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%5$s>(() -> new %2$s(%3$s.class), %4$s));%n",
                      /* 1 */ OptionalBasicInParameter.class.getName(),
                      /* 2 */ ctType.getTypeName(),
                      /* 3 */ ctType.getBasicCtType().getQualifiedName(),
                      /* 4 */ m.getName(),
                      /* 5 */ ctType.getBasicCtType().getBoxedTypeName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%4$s>(%2$s::new, %3$s));%n",
                      /* 1 */ OptionalBasicInParameter.class.getName(),
                      /* 2 */ ctType.getTypeName(),
                      /* 3 */ m.getName(),
                      /* 4 */ ctType.getBasicCtType().getBoxedTypeName());
                  return null;
                }
              },
              null);

      return null;
    }

    @Override
    public Void visitOptionalBasicOutParameterMeta(
        OptionalBasicOutParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(%4$s.class), %5$s));%n",
                      /* 1 */ OptionalBasicOutParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ ctType.getBasicCtType().getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(), %4$s));%n",
                      /* 1 */ OptionalBasicOutParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ m.getName());
                  return null;
                }
              },
              null);

      return null;
    }

    @Override
    public Void visitOptionalBasicInOutParameterMeta(
        OptionalBasicInOutParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(%4$s.class), %5$s));%n",
                      /* 1 */ OptionalBasicInOutParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ ctType.getBasicCtType().getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(), %4$s));%n",
                      /* 1 */ OptionalBasicInOutParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ m.getName());
                  return null;
                }
              },
              null);
      return null;
    }

    @Override
    public Void visitOptionalBasicListParameterMeta(
        OptionalBasicListParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(%4$s.class), %5$s, \"%5$s\"));%n",
                      /* 1 */ OptionalBasicListParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ ctType.getBasicCtType().getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(), %4$s, \"%4$s\"));%n",
                      /* 1 */ OptionalBasicListParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ m.getName());
                  return null;
                }
              },
              null);
      return null;
    }

    @Override
    public Void visitOptionalBasicSingleResultParameterMeta(
        OptionalBasicSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      final BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(() -> new %3$s(%4$s.class)));%n",
                      /* 1 */ OptionalBasicSingleResultParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ ctType.getBasicCtType().getQualifiedName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(() -> new %3$s()));%n",
                      /* 1 */ OptionalBasicSingleResultParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName());
                  return null;
                }
              },
              null);
      return null;
    }

    @Override
    public Void visitOptionalBasicResultListParameterMeta(
        OptionalBasicResultListParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(() -> new %3$s(%4$s.class)));%n",
                      /* 1 */ OptionalBasicResultListParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ ctType.getBasicCtType().getQualifiedName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(() -> new %3$s()));%n",
                      /* 1 */ OptionalBasicResultListParameter.class.getName(),
                      /* 2 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName());
                  return null;
                }
              },
              null);
      return null;
    }

    @Override
    public Void visitOptionalDomainInParameterMeta(
        OptionalDomainInParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
          /* 1 */ OptionalDomainInParameter.class.getName(),
          /* 2 */ basicCtType.getTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalDomainOutParameterMeta(
        OptionalDomainOutParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
          /* 1 */ OptionalDomainOutParameter.class.getName(),
          /* 2 */ basicCtType.getTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalDomainInOutParameterMeta(
        OptionalDomainInOutParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
          /* 1 */ OptionalDomainInOutParameter.class.getName(),
          /* 2 */ basicCtType.getBoxedTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalDomainListParameterMeta(
        OptionalDomainListParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s, \"%5$s\"));%n",
          /* 1 */ OptionalDomainListParameter.class.getName(),
          /* 2 */ basicCtType.getTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalDomainSingleResultParameterMeta(
        OptionalDomainSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s, %3$s>(%4$s));%n",
          /* 1 */ OptionalDomainSingleResultParameter.class.getName(),
          /* 2 */ basicCtType.getTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand());
      return null;
    }

    @Override
    public Void visitOptionalDomainResultListParameterMeta(
        OptionalDomainResultListParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s, %3$s>(%4$s));%n",
          /* 1 */ OptionalDomainResultListParameter.class.getName(),
          /* 2 */ basicCtType.getTypeName(),
          /* 3 */ domainCtType.getTypeName(),
          /* 4 */ domainCtType.getInstantiationCommand());
      return null;
    }

    @Override
    public Void visitOptionalIntInOutParameterMeta(
        OptionalIntInOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalIntInOutParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalIntInParameterMeta(
        OptionalIntInParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalIntInParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalIntListParameterMeta(
        OptionalIntListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s, \"%2$s\"));%n",
          /* 1 */ OptionalIntListParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }

    public Void visitOptionalIntOutParameterMeta(
        OptionalIntOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalIntOutParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalIntSingleResultParameterMeta(
        OptionalIntSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalIntSingleResultParameter.class.getName());
      return null;
    }

    @Override
    public Void visitOptionalIntResultListParameterMeta(
        OptionalIntResultListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalIntResultListParameter.class.getName());
      return null;
    }

    @Override
    public Void visitOptionalLongOutParameterMeta(
        OptionalLongOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalLongOutParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalLongSingleResultParameterMeta(
        OptionalLongSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalLongSingleResultParameter.class.getName());
      return null;
    }

    @Override
    public Void visitOptionalLongResultListParameterMeta(
        OptionalLongResultListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalLongResultListParameter.class.getName());
      return null;
    }

    @Override
    public Void visitOptionalLongInOutParameterMeta(
        OptionalLongInOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalLongInOutParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalLongInParameterMeta(
        OptionalLongInParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalLongInParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalLongListParameterMeta(
        OptionalLongListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s, \"%2$s\"));%n",
          /* 1 */ OptionalLongListParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }

    public Void visitOptionalDoubleOutParameterMeta(
        OptionalDoubleOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalDoubleOutParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalDoubleSingleResultParameterMeta(
        OptionalDoubleSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalDoubleSingleResultParameter.class.getName());
      return null;
    }

    @Override
    public Void visitOptionalDoubleResultListParameterMeta(
        OptionalDoubleResultListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalDoubleResultListParameter.class.getName());
      return null;
    }

    @Override
    public Void visitOptionalDoubleInOutParameterMeta(
        OptionalDoubleInOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalDoubleInOutParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalDoubleInParameterMeta(
        OptionalDoubleInParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalDoubleInParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalDoubleListParameterMeta(
        OptionalDoubleListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s, \"%2$s\"));%n",
          /* 1 */ OptionalDoubleListParameter.class.getName(), /* 2 */ m.getName());
      return null;
    }
  }

  protected class SqlFileSelectQueryFunctionCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected final SqlFileSelectQueryMeta m;

    protected final String methodName;

    protected final QueryReturnMeta resultMeta;

    protected final String commandClassName;

    protected final String commandName;

    protected final String functionParamName;

    public SqlFileSelectQueryFunctionCtTypeVisitor(SqlFileSelectQueryMeta m, String methodName) {
      this.m = m;
      this.methodName = methodName;
      this.resultMeta = m.getReturnMeta();
      this.commandClassName = m.getCommandClass().getName();
      this.commandName = m.getCommandClass().getSimpleName();
      this.functionParamName = m.getFunctionParameterName();
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Void p) throws RuntimeException {
      ctType
          .getElementCtType()
          .accept(
              new StreamElementCtTypeVisitor(
                  m,
                  methodName,
                  resultMeta.getBoxedTypeName(),
                  commandClassName,
                  commandName,
                  functionParamName),
              false);
      return null;
    }
  }

  protected class StreamElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    protected final SqlFileSelectQueryMeta m;

    protected final String methodName;

    protected final String resultBoxedTypeName;

    protected final String commandClassName;

    protected final String commandName;

    protected final String functionParamName;

    public StreamElementCtTypeVisitor(
        SqlFileSelectQueryMeta m,
        String methodName,
        String resultBoxedTypeName,
        String commandClassName,
        String commandName,
        String functionParamName) {
      this.m = m;
      this.methodName = methodName;
      this.resultBoxedTypeName = resultBoxedTypeName;
      this.commandClassName = commandClassName;
      this.commandName = commandName;
      this.functionParamName = functionParamName;
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, final Boolean optional)
        throws RuntimeException {
      ctType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%8$s(%9$s, __query, new %3$s<%4$s, %2$s>(() -> new %5$s(%6$s.class), %7$s));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultBoxedTypeName,
                      /* 3 */ getBasicStreamHandlerName(optional),
                      /* 4 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 5 */ ctType.getTypeName(),
                      /* 6 */ ctType.getBasicCtType().getQualifiedName(),
                      /* 7 */ functionParamName,
                      /* 8 */ commandName, /* 9 */
                      methodName);
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%4$s, %2$s>(%5$s::new, %6$s));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultBoxedTypeName,
                      /* 3 */ getBasicStreamHandlerName(optional),
                      /* 4 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 5 */ ctType.getTypeName(),
                      /* 6 */ functionParamName,
                      /* 7 */ commandName, /* 8 */
                      methodName);
                  return null;
                }
              },
              null);

      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Boolean optional) throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%9$s, %4$s, %2$s>(%5$s, %6$s));%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ getDomainStreamHandlerName(optional),
          /* 4 */ ctType.getBoxedTypeName(),
          /* 5 */ ctType.getInstantiationCommand(),
          /* 6 */ functionParamName, /* 7 */
          commandName,
          /* 8 */ methodName,
          /* 9 */ ctType.getBasicCtType().getBoxedTypeName());
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Boolean optional) throws RuntimeException {
      MapKeyNamingType namingType = m.getMapKeyNamingType();
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%2$s>(%4$s.%5$s, %6$s));%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ getMapStreamHandlerName(optional),
          /* 4 */ namingType.getDeclaringClass().getName(),
          /* 5 */ namingType.name(), /* 6 */
          functionParamName,
          /* 7 */ commandName, /* 8 */
          methodName);
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Boolean optional) throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%4$s, %2$s>(%5$s.getSingletonInternal(), %6$s));%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ getEntityStreamHandlerName(optional),
          /* 4 */ ctType.getTypeName(),
          /* 5 */ ctType.getMetaTypeName(), /* 6 */
          functionParamName,
          /* 7 */ commandName, /* 8 */
          methodName);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Boolean optional)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, true);
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ OptionalIntStreamHandler.class.getName(),
          /* 4 */ functionParamName, /* 5 */
          commandName,
          /* 6 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ OptionalLongStreamHandler.class.getName(),
          /* 4 */ functionParamName, /* 5 */
          commandName,
          /* 6 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ OptionalDoubleStreamHandler.class.getName(),
          /* 4 */ functionParamName, /* 5 */
          commandName,
          /* 6 */ methodName);
      return null;
    }

    protected String getBasicStreamHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicStreamHandler.class.getName();
      }
      return BasicStreamHandler.class.getName();
    }

    protected String getDomainStreamHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainStreamHandler.class.getName();
      }
      return DomainStreamHandler.class.getName();
    }

    protected String getMapStreamHandlerName(Boolean optional) {
      return MapStreamHandler.class.getName();
    }

    protected String getEntityStreamHandlerName(Boolean optional) {
      return EntityStreamHandler.class.getName();
    }
  }

  protected class SqlFileSelectQueryCollectorCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    protected final SqlFileSelectQueryMeta m;

    protected final String methodName;

    protected final QueryReturnMeta resultMeta;

    protected final String commandClassName;

    protected final String commandName;

    protected final String collectorParamName;

    public SqlFileSelectQueryCollectorCtTypeVisitor(SqlFileSelectQueryMeta m, String methodName) {
      this.m = m;
      this.methodName = methodName;
      this.resultMeta = m.getReturnMeta();
      this.commandClassName = m.getCommandClass().getName();
      this.commandName = m.getCommandClass().getSimpleName();
      this.collectorParamName = m.getCollectorParameterName();
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, final Boolean optional)
        throws RuntimeException {
      ctType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%8$s(%9$s, __query, new %3$s<%4$s, %2$s>(() -> new %5$s(%6$s.class), %7$s));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultMeta.getBoxedTypeName(),
                      /* 3 */ getBasicCollectorHandlerName(optional),
                      /* 4 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 5 */ ctType.getTypeName(),
                      /* 6 */ ctType.getBasicCtType().getQualifiedName(),
                      /* 7 */ collectorParamName,
                      /* 8 */ commandName, /* 9 */
                      methodName);
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%4$s, %2$s>(%5$s::new, %6$s));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultMeta.getBoxedTypeName(),
                      /* 3 */ getBasicCollectorHandlerName(optional),
                      /* 4 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 5 */ ctType.getTypeName(),
                      /* 6 */ collectorParamName,
                      /* 7 */ commandName, /* 8 */
                      methodName);
                  return null;
                }
              },
              null);

      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Boolean optional) throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%9$s, %4$s, %2$s>(%5$s, %6$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ resultMeta.getBoxedTypeName(),
          /* 3 */ getDomainCollectorHandlerName(optional),
          /* 4 */ ctType.getBoxedTypeName(),
          /* 5 */ ctType.getInstantiationCommand(),
          /* 6 */ collectorParamName, /* 7 */
          commandName,
          /* 8 */ methodName,
          /* 9 */ ctType.getBasicCtType().getBoxedTypeName());
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Boolean optional) throws RuntimeException {
      MapKeyNamingType namingType = m.getMapKeyNamingType();
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%2$s>(%4$s.%5$s, %6$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ resultMeta.getBoxedTypeName(),
          /* 3 */ getMapCollectorHandlerName(optional),
          /* 4 */ namingType.getDeclaringClass().getName(),
          /* 5 */ namingType.name(), /* 6 */
          collectorParamName,
          /* 7 */ commandName, /* 8 */
          methodName);
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Boolean optional) throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%4$s, %2$s>(%5$s.getSingletonInternal(), %6$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ resultMeta.getBoxedTypeName(),
          /* 3 */ getEntityCollectorHandlerName(optional),
          /* 4 */ ctType.getTypeName(),
          /* 5 */ ctType.getMetaTypeName(), /* 6 */
          collectorParamName,
          /* 7 */ commandName, /* 8 */
          methodName);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Boolean optional)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, true);
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ resultMeta.getBoxedTypeName(),
          /* 3 */ OptionalIntCollectorHandler.class.getName(),
          /* 4 */ collectorParamName, /* 5 */
          commandName,
          /* 6 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ resultMeta.getBoxedTypeName(),
          /* 3 */ OptionalLongCollectorHandler.class.getName(),
          /* 4 */ collectorParamName, /* 5 */
          commandName,
          /* 6 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ resultMeta.getBoxedTypeName(),
          /* 3 */ OptionalDoubleCollectorHandler.class.getName(),
          /* 4 */ collectorParamName, /* 5 */
          commandName,
          /* 6 */ methodName);
      return null;
    }

    protected String getBasicCollectorHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicCollectorHandler.class.getName();
      }
      return BasicCollectorHandler.class.getName();
    }

    protected String getDomainCollectorHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainCollectorHandler.class.getName();
      }
      return DomainCollectorHandler.class.getName();
    }

    protected String getMapCollectorHandlerName(Boolean optional) {
      return MapCollectorHandler.class.getName();
    }

    protected String getEntityCollectorHandlerName(Boolean optional) {
      return EntityCollectorHandler.class.getName();
    }
  }

  /**
   * {@link SqlFileSelectQuery} に関して、 戻り値に関するコードを生成します。
   *
   * @author nakamura-to
   */
  protected class SqlFileSelectQueryReturnCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    protected final SqlFileSelectQueryMeta m;

    protected final String methodName;

    protected final String resultBoxedTypeName;

    protected final String commandClassName;

    protected final String commandName;

    protected SqlFileSelectQueryReturnCtTypeVisitor(SqlFileSelectQueryMeta m, String methodName) {
      this.m = m;
      this.methodName = methodName;
      this.resultBoxedTypeName = this.m.getReturnMeta().getBoxedTypeName();
      this.commandClassName = m.getCommandClass().getName();
      this.commandName = m.getCommandClass().getSimpleName();
    }

    @Override
    public Void visitBasicCtType(final BasicCtType basicCtType, Boolean optional)
        throws RuntimeException {
      basicCtType
          .getWrapperCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%6$s>(() -> new %4$s(%5$s.class), false));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultBoxedTypeName,
                      /* 3 */ getBasicSingleResultHandlerName(optional),
                      /* 4 */ ctType.getTypeName(),
                      /* 5 */ ctType.getBasicCtType().getQualifiedName(),
                      /* 6 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 7 */ commandName, /* 8 */
                      methodName);
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%6$s>(%4$s::new, %5$s));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultBoxedTypeName,
                      /* 3 */ getBasicSingleResultHandlerName(optional),
                      /* 4 */ ctType.getTypeName(),
                      /* 5 */ basicCtType.isPrimitive(),
                      /* 6 */ ctType.getBasicCtType().getBoxedTypeName(),
                      /* 7 */ commandName, /* 8 */
                      methodName);
                  return null;
                }
              },
              null);

      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Boolean optional) throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%8$s, %5$s>(%4$s));%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ getDomainSingleResultHandlerName(optional),
          /* 4 */ ctType.getInstantiationCommand(),
          /* 5 */ ctType.getBoxedTypeName(), /* 6 */
          commandName,
          /* 7 */ methodName,
          /* 8 */ ctType.getBasicCtType().getBoxedTypeName());
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Boolean optional) throws RuntimeException {
      MapKeyNamingType namingType = m.getMapKeyNamingType();
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s(%4$s.%5$s));%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ getMapSingleResultHandlerName(optional),
          /* 4 */ namingType.getDeclaringClass().getName(),
          /* 5 */ namingType.name(), /* 6 */
          commandName,
          /* 7 */ methodName);
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Boolean optional) throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%5$s>(%4$s.getSingletonInternal()));%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ getEntitySingleResultHandlerName(optional),
          /* 4 */ ctType.getMetaTypeName(),
          /* 5 */ ctType.getTypeName(), /* 6 */
          commandName,
          /* 7 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Boolean optional)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, true);
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ OptionalIntSingleResultHandler.class.getName(),
          /* 4 */ commandName, /* 5 */
          methodName);
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ OptionalLongSingleResultHandler.class.getName(),
          /* 4 */ commandName, /* 5 */
          methodName);
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
          /* 1 */ commandClassName, /* 2 */
          resultBoxedTypeName,
          /* 3 */ OptionalDoubleSingleResultHandler.class.getName(),
          /* 4 */ commandName, /* 5 */
          methodName);
      return null;
    }

    @Override
    public Void visitIterableCtType(final IterableCtType iterableCtType, final Boolean __)
        throws RuntimeException {
      iterableCtType
          .getElementCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Boolean, RuntimeException>() {

                @Override
                public Void visitBasicCtType(BasicCtType ctType, Boolean optional)
                    throws RuntimeException {
                  ctType
                      .getWrapperCtType()
                      .accept(
                          new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                            @Override
                            public Void visitEnumWrapperCtType(EnumWrapperCtType ctType, Void p)
                                throws RuntimeException {
                              iprint(
                                  "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%4$s>(() -> new %5$s(%6$s.class)));%n",
                                  /* 1 */ commandClassName,
                                  /* 2 */ resultBoxedTypeName,
                                  /* 3 */ getBasicResultListHandlerName(optional),
                                  /* 4 */ ctType.getBasicCtType().getBoxedTypeName(),
                                  /* 5 */ ctType.getTypeName(),
                                  /* 6 */ ctType.getBasicCtType().getQualifiedName(),
                                  /* 7 */ commandName,
                                  /* 8 */ methodName);
                              return null;
                            }

                            @Override
                            public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                                throws RuntimeException {
                              iprint(
                                  "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%4$s>(%5$s::new));%n",
                                  /* 1 */ commandClassName,
                                  /* 2 */ resultBoxedTypeName,
                                  /* 3 */ getBasicResultListHandlerName(optional),
                                  /* 4 */ ctType.getBasicCtType().getBoxedTypeName(),
                                  /* 5 */ ctType.getTypeName(),
                                  /* 6 */ commandName,
                                  /* 7 */ methodName);
                              return null;
                            }
                          },
                          null);

                  return null;
                }

                @Override
                public Void visitDomainCtType(DomainCtType ctType, Boolean optional)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%8$s, %4$s>(%5$s));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultBoxedTypeName,
                      /* 3 */ getDomainResultListHandlerName(optional),
                      /* 4 */ ctType.getBoxedTypeName(),
                      /* 5 */ ctType.getInstantiationCommand(),
                      /* 6 */ commandName, /* 7 */
                      methodName,
                      /* 8 */ ctType.getBasicCtType().getBoxedTypeName());
                  return null;
                }

                @Override
                public Void visitMapCtType(MapCtType ctType, Boolean optional)
                    throws RuntimeException {
                  MapKeyNamingType namingType = m.getMapKeyNamingType();
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s(%4$s.%5$s));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultBoxedTypeName,
                      /* 3 */ getMapResultListHandlerName(optional),
                      /* 4 */ namingType.getDeclaringClass().getName(),
                      /* 5 */ namingType.name(),
                      /* 6 */ commandName, /* 7 */
                      methodName);
                  return null;
                }

                @Override
                public Void visitEntityCtType(EntityCtType ctType, Boolean optional)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%4$s>(%5$s.getSingletonInternal()));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultBoxedTypeName,
                      /* 3 */ getEntityResultListHandlerName(optional),
                      /* 4 */ ctType.getTypeName(),
                      /* 5 */ ctType.getMetaTypeName(),
                      /* 6 */ commandName, /* 7 */
                      methodName);
                  return null;
                }

                @Override
                public Void visitOptionalCtType(OptionalCtType ctType, Boolean __)
                    throws RuntimeException {
                  return ctType.getElementCtType().accept(this, true);
                }

                @Override
                public Void visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultBoxedTypeName,
                      /* 3 */ OptionalIntResultListHandler.class.getName(),
                      /* 4 */ commandName, /* 5 */
                      methodName);
                  return null;
                }

                @Override
                public Void visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultBoxedTypeName,
                      /* 3 */ OptionalLongResultListHandler.class.getName(),
                      /* 4 */ commandName, /* 5 */
                      methodName);
                  return null;
                }

                @Override
                public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultBoxedTypeName,
                      /* 3 */ OptionalDoubleResultListHandler.class.getName(),
                      /* 4 */ commandName, /* 5 */
                      methodName);
                  return null;
                }
              },
              false);
      return null;
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Boolean __) throws RuntimeException {
      ctType
          .getElementCtType()
          .accept(
              new StreamElementCtTypeVisitor(
                  m,
                  methodName,
                  resultBoxedTypeName,
                  commandClassName,
                  commandName,
                  Function.class.getName() + ".identity()"),
              false);
      return null;
    }

    protected String getBasicSingleResultHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicSingleResultHandler.class.getName();
      }
      return BasicSingleResultHandler.class.getName();
    }

    protected String getBasicResultListHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicResultListHandler.class.getName();
      }
      return BasicResultListHandler.class.getName();
    }

    protected String getDomainSingleResultHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainSingleResultHandler.class.getName();
      }
      return DomainSingleResultHandler.class.getName();
    }

    protected String getDomainResultListHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainResultListHandler.class.getName();
      }
      return DomainResultListHandler.class.getName();
    }

    protected String getMapSingleResultHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalMapSingleResultHandler.class.getName();
      }
      return MapSingleResultHandler.class.getName();
    }

    protected String getMapResultListHandlerName(Boolean optional) {
      return MapResultListHandler.class.getName();
    }

    protected String getEntitySingleResultHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalEntitySingleResultHandler.class.getName();
      }
      return EntitySingleResultHandler.class.getName();
    }

    protected String getEntityResultListHandlerName(Boolean optional) {
      return EntityResultListHandler.class.getName();
    }
  }
}
