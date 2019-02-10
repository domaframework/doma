package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import javax.lang.model.type.TypeKind;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.FetchType;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.*;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.parameter.*;
import org.seasar.doma.internal.apt.meta.query.*;
import org.seasar.doma.internal.jdbc.command.*;
import org.seasar.doma.internal.jdbc.sql.*;

public class DaoImplMethodGenerator extends AbstractGenerator implements QueryMetaVisitor<Void> {

  private final DaoMeta daoMeta;

  private final QueryMeta queryMeta;

  private final String methodName;

  public DaoImplMethodGenerator(
      Context ctx,
      ClassName className,
      Printer printer,
      DaoMeta daoMeta,
      QueryMeta queryMeta,
      int index) {
    super(ctx, className, printer);
    assertNotNull(daoMeta, queryMeta);
    this.daoMeta = daoMeta;
    this.queryMeta = queryMeta;
    this.methodName = "__method" + index;
  }

  @Override
  public void generate() {
    printMethod();
  }

  private void printMethod() {
    iprint("@Override%n");
    iprint("public ");
    if (!queryMeta.getTypeParameterNames().isEmpty()) {
      print("<");
      for (Iterator<String> it = queryMeta.getTypeParameterNames().iterator(); it.hasNext(); ) {
        print("%1$s", it.next());
        if (it.hasNext()) {
          print(", ");
        }
      }
      print("> ");
    }
    print("%1$s %2$s(", queryMeta.getReturnMeta().getTypeName(), queryMeta.getName());
    for (Iterator<QueryParameterMeta> it = queryMeta.getParameterMetas().iterator();
        it.hasNext(); ) {
      QueryParameterMeta parameterMeta = it.next();
      String parameterTypeName = parameterMeta.getTypeName();
      if (!it.hasNext() && queryMeta.isVarArgs()) {
        parameterTypeName = parameterTypeName.replace("[]", "...");
      }
      print("%1$s %2$s", parameterTypeName, parameterMeta.getName());
      if (it.hasNext()) {
        print(", ");
      }
    }
    print(") ");
    if (!queryMeta.getThrownTypeNames().isEmpty()) {
      print("throws ");
      for (Iterator<String> it = queryMeta.getThrownTypeNames().iterator(); it.hasNext(); ) {
        print("%1$s", it.next());
        if (it.hasNext()) {
          print(", ");
        }
      }
      print(" ");
    }
    print("{%n");
    indent();
    queryMeta.accept(this);
    unindent();
    iprint("}%n");
    print("%n");
  }

  @Override
  public Void visitSqlFileSelectQueryMeta(final SqlFileSelectQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
        m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__config);%n");
    iprint("__query.setSqlFilePath(\"%1$s\");%n", m.getPath());
    if (m.getSelectOptionsCtType() != null) {
      iprint("__query.setOptions(%1$s);%n", m.getSelectOptionsParameterName());
    }
    if (m.getEntityCtType() != null) {
      iprint("__query.setEntityType(%1$s);%n", m.getEntityCtType().entityDescSingletonCode());
    }

    printAddParameterStatements(m.getParameterMetas());

    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
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
      returnCtType.accept(new SqlFileSelectQueryReturnCtTypeVisitor(m), false);
      iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
      iprint("__query.complete();%n");
      iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
      iprint("return __result;%n");
    } else {
      if (m.getSelectStrategyType() == SelectType.STREAM) {
        FunctionCtType functionCtType = m.getFunctionCtType();
        functionCtType
            .getTargetCtType()
            .accept(new SqlFileSelectQueryFunctionCtTypeVisitor(m), null);
      } else if (m.getSelectStrategyType() == SelectType.COLLECT) {
        CollectorCtType collectorCtType = m.getCollectorCtType();
        collectorCtType
            .getTargetCtType()
            .accept(new SqlFileSelectQueryCollectorCtTypeVisitor(m), false);
      }
      if ("void".equals(returnMeta.getTypeName())) {
        iprint("__command.execute();%n");
        iprint("__query.complete();%n");
        iprint("exiting(\"%1$s\", \"%2$s\", null);%n", className, m.getName());
      } else {
        iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
        iprint("__query.complete();%n");
        iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
        iprint("return __result;%n");
      }
    }

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitSqlFileScriptQueryMeta(SqlFileScriptQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
        m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__config);%n");
    iprint("__query.setScriptFilePath(\"%1$s\");%n", m.getPath());
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
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
    iprint("exiting(\"%1$s\", \"%2$s\", null);%n", className, m.getName());

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitAutoModifyQueryMeta(AutoModifyQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s<%2$s> __query = getQueryImplementors().create%4$s(%5$s, %3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getEntityCtType().getTypeName(),
        /* 3 */ m.getEntityCtType().entityDescSingletonCode(),
        /* 4 */ m.getQueryClass().getSimpleName(),
        /* 5 */ methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__config);%n");
    iprint("__query.setEntity(%1$s);%n", m.getEntityParameterName());
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
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
          "__query.setOptimisticLockExceptionSuppressed(%1$s);%n", suppressOptimisticLockException);
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

    iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getQueryClass().getSimpleName(),
        /* 3 */ methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__config);%n");
    iprint("__query.setSqlFilePath(\"%1$s\");%n", m.getPath());

    printAddParameterStatements(m.getParameterMetas());

    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    iprint(
        "__query.setSqlLogType(%1$s.%2$s);%n",
        m.getSqlLogType().getClass().getName(), m.getSqlLogType());

    if (m.getEntityParameterName() != null && m.getEntityCtType() != null) {
      iprint(
          "__query.setEntityAndEntityType(\"%1$s\", %2$s, %3$s);%n",
          m.getEntityParameterName(),
          m.getEntityParameterName(),
          m.getEntityCtType().entityDescSingletonCode());
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
          "__query.setOptimisticLockExceptionSuppressed(%1$s);%n", suppressOptimisticLockException);
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

    iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s<%2$s> __query = getQueryImplementors().create%4$s(%5$s, %3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getEntityCtType().getTypeName(),
        /* 3 */ m.getEntityCtType().entityDescSingletonCode(),
        /* 4 */ m.getQueryClass().getSimpleName(),
        /* 5 */ methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__config);%n");
    iprint("__query.setEntities(%1$s);%n", m.getEntitiesParameterName());
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
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
          "__query.setOptimisticLockExceptionSuppressed(%1$s);%n", suppressOptimisticLockException);
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

    iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitSqlFileBatchModifyQueryMeta(SqlFileBatchModifyQueryMeta m) {
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
    iprint("__query.setSqlFilePath(\"%1$s\");%n", m.getPath());
    iprint("__query.setParameterName(\"%1$s\");%n", m.getElementsParameterName());
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    iprint("__query.setBatchSize(%1$s);%n", m.getBatchSize());
    iprint(
        "__query.setSqlLogType(%1$s.%2$s);%n",
        m.getSqlLogType().getClass().getName(), m.getSqlLogType());

    if (m.getEntityType() != null) {
      iprint("__query.setEntityType(%1$s);%n", m.getEntityType().entityDescSingletonCode());
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
          "__query.setOptimisticLockExceptionSuppressed(%1$s);%n", suppressOptimisticLockException);
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

    iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m) {
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
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
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
    iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m) {
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
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
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
    iprint("exiting(\"%1$s\", \"%2$s\", null);%n", className, m.getName());

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m) {
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
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
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
    iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitBlobCreateQueryMeta(BlobCreateQueryMeta m) {
    return visitAbstractCreateQueryMeta(m);
  }

  @Override
  public Void visitClobCreateQueryMeta(ClobCreateQueryMeta m) {
    return visitAbstractCreateQueryMeta(m);
  }

  @Override
  public Void visitNClobCreateQueryMeta(NClobCreateQueryMeta m) {
    return visitAbstractCreateQueryMeta(m);
  }

  @Override
  public Void visitSQLXMLCreateQueryMeta(SQLXMLCreateQueryMeta m) {
    return visitAbstractCreateQueryMeta(m);
  }

  private Void visitAbstractCreateQueryMeta(AbstractCreateQueryMeta m) {
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
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
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
    iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitDefaultQueryMeta(DefaultQueryMeta m) {
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
    iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    if (!"void".equals(resultMeta.getTypeName())) {
      iprint("return __result;%n");
    }

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitSqlProcessorQueryMeta(SqlProcessorQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
        m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__config);%n");
    iprint("__query.setSqlFilePath(\"%1$s\");%n", m.getPath());

    printAddParameterStatements(m.getParameterMetas());

    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
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
      iprint("exiting(\"%1$s\", \"%2$s\", null);%n", className, m.getName());
    } else {
      iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
      iprint("__query.complete();%n");
      iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
      iprint("return __result;%n");
    }

    printThrowingStatements(m);
    return null;
  }

  private void printEnteringStatements(QueryMeta m) {
    iprint("entering(\"%1$s\", \"%2$s\"", className, m.getName());
    for (Iterator<QueryParameterMeta> it = m.getParameterMetas().iterator(); it.hasNext(); ) {
      QueryParameterMeta parameterMeta = it.next();
      if (parameterMeta.getType().getKind() != TypeKind.ARRAY) {
        print(", %1$s", parameterMeta.getName());
      } else {
        print(", (Object) %1$s", parameterMeta.getName());
      }
    }
    print(");%n");
    iprint("try {%n");
    indent();
  }

  private void printArrayCreateEnteringStatements(ArrayCreateQueryMeta m) {
    iprint(
        "entering(\"%1$s\", \"%2$s\", (Object)%3$s);%n",
        className, m.getName(), m.getParameterName());
    iprint("try {%n");
    indent();
  }

  private void printThrowingStatements(QueryMeta m) {
    unindent();
    iprint("} catch (%1$s __e) {%n", RuntimeException.class.getName());
    indent();
    iprint("throwing(\"%1$s\", \"%2$s\", __e);%n", className, m.getName());
    iprint("throw __e;%n");
    unindent();
    iprint("}%n");
  }

  private void printPrerequisiteStatements(QueryMeta m) {
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

  private void printAddParameterStatements(List<QueryParameterMeta> ParameterMetas) {
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

  private String toCSVFormat(List<String> values) {
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

  private class CallableSqlParameterStatementGenerator
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
                      /* 2 */ basicCtType.getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ basicCtType.getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(%3$s::new, %4$s, \"%4$s\"));%n",
                      /* 1 */ BasicListParameter.class.getName(),
                      /* 2 */ basicCtType.getBoxedTypeName(),
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
          /* 4 */ domainCtType.domainDescSingletonCode(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitEntityListParameterMeta(EntityListParameterMeta m, AutoModuleQueryMeta p) {
      EntityCtType entityCtType = m.getEntityCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s>(%3$s, %4$s, \"%4$s\", %5$s));%n",
          /* 1 */ EntityListParameter.class.getName(),
          /* 2 */ entityCtType.getTypeName(),
          /* 3 */ entityCtType.entityDescSingletonCode(),
          /* 4 */ m.getName(),
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
                      /* 2 */ basicCtType.getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ basicCtType.getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(%3$s::new, %4$s));%n",
                      /* 1 */ BasicInOutParameter.class.getName(),
                      /* 2 */ basicCtType.getBoxedTypeName(),
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
          /* 4 */ domainCtType.domainDescSingletonCode(),
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
                      /* 2 */ basicCtType.getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ basicCtType.getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(%3$s::new, %4$s));%n",
                      /* 1 */ BasicOutParameter.class.getName(),
                      /* 2 */ basicCtType.getBoxedTypeName(),
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
          /* 4 */ domainCtType.domainDescSingletonCode(),
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
                      /* 3 */ basicCtType.getQualifiedName(),
                      /* 4 */ m.getName(),
                      /* 5 */ basicCtType.getBoxedTypeName());
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
                      /* 4 */ basicCtType.getBoxedTypeName());
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
          /* 4 */ domainCtType.domainDescSingletonCode(),
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
                      /* 2 */ basicCtType.getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ basicCtType.getQualifiedName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(%3$s::new));%n",
                      /* 1 */ BasicResultListParameter.class.getName(),
                      /* 2 */ basicCtType.getBoxedTypeName(),
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
          /* 4 */ domainCtType.domainDescSingletonCode());
      return null;
    }

    @Override
    public Void visitEntityResultListParameterMeta(
        EntityResultListParameterMeta m, AutoModuleQueryMeta p) {
      EntityCtType entityCtType = m.getEntityCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s>(%3$s, %4$s));%n",
          /* 1 */ EntityResultListParameter.class.getName(),
          /* 2 */ entityCtType.getTypeName(),
          /* 3 */ entityCtType.entityDescSingletonCode(),
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
                      /* 2 */ basicCtType.getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ basicCtType.getQualifiedName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(%3$s::new, %4$s));%n",
                      /* 1 */ BasicSingleResultParameter.class.getName(),
                      /* 2 */ basicCtType.getBoxedTypeName(),
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
          /* 4 */ domainCtType.domainDescSingletonCode());
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
                      /* 3 */ basicCtType.getQualifiedName(),
                      /* 4 */ m.getName(),
                      /* 5 */ basicCtType.getBoxedTypeName());
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
                      /* 4 */ basicCtType.getBoxedTypeName());
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
                      /* 2 */ basicCtType.getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ basicCtType.getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(), %4$s));%n",
                      /* 1 */ OptionalBasicOutParameter.class.getName(),
                      /* 2 */ basicCtType.getBoxedTypeName(),
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
                      /* 2 */ basicCtType.getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ basicCtType.getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(), %4$s));%n",
                      /* 1 */ OptionalBasicInOutParameter.class.getName(),
                      /* 2 */ basicCtType.getBoxedTypeName(),
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
                      /* 2 */ basicCtType.getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ basicCtType.getQualifiedName(),
                      /* 5 */ m.getName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.addParameter(new %1$s<%2$s>(() -> new %3$s(), %4$s, \"%4$s\"));%n",
                      /* 1 */ OptionalBasicListParameter.class.getName(),
                      /* 2 */ basicCtType.getBoxedTypeName(),
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
                      /* 2 */ basicCtType.getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ basicCtType.getQualifiedName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(() -> new %3$s()));%n",
                      /* 1 */ OptionalBasicSingleResultParameter.class.getName(),
                      /* 2 */ basicCtType.getBoxedTypeName(),
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
                      /* 2 */ basicCtType.getBoxedTypeName(),
                      /* 3 */ ctType.getTypeName(),
                      /* 4 */ basicCtType.getQualifiedName());
                  return null;
                }

                @Override
                public Void visitWrapperCtType(WrapperCtType ctType, Void p)
                    throws RuntimeException {
                  iprint(
                      "__query.setResultParameter(new %1$s<%2$s>(() -> new %3$s()));%n",
                      /* 1 */ OptionalBasicResultListParameter.class.getName(),
                      /* 2 */ basicCtType.getBoxedTypeName(),
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
          /* 4 */ domainCtType.domainDescSingletonCode(),
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
          /* 4 */ domainCtType.domainDescSingletonCode(),
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
          /* 4 */ domainCtType.domainDescSingletonCode(),
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
          /* 4 */ domainCtType.domainDescSingletonCode(),
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
          /* 4 */ domainCtType.domainDescSingletonCode());
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
          /* 4 */ domainCtType.domainDescSingletonCode());
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

  private class SqlFileSelectQueryFunctionCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected final SqlFileSelectQueryMeta m;

    protected final QueryReturnMeta resultMeta;

    protected final String commandClassName;

    protected final String commandName;

    protected final String functionParamName;

    public SqlFileSelectQueryFunctionCtTypeVisitor(SqlFileSelectQueryMeta m) {
      this.m = m;
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
                  resultMeta.getBoxedTypeName(),
                  commandClassName,
                  commandName,
                  functionParamName),
              false);
      return null;
    }
  }

  private class StreamElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    protected final SqlFileSelectQueryMeta m;

    protected final String resultBoxedTypeName;

    protected final String commandClassName;

    protected final String commandName;

    protected final String functionParamName;

    public StreamElementCtTypeVisitor(
        SqlFileSelectQueryMeta m,
        String resultBoxedTypeName,
        String commandClassName,
        String commandName,
        String functionParamName) {
      this.m = m;
      this.resultBoxedTypeName = resultBoxedTypeName;
      this.commandClassName = commandClassName;
      this.commandName = commandName;
      this.functionParamName = functionParamName;
    }

    @Override
    public Void visitBasicCtType(BasicCtType basicCtType, final Boolean optional)
        throws RuntimeException {
      basicCtType
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
                      /* 4 */ basicCtType.getBoxedTypeName(),
                      /* 5 */ ctType.getTypeName(),
                      /* 6 */ basicCtType.getQualifiedName(),
                      /* 7 */ functionParamName,
                      /* 8 */ commandName,
                      /* 9 */ methodName);
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
                      /* 4 */ basicCtType.getBoxedTypeName(),
                      /* 5 */ ctType.getTypeName(),
                      /* 6 */ functionParamName,
                      /* 7 */ commandName,
                      /* 8 */ methodName);
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
          /* 2 */ resultBoxedTypeName,
          /* 3 */ getDomainStreamHandlerName(optional),
          /* 4 */ ctType.getBoxedTypeName(),
          /* 5 */ ctType.domainDescSingletonCode(),
          /* 6 */ functionParamName,
          /* 7 */ commandName,
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
          "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%4$s, %2$s>(%5$s, %6$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ resultBoxedTypeName,
          /* 3 */ getEntityStreamHandlerName(optional),
          /* 4 */ ctType.getTypeName(),
          /* 5 */ ctType.entityDescSingletonCode(),
          /* 6 */ functionParamName,
          /* 7 */ commandName,
          /* 8 */ methodName);
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
          /* 2 */ resultBoxedTypeName,
          /* 3 */ OptionalIntStreamHandler.class.getName(),
          /* 4 */ functionParamName,
          /* 5 */ commandName,
          /* 6 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ resultBoxedTypeName,
          /* 3 */ OptionalLongStreamHandler.class.getName(),
          /* 4 */ functionParamName,
          /* 5 */ commandName,
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

    private String getBasicStreamHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicStreamHandler.class.getName();
      }
      return BasicStreamHandler.class.getName();
    }

    private String getDomainStreamHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainStreamHandler.class.getName();
      }
      return DomainStreamHandler.class.getName();
    }

    private String getMapStreamHandlerName(Boolean optional) {
      return MapStreamHandler.class.getName();
    }

    private String getEntityStreamHandlerName(Boolean optional) {
      return EntityStreamHandler.class.getName();
    }
  }

  private class SqlFileSelectQueryCollectorCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    private final SqlFileSelectQueryMeta m;

    private final QueryReturnMeta resultMeta;

    private final String commandClassName;

    private final String commandName;

    private final String collectorParamName;

    public SqlFileSelectQueryCollectorCtTypeVisitor(SqlFileSelectQueryMeta m) {
      this.m = m;
      this.resultMeta = m.getReturnMeta();
      this.commandClassName = m.getCommandClass().getName();
      this.commandName = m.getCommandClass().getSimpleName();
      this.collectorParamName = m.getCollectorParameterName();
    }

    @Override
    public Void visitBasicCtType(BasicCtType basicCtType, final Boolean optional)
        throws RuntimeException {
      basicCtType
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
                      /* 4 */ basicCtType.getBoxedTypeName(),
                      /* 5 */ ctType.getTypeName(),
                      /* 6 */ basicCtType.getQualifiedName(),
                      /* 7 */ collectorParamName,
                      /* 8 */ commandName,
                      /* 9 */ methodName);
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
                      /* 4 */ basicCtType.getBoxedTypeName(),
                      /* 5 */ ctType.getTypeName(),
                      /* 6 */ collectorParamName,
                      /* 7 */ commandName,
                      /* 8 */ methodName);
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
          /* 5 */ ctType.domainDescSingletonCode(),
          /* 6 */ collectorParamName,
          /* 7 */ commandName,
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
          /* 5 */ namingType.name(),
          /* 6 */ collectorParamName,
          /* 7 */ commandName,
          /* 8 */ methodName);
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Boolean optional) throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%4$s, %2$s>(%5$s, %6$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ resultMeta.getBoxedTypeName(),
          /* 3 */ getEntityCollectorHandlerName(optional),
          /* 4 */ ctType.getTypeName(),
          /* 5 */ ctType.entityDescSingletonCode(),
          /* 6 */ collectorParamName,
          /* 7 */ commandName,
          /* 8 */ methodName);
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
          /* 4 */ collectorParamName,
          /* 5 */ commandName,
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
          /* 4 */ collectorParamName,
          /* 5 */ commandName,
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
          /* 4 */ collectorParamName,
          /* 5 */ commandName,
          /* 6 */ methodName);
      return null;
    }

    private String getBasicCollectorHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicCollectorHandler.class.getName();
      }
      return BasicCollectorHandler.class.getName();
    }

    private String getDomainCollectorHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainCollectorHandler.class.getName();
      }
      return DomainCollectorHandler.class.getName();
    }

    private String getMapCollectorHandlerName(Boolean optional) {
      return MapCollectorHandler.class.getName();
    }

    protected String getEntityCollectorHandlerName(Boolean optional) {
      return EntityCollectorHandler.class.getName();
    }
  }

  private class SqlFileSelectQueryReturnCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    private final SqlFileSelectQueryMeta m;

    private final String resultBoxedTypeName;

    private final String commandClassName;

    private final String commandName;

    private SqlFileSelectQueryReturnCtTypeVisitor(SqlFileSelectQueryMeta m) {
      this.m = m;
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
                      /* 5 */ basicCtType.getQualifiedName(),
                      /* 6 */ basicCtType.getBoxedTypeName(),
                      /* 7 */ commandName,
                      /* 8 */ methodName);
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
                      /* 6 */ basicCtType.getBoxedTypeName(),
                      /* 7 */ commandName,
                      /* 8 */ methodName);
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
          /* 1 */ commandClassName,
          /* 2 */ resultBoxedTypeName,
          /* 3 */ getDomainSingleResultHandlerName(optional),
          /* 4 */ ctType.domainDescSingletonCode(),
          /* 5 */ ctType.getBoxedTypeName(),
          /* 6 */ commandName,
          /* 7 */ methodName,
          /* 8 */ ctType.getBasicCtType().getBoxedTypeName());
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Boolean optional) throws RuntimeException {
      MapKeyNamingType namingType = m.getMapKeyNamingType();
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s(%4$s.%5$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ resultBoxedTypeName,
          /* 3 */ getMapSingleResultHandlerName(optional),
          /* 4 */ namingType.getDeclaringClass().getName(),
          /* 5 */ namingType.name(),
          /* 6 */ commandName,
          /* 7 */ methodName);
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Boolean optional) throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%5$s>(%4$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ resultBoxedTypeName,
          /* 3 */ getEntitySingleResultHandlerName(optional),
          /* 4 */ ctType.entityDescSingletonCode(),
          /* 5 */ ctType.getTypeName(),
          /* 6 */ commandName,
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
          /* 1 */ commandClassName,
          /* 2 */ resultBoxedTypeName,
          /* 3 */ OptionalIntSingleResultHandler.class.getName(),
          /* 4 */ commandName,
          /* 5 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
          /* 1 */ commandClassName,
          /* 2 */ resultBoxedTypeName,
          /* 3 */ OptionalLongSingleResultHandler.class.getName(),
          /* 4 */ commandName,
          /* 5 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p)
        throws RuntimeException {
      iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
          /* 1 */ commandClassName,
          /* 2 */ resultBoxedTypeName,
          /* 3 */ OptionalDoubleSingleResultHandler.class.getName(),
          /* 4 */ commandName,
          /* 5 */ methodName);
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
                public Void visitBasicCtType(BasicCtType basicCtType, Boolean optional)
                    throws RuntimeException {
                  basicCtType
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
                                  /* 4 */ basicCtType.getBoxedTypeName(),
                                  /* 5 */ ctType.getTypeName(),
                                  /* 6 */ basicCtType.getQualifiedName(),
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
                                  /* 4 */ basicCtType.getBoxedTypeName(),
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
                      /* 5 */ ctType.domainDescSingletonCode(),
                      /* 6 */ commandName,
                      /* 7 */ methodName,
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
                      /* 6 */ commandName,
                      /* 7 */ methodName);
                  return null;
                }

                @Override
                public Void visitEntityCtType(EntityCtType ctType, Boolean optional)
                    throws RuntimeException {
                  iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%4$s>(%5$s));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ resultBoxedTypeName,
                      /* 3 */ getEntityResultListHandlerName(optional),
                      /* 4 */ ctType.getTypeName(),
                      /* 5 */ ctType.entityDescSingletonCode(),
                      /* 6 */ commandName,
                      /* 7 */ methodName);
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
                      /* 4 */ commandName,
                      /* 5 */ methodName);
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
                      /* 4 */ commandName,
                      /* 5 */ methodName);
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
                      /* 4 */ commandName,
                      /* 5 */ methodName);
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
                  resultBoxedTypeName,
                  commandClassName,
                  commandName,
                  Function.class.getName() + ".identity()"),
              false);
      return null;
    }

    private String getBasicSingleResultHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicSingleResultHandler.class.getName();
      }
      return BasicSingleResultHandler.class.getName();
    }

    private String getBasicResultListHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicResultListHandler.class.getName();
      }
      return BasicResultListHandler.class.getName();
    }

    private String getDomainSingleResultHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainSingleResultHandler.class.getName();
      }
      return DomainSingleResultHandler.class.getName();
    }

    private String getDomainResultListHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainResultListHandler.class.getName();
      }
      return DomainResultListHandler.class.getName();
    }

    private String getMapSingleResultHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalMapSingleResultHandler.class.getName();
      }
      return MapSingleResultHandler.class.getName();
    }

    private String getMapResultListHandlerName(Boolean optional) {
      return MapResultListHandler.class.getName();
    }

    private String getEntitySingleResultHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalEntitySingleResultHandler.class.getName();
      }
      return EntitySingleResultHandler.class.getName();
    }

    private String getEntityResultListHandlerName(Boolean optional) {
      return EntityResultListHandler.class.getName();
    }
  }
}
