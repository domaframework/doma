package org.seasar.doma.internal.apt.generator;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
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
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class DaoImplQueryMethodGenerator extends AbstractGenerator
    implements QueryMetaVisitor<Void> {

  private final DaoMeta daoMeta;

  private final QueryMeta queryMeta;

  private final String methodName;

  DaoImplQueryMethodGenerator(
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
      print("<%1$s> ", queryMeta.getTypeParameterNames());
    }
    print("%1$s %2$s(", queryMeta.getReturnMeta().getType(), queryMeta.getName());
    for (Iterator<QueryParameterMeta> it = queryMeta.getParameterMetas().iterator();
        it.hasNext(); ) {
      QueryParameterMeta parameterMeta = it.next();
      String parameterTypeName = ctx.getMoreTypes().getTypeName(parameterMeta.getType());
      if (!it.hasNext() && queryMeta.isVarArgs()) {
        parameterTypeName = parameterTypeName.replace("[]", "...");
      }
      print("%1$s %2$s", parameterTypeName, parameterMeta.getName());
      if (it.hasNext()) {
        print(", ");
      }
    }
    print(") ");
    if (!queryMeta.getThrownTypes().isEmpty()) {
      print("throws %1$s ", queryMeta.getThrownTypes());
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
        "%1$s __query = __support.getQueryImplementors().create%2$s(%3$s);%n",
        m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
    iprint("__query.setSqlFilePath(\"%1$s\");%n", m.getPath());
    if (m.getSelectOptionsCtType() != null) {
      iprint("__query.setOptions(%1$s);%n", m.getSelectOptionsParameterName());
    }
    if (m.getEntityCtType() != null) {
      iprint("__query.setEntityType(%1$s);%n", m.getEntityCtType().getTypeCode());
    }

    printAddParameterStatements(m.getParameterMetas());

    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.setResultEnsured(%1$s);%n", m.getEnsureResult());
    iprint("__query.setResultMappingEnsured(%1$s);%n", m.getEnsureResultMapping());
    if (m.getSelectStrategyType() == SelectType.RETURN) {
      iprint("__query.setFetchType(%1$s.%2$s);%n", FetchType.class, FetchType.LAZY);
    } else {
      iprint("__query.setFetchType(%1$s.%2$s);%n", FetchType.class, m.getFetchType());
    }
    iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    iprint("__query.setMaxRows(%1$s);%n", m.getMaxRows());
    iprint("__query.setFetchSize(%1$s);%n", m.getFetchSize());
    iprint("__query.setSqlLogType(%1$s.%2$s);%n", m.getSqlLogType().getClass(), m.getSqlLogType());
    if (m.isResultStream()) {
      iprint("__query.setResultStream(true);%n");
    }
    iprint("__query.prepare();%n");

    QueryReturnMeta returnMeta = m.getReturnMeta();

    if (m.getSelectStrategyType() == SelectType.RETURN) {
      CtType returnCtType = returnMeta.getCtType();
      returnCtType.accept(new SqlFileSelectQueryReturnCtTypeVisitor(m), false);
      iprint("%1$s __result = __command.execute();%n", returnMeta.getType());
      iprint("__query.complete();%n");
      iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
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
      if (returnMeta.getType().getKind() == TypeKind.VOID) {
        iprint("__command.execute();%n");
        iprint("__query.complete();%n");
        iprint("__support.exiting(\"%1$s\", \"%2$s\", null);%n", className, m.getName());
      } else {
        iprint("%1$s __result = __command.execute();%n", returnMeta.getType());
        iprint("__query.complete();%n");
        iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
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
        "%1$s __query = __support.getQueryImplementors().create%2$s(%3$s);%n",
        m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
    iprint("__query.setScriptFilePath(\"%1$s\");%n", m.getPath());
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.setBlockDelimiter(\"%1$s\");%n", m.getBlockDelimiter());
    iprint("__query.setHaltOnError(%1$s);%n", m.getHaltOnError());
    iprint("__query.setSqlLogType(%1$s.%2$s);%n", m.getSqlLogType().getClass(), m.getSqlLogType());
    iprint("__query.prepare();%n");
    iprint(
        "%1$s __command = __support.getCommandImplementors().create%2$s(%3$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getCommandClass().getSimpleName(),
        /* 3 */ methodName);
    iprint("__command.execute();%n");
    iprint("__query.complete();%n");
    iprint("__support.exiting(\"%1$s\", \"%2$s\", null);%n", className, m.getName());

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitAutoModifyQueryMeta(AutoModifyQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s<%2$s> __query = __support.getQueryImplementors().create%4$s(%5$s, %3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getEntityCtType().getType(),
        /* 3 */ m.getEntityCtType().getTypeCode(),
        /* 4 */ m.getQueryClass().getSimpleName(),
        /* 5 */ methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
    iprint("__query.setEntity(%1$s);%n", m.getEntityParameterName());
    DuplicateKeyType duplicateKeyType = m.getDuplicateKeyType();
    if (duplicateKeyType != null) {
      iprint(
          "__query.setDuplicateKeyType(org.seasar.doma.jdbc.query.DuplicateKeyType.%1$s);%n",
          duplicateKeyType);
    }
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    iprint("__query.setSqlLogType(%1$s.%2$s);%n", m.getSqlLogType().getClass(), m.getSqlLogType());

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
      iprint("__query.setIncludedPropertyNames(%1$s);%n", toConstants(include));
    }

    List<String> exclude = m.getExclude();
    if (exclude != null) {
      iprint("__query.setExcludedPropertyNames(%1$s);%n", toConstants(exclude));
    }

    List<String> duplicateKeys = m.getDuplicateKeys();
    if (duplicateKeys != null) {
      iprint("__query.setDuplicateKeyNames(%1$s);%n", toConstants(duplicateKeys));
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
        "%1$s __command = __support.getCommandImplementors().create%2$s(%3$s, __query);%n",
        m.getCommandClass().getName(), m.getCommandClass().getSimpleName(), methodName);

    EntityCtType entityCtType = m.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      iprint("int __count = __command.execute();%n");
      iprint("__query.complete();%n");
      iprint(
          "%1$s __result = new %1$s(__count, __query.getEntity());%n", m.getReturnMeta().getType());
    } else {
      iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getType());
      iprint("__query.complete();%n");
    }

    iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s __query = __support.getQueryImplementors().create%2$s(%3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getQueryClass().getSimpleName(),
        /* 3 */ methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
    iprint("__query.setSqlFilePath(\"%1$s\");%n", m.getPath());

    printAddParameterStatements(m.getParameterMetas());

    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    iprint("__query.setSqlLogType(%1$s.%2$s);%n", m.getSqlLogType().getClass(), m.getSqlLogType());

    if (m.getEntityParameterName() != null && m.getEntityCtType() != null) {
      iprint(
          "__query.setEntityAndEntityType(\"%1$s\", %2$s, %3$s);%n",
          m.getEntityParameterName(),
          m.getEntityParameterName(),
          m.getEntityCtType().getTypeCode());
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
      iprint("__query.setIncludedPropertyNames(%1$s);%n", toConstants(include));
    }

    List<String> exclude = m.getExclude();
    if (exclude != null) {
      iprint("__query.setExcludedPropertyNames(%1$s);%n", toConstants(exclude));
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
        "%1$s __command = __support.getCommandImplementors().create%2$s(%3$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getCommandClass().getSimpleName(),
        /* 3 */ methodName);

    EntityCtType entityCtType = m.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      iprint("int __count = __command.execute();%n");
      iprint("__query.complete();%n");
      iprint(
          "%1$s __result = new %1$s(__count, __query.getEntity(%2$s.class));%n",
          m.getReturnMeta().getType(), entityCtType.getQualifiedName());
    } else {
      iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getType());
      iprint("__query.complete();%n");
    }

    iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitAutoMultiInsertQueryMeta(AutoMultiInsertQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s<%2$s> __query = __support.getQueryImplementors().create%4$s(%5$s, %3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getEntityCtType().getType(),
        /* 3 */ m.getEntityCtType().getTypeCode(),
        /* 4 */ m.getQueryClass().getSimpleName(),
        /* 5 */ methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
    DuplicateKeyType duplicateKeyType = m.getDuplicateKeyType();
    if (duplicateKeyType != null) {
      iprint(
          "__query.setDuplicateKeyType(org.seasar.doma.jdbc.query.DuplicateKeyType.%1$s);%n",
          duplicateKeyType);
    }
    iprint("__query.setEntities(%1$s);%n", m.getEntityParameterName());
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    iprint("__query.setSqlLogType(%1$s.%2$s);%n", m.getSqlLogType().getClass(), m.getSqlLogType());

    List<String> include = m.getInclude();
    if (include != null) {
      iprint("__query.setIncludedPropertyNames(%1$s);%n", toConstants(include));
    }

    List<String> exclude = m.getExclude();
    if (exclude != null) {
      iprint("__query.setExcludedPropertyNames(%1$s);%n", toConstants(exclude));
    }

    List<String> duplicateKeys = m.getDuplicateKeys();
    if (duplicateKeys != null) {
      iprint("__query.setDuplicateKeyNames(%1$s);%n", toConstants(duplicateKeys));
    }

    iprint("__query.prepare();%n");
    iprint(
        "%1$s __command = __support.getCommandImplementors().create%2$s(%3$s, __query);%n",
        m.getCommandClass().getName(), m.getCommandClass().getSimpleName(), methodName);

    EntityCtType entityCtType = m.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      iprint("int __count = __command.execute();%n");
      iprint("__query.complete();%n");
      iprint(
          "%1$s __result = new %1$s(__count, __query.getEntities());%n",
          m.getReturnMeta().getType());
    } else {
      iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getType());
      iprint("__query.complete();%n");
    }

    iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s<%2$s> __query = __support.getQueryImplementors().create%4$s(%5$s, %3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getEntityCtType().getType(),
        /* 3 */ m.getEntityCtType().getTypeCode(),
        /* 4 */ m.getQueryClass().getSimpleName(),
        /* 5 */ methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
    iprint("__query.setEntities(%1$s);%n", m.getEntitiesParameterName());
    DuplicateKeyType duplicateKeyType = m.getDuplicateKeyType();
    if (duplicateKeyType != null) {
      iprint(
          "__query.setDuplicateKeyType(org.seasar.doma.jdbc.query.DuplicateKeyType.%1$s);%n",
          duplicateKeyType);
    }
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    iprint("__query.setBatchSize(%1$s);%n", m.getBatchSize());
    iprint("__query.setSqlLogType(%1$s.%2$s);%n", m.getSqlLogType().getClass(), m.getSqlLogType());

    Boolean ignoreVersion = m.getIgnoreVersion();
    if (ignoreVersion != null) {
      iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
    }

    List<String> include = m.getInclude();
    if (include != null) {
      iprint("__query.setIncludedPropertyNames(%1$s);%n", toConstants(include));
    }

    List<String> exclude = m.getExclude();
    if (exclude != null) {
      iprint("__query.setExcludedPropertyNames(%1$s);%n", toConstants(exclude));
    }

    List<String> duplicateKeys = m.getDuplicateKeys();
    if (duplicateKeys != null) {
      iprint("__query.setDuplicateKeyNames(%1$s);%n", toConstants(duplicateKeys));
    }

    Boolean suppressOptimisticLockException = m.getSuppressOptimisticLockException();
    if (suppressOptimisticLockException != null) {
      iprint(
          "__query.setOptimisticLockExceptionSuppressed(%1$s);%n", suppressOptimisticLockException);
    }

    Boolean ignoreGeneratedKeys = m.getIgnoreGeneratedKeysValues();
    if (ignoreGeneratedKeys != null) {
      iprint("__query.setGeneratedKeysIgnored(%1$s);%n", ignoreGeneratedKeys);
    }

    iprint("__query.prepare();%n");
    iprint(
        "%1$s __command = __support.getCommandImplementors().create%2$s(%3$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getCommandClass().getSimpleName(),
        /* 3 */ methodName);

    EntityCtType entityCtType = m.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      iprint("int[] __counts = __command.execute();%n");
      iprint("__query.complete();%n");
      iprint(
          "%1$s __result = new %1$s(__counts, __query.getEntities());%n",
          m.getReturnMeta().getType());
    } else {
      iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getType());
      iprint("__query.complete();%n");
    }

    iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitSqlFileBatchModifyQueryMeta(SqlFileBatchModifyQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s<%2$s> __query = __support.getQueryImplementors().create%4$s(%5$s, %3$s.class);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getElementCtType().getType(),
        /* 3 */ m.getElementCtType().getQualifiedName(),
        /* 4 */ m.getQueryClass().getSimpleName(),
        /* 5 */ methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
    iprint("__query.setElements(%1$s);%n", m.getElementsParameterName());
    iprint("__query.setSqlFilePath(\"%1$s\");%n", m.getPath());
    iprint("__query.setParameterName(\"%1$s\");%n", m.getElementsParameterName());
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    iprint("__query.setBatchSize(%1$s);%n", m.getBatchSize());
    iprint("__query.setSqlLogType(%1$s.%2$s);%n", m.getSqlLogType().getClass(), m.getSqlLogType());

    if (m.getEntityType() != null) {
      iprint("__query.setEntityType(%1$s);%n", m.getEntityType().getTypeCode());
    }

    Boolean ignoreVersion = m.getIgnoreVersion();
    if (ignoreVersion != null) {
      iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
    }

    List<String> include = m.getInclude();
    if (include != null) {
      iprint("__query.setIncludedPropertyNames(%1$s);%n", toConstants(include));
    }

    List<String> exclude = m.getExclude();
    if (exclude != null) {
      iprint("__query.setExcludedPropertyNames(%1$s);%n", toConstants(exclude));
    }

    Boolean suppressOptimisticLockException = m.getSuppressOptimisticLockException();
    if (suppressOptimisticLockException != null) {
      iprint(
          "__query.setOptimisticLockExceptionSuppressed(%1$s);%n", suppressOptimisticLockException);
    }

    iprint("__query.prepare();%n");
    iprint(
        "%1$s __command = __support.getCommandImplementors().create%2$s(%3$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getCommandClass().getSimpleName(),
        /* 3 */ methodName);

    EntityCtType entityCtType = m.getEntityType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      iprint("int[] __counts = __command.execute();%n");
      iprint("__query.complete();%n");
      iprint(
          "%1$s __result = new %1$s(__counts, __query.getEntities());%n",
          m.getReturnMeta().getType());
    } else {
      iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getType());
      iprint("__query.complete();%n");
    }

    iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
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
        "%1$s<%2$s> __query = __support.getQueryImplementors().create%3$s(%4$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ returnMeta.getBoxedType(),
        /* 3 */ m.getQueryClass().getSimpleName(),
        /* 4 */ methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
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
    iprint("__query.setSqlLogType(%1$s.%2$s);%n", m.getSqlLogType().getClass(), m.getSqlLogType());
    iprint("__query.prepare();%n");
    iprint(
        "%1$s<%2$s> __command = __support.getCommandImplementors().create%3$s(%4$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ returnMeta.getBoxedType(),
        /* 3 */ m.getCommandClass().getSimpleName(),
        /* 4 */ methodName);
    iprint("%1$s __result = __command.execute();%n", returnMeta.getType());
    iprint("__query.complete();%n");
    iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    iprint(
        "%1$s __query = __support.getQueryImplementors().create%2$s(%3$s);%n",
        m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
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
    iprint("__query.setSqlLogType(%1$s.%2$s);%n", m.getSqlLogType().getClass(), m.getSqlLogType());
    iprint("__query.prepare();%n");
    iprint(
        "%1$s __command = __support.getCommandImplementors().create%2$s(%3$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getCommandClass().getSimpleName(),
        /* 3 */ methodName);
    iprint("__command.execute();%n");
    iprint("__query.complete();%n");
    iprint("__support.exiting(\"%1$s\", \"%2$s\", null);%n", className, m.getName());

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m) {
    printArrayCreateEnteringStatements(m);
    printPrerequisiteStatements(m);

    QueryReturnMeta resultMeta = m.getReturnMeta();
    iprint(
        "%1$s __query = __support.getQueryImplementors().create%2$s(%3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getQueryClass().getSimpleName(),
        /* 3 */ methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.setTypeName(\"%1$s\");%n", m.getArrayTypeName());
    iprint("__query.setElements(%1$s);%n", m.getParameterName());
    iprint("__query.prepare();%n");
    iprint(
        "%1$s<%2$s> __command = __support.getCommandImplementors().create%3$s(%4$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ resultMeta.getBoxedType(),
        /* 3 */ m.getCommandClass().getSimpleName(),
        /* 4 */ methodName);
    iprint("%1$s __result = __command.execute();%n", resultMeta.getType());
    iprint("__query.complete();%n");
    iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
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
        "%1$s __query = __support.getQueryImplementors().create%2$s(%3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getQueryClass().getSimpleName(),
        /* 3 */ methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.prepare();%n");
    iprint(
        "%1$s<%2$s> __command = __support.getCommandImplementors().create%3$s(%4$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ resultMeta.getType(),
        /* 3 */ m.getCommandClass().getSimpleName(),
        /* 4 */ methodName);
    iprint("%1$s __result = __command.execute();%n", resultMeta.getType());
    iprint("__query.complete();%n");
    iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    iprint("return __result;%n");

    printThrowingStatements(m);
    return null;
  }

  @Override
  public Void visitDefaultQueryMeta(DefaultQueryMeta m) {
    printEnteringStatements(m);

    QueryReturnMeta resultMeta = m.getReturnMeta();
    if (resultMeta.getType().getKind() == TypeKind.VOID) {
      iprint("Object __result = null;%n");
      iprint("");
    } else {
      iprint("%1$s __result = ", resultMeta.getType());
    }
    if (m.isVirtual()) {
      print("%1$s.DefaultImpls.%2$s(this", daoMeta.getTypeElement(), m.getName());
      if (!m.getParameterMetas().isEmpty()) {
        print(", ");
      }
    } else {
      print("%1$s.super.%2$s(", daoMeta.getTypeElement(), m.getName());
    }
    for (Iterator<QueryParameterMeta> it = m.getParameterMetas().iterator(); it.hasNext(); ) {
      QueryParameterMeta parameterMeta = it.next();
      print("%1$s", parameterMeta.getName());
      if (it.hasNext()) {
        print(", ");
      }
    }
    print(");%n");
    iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
    if (resultMeta.getType().getKind() != TypeKind.VOID) {
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
        "%1$s __query = __support.getQueryImplementors().create%2$s(%3$s);%n",
        m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
    iprint("__query.setMethod(%1$s);%n", methodName);
    iprint("__query.setConfig(__support.getConfig());%n");
    iprint("__query.setSqlFilePath(\"%1$s\");%n", m.getPath());

    printAddParameterStatements(m.getParameterMetas());

    iprint("__query.setCallerClassName(\"%1$s\");%n", className);
    iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    iprint("__query.prepare();%n");

    QueryReturnMeta returnMeta = m.getReturnMeta();
    iprint(
        "%1$s<%2$s> __command = __support.getCommandImplementors().create%3$s(%4$s, __query, %5$s);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getBiFunctionCtType().getResultCtType().getType(),
        /* 3 */ m.getCommandClass().getSimpleName(),
        /* 4 */ methodName,
        /* 5 */ m.getBiFunctionParameterName());

    if (returnMeta.getType().getKind() == TypeKind.VOID) {
      iprint("__command.execute();%n");
      iprint("__query.complete();%n");
      iprint("__support.exiting(\"%1$s\", \"%2$s\", null);%n", className, m.getName());
    } else {
      iprint("%1$s __result = __command.execute();%n", returnMeta.getType());
      iprint("__query.complete();%n");
      iprint("__support.exiting(\"%1$s\", \"%2$s\", __result);%n", className, m.getName());
      iprint("return __result;%n");
    }

    printThrowingStatements(m);
    return null;
  }

  private void printEnteringStatements(QueryMeta m) {
    iprint("__support.entering(\"%1$s\", \"%2$s\"", className, m.getName());
    for (QueryParameterMeta parameterMeta : m.getParameterMetas()) {
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
        "__support.entering(\"%1$s\", \"%2$s\", (Object)%3$s);%n",
        className, m.getName(), m.getParameterName());
    iprint("try {%n");
    indent();
  }

  private void printThrowingStatements(QueryMeta m) {
    unindent();
    iprint("} catch (%1$s __e) {%n", RuntimeException.class);
    indent();
    iprint("__support.throwing(\"%1$s\", \"%2$s\", __e);%n", className, m.getName());
    iprint("throw __e;%n");
    unindent();
    iprint("}%n");
  }

  private void printPrerequisiteStatements(QueryMeta m) {
    for (QueryParameterMeta parameterMeta : m.getParameterMetas()) {
      if (parameterMeta.isNullable()) {
        continue;
      }
      String paramName = parameterMeta.getName();
      iprint("if (%1$s == null) {%n", paramName);
      iprint("    throw new %1$s(\"%2$s\");%n", DomaNullPointerException.class, paramName);
      iprint("}%n");
    }
  }

  private void printAddParameterStatements(List<QueryParameterMeta> ParameterMetas) {
    for (QueryParameterMeta parameterMeta : ParameterMetas) {
      if (parameterMeta.isBindable()) {
        CtType ctType = parameterMeta.getCtType();
        ctType.accept(
            new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

              @Override
              protected Void defaultAction(CtType ctType, Void p) {
                iprint(
                    "__query.addParameter(\"%1$s\", %2$s.class, %1$s);%n",
                    /* 1 */ parameterMeta.getName(), /* 2 */ ctType.getQualifiedName());
                return null;
              }

              @Override
              public Void visitOptionalCtType(OptionalCtType ctType, Void p) {
                iprint(
                    "__query.addParameter(\"%1$s\", %2$s.class, %1$s.orElse(null));%n",
                    /* 1 */ parameterMeta.getName(),
                    /* 2 */ ctType.getElementCtType().getQualifiedName());
                return null;
              }

              @Override
              public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p) {
                iprint(
                    "__query.addParameter(\"%1$s\", %2$s.class, %1$s.isPresent() ? %1$s.getAsInt() : null);%n",
                    /* 1 */ parameterMeta.getName(), /* 2 */ Integer.class);
                return null;
              }

              @Override
              public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p) {
                iprint(
                    "__query.addParameter(\"%1$s\", %2$s.class, %1$s.isPresent() ? %1$s.getAsLong() : null);%n",
                    /* 1 */ parameterMeta.getName(), /* 2 */ Long.class);
                return null;
              }

              @Override
              public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p) {
                iprint(
                    "__query.addParameter(\"%1$s\", %2$s.class, %1$s.isPresent() ? %1$s.getAsDouble() : null);%n",
                    /* 1 */ parameterMeta.getName(), /* 2 */ Double.class);
                return null;
              }
            },
            null);
      }
    }
  }

  private List<String> toConstants(List<String> values) {
    return values.stream().map(ctx.getMoreElements()::getConstantExpression).collect(toList());
  }

  private class CallableSqlParameterStatementGenerator
      implements CallableSqlParameterMetaVisitor<Void, AutoModuleQueryMeta> {

    @Override
    public Void visitBasicListParameterMeta(final BasicListParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s>(%3$s, %4$s, \"%4$s\"));%n",
          /* 1 */ BasicListParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ basicCtType.getWrapperSupplierCode(),
          /* 4 */ m.getName());
      return null;
    }

    @Override
    public Void visitDomainListParameterMeta(DomainListParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s, \"%5$s\"));%n",
          /* 1 */ DomainListParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitEntityListParameterMeta(EntityListParameterMeta m, AutoModuleQueryMeta p) {
      EntityCtType entityCtType = m.getEntityCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s>(%3$s, %4$s, \"%4$s\", %5$s));%n",
          /* 1 */ EntityListParameter.class,
          /* 2 */ entityCtType.getType(),
          /* 3 */ entityCtType.getTypeCode(),
          /* 4 */ m.getName(),
          /* 5 */ m.getEnsureResultMapping());
      return null;
    }

    @Override
    public Void visitMapListParameterMeta(MapListParameterMeta m, AutoModuleQueryMeta p) {
      MapKeyNamingType namingType = p.getMapKeyNamingType();
      iprint(
          "__query.addParameter(new %1$s(%2$s.%3$s, %4$s, \"%4$s\"));%n",
          /* 1 */ MapListParameter.class,
          /* 2 */ namingType.getDeclaringClass().getName(),
          /* 3 */ namingType.name(),
          m.getName());
      return null;
    }

    @Override
    public Void visitBasicInOutParameterMeta(
        final BasicInOutParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s>(%3$s, %4$s));%n",
          /* 1 */ BasicInOutParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ basicCtType.getWrapperSupplierCode(),
          /* 4 */ m.getName());
      return null;
    }

    @Override
    public Void visitDomainInOutParameterMeta(DomainInOutParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
          /* 1 */ DomainInOutParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitBasicOutParameterMeta(final BasicOutParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s>(%3$s, %4$s));%n",
          /* 1 */ BasicOutParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ basicCtType.getWrapperSupplierCode(),
          /* 4 */ m.getName());
      return null;
    }

    @Override
    public Void visitDomainOutParameterMeta(DomainOutParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
          /* 1 */ DomainOutParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitBasicInParameterMeta(final BasicInParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%4$s>(%2$s, %3$s));%n",
          /* 1 */ BasicInParameter.class,
          /* 2 */ basicCtType.getWrapperSupplierCode(),
          /* 3 */ m.getName(),
          /* 4 */ basicCtType.getBoxedType());
      return null;
    }

    @Override
    public Void visitDomainInParameterMeta(DomainInParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
          /* 1 */ DomainInParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode(),
          /* 5 */ m.getName());
      return null;
    }

    @Override
    public Void visitBasicResultListParameterMeta(
        BasicResultListParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s>(%3$s));%n",
          /* 1 */ BasicResultListParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ basicCtType.getWrapperSupplierCode());
      return null;
    }

    @Override
    public Void visitDomainResultListParameterMeta(
        DomainResultListParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s, %3$s>(%4$s));%n",
          /* 1 */ DomainResultListParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode());
      return null;
    }

    @Override
    public Void visitEntityResultListParameterMeta(
        EntityResultListParameterMeta m, AutoModuleQueryMeta p) {
      EntityCtType entityCtType = m.getEntityCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s>(%3$s, %4$s));%n",
          /* 1 */ EntityResultListParameter.class,
          /* 2 */ entityCtType.getType(),
          /* 3 */ entityCtType.getTypeCode(),
          /* 4 */ m.getEnsureResultMapping());
      return null;
    }

    @Override
    public Void visitMapResultListParameterMeta(
        MapResultListParameterMeta m, AutoModuleQueryMeta p) {
      MapKeyNamingType namingType = p.getMapKeyNamingType();
      iprint(
          "__query.setResultParameter(new %1$s(%2$s.%3$s));%n",
          /* 1 */ MapResultListParameter.class,
          /* 2 */ namingType.getDeclaringClass().getName(),
          /* 3 */ namingType.name());
      return null;
    }

    @Override
    public Void visitBasicSingleResultParameterMeta(
        BasicSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      final BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s>(%3$s));%n",
          /* 1 */ BasicSingleResultParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ basicCtType.getWrapperSupplierCode());
      return null;
    }

    @Override
    public Void visitDomainSingleResultParameterMeta(
        DomainSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s, %3$s>(%4$s));%n",
          /* 1 */ DomainSingleResultParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode());
      return null;
    }

    @Override
    public Void visitOptionalBasicInParameterMeta(
        OptionalBasicInParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%4$s>(%2$s, %3$s));%n",
          /* 1 */ OptionalBasicInParameter.class,
          /* 2 */ basicCtType.getWrapperSupplierCode(),
          /* 3 */ m.getName(),
          /* 4 */ basicCtType.getBoxedType());
      return null;
    }

    @Override
    public Void visitOptionalBasicOutParameterMeta(
        OptionalBasicOutParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s>(%3$s, %4$s));%n",
          /* 1 */ OptionalBasicOutParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ basicCtType.getWrapperSupplierCode(),
          /* 4 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalBasicInOutParameterMeta(
        OptionalBasicInOutParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s>(%3$s, %4$s));%n",
          /* 1 */ OptionalBasicInOutParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ basicCtType.getWrapperSupplierCode(),
          /* 4 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalBasicListParameterMeta(
        OptionalBasicListParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s>(%3$s, %4$s, \"%4$s\"));%n",
          /* 1 */ OptionalBasicListParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ basicCtType.getWrapperSupplierCode(),
          /* 4 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalBasicSingleResultParameterMeta(
        OptionalBasicSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      final BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s>(%3$s));%n",
          /* 1 */ OptionalBasicSingleResultParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ basicCtType.getWrapperSupplierCode());
      return null;
    }

    @Override
    public Void visitOptionalBasicResultListParameterMeta(
        OptionalBasicResultListParameterMeta m, AutoModuleQueryMeta p) {
      BasicCtType basicCtType = m.getBasicCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s>(%3$s));%n",
          /* 1 */ OptionalBasicResultListParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ basicCtType.getWrapperSupplierCode());
      return null;
    }

    @Override
    public Void visitOptionalDomainInParameterMeta(
        OptionalDomainInParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
          /* 1 */ OptionalDomainInParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode(),
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
          /* 1 */ OptionalDomainOutParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode(),
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
          /* 1 */ OptionalDomainInOutParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode(),
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
          /* 1 */ OptionalDomainListParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode(),
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
          /* 1 */ OptionalDomainSingleResultParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode());
      return null;
    }

    @Override
    public Void visitOptionalDomainResultListParameterMeta(
        OptionalDomainResultListParameterMeta m, AutoModuleQueryMeta p) {
      DomainCtType domainCtType = m.getDomainCtType();
      BasicCtType basicCtType = domainCtType.getBasicCtType();
      iprint(
          "__query.setResultParameter(new %1$s<%2$s, %3$s>(%4$s));%n",
          /* 1 */ OptionalDomainResultListParameter.class,
          /* 2 */ basicCtType.getBoxedType(),
          /* 3 */ domainCtType.getType(),
          /* 4 */ domainCtType.getTypeCode());
      return null;
    }

    @Override
    public Void visitOptionalIntInOutParameterMeta(
        OptionalIntInOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalIntInOutParameter.class, /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalIntInParameterMeta(
        OptionalIntInParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalIntInParameter.class, /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalIntListParameterMeta(
        OptionalIntListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s, \"%2$s\"));%n",
          /* 1 */ OptionalIntListParameter.class, /* 2 */ m.getName());
      return null;
    }

    public Void visitOptionalIntOutParameterMeta(
        OptionalIntOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalIntOutParameter.class, /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalIntSingleResultParameterMeta(
        OptionalIntSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalIntSingleResultParameter.class);
      return null;
    }

    @Override
    public Void visitOptionalIntResultListParameterMeta(
        OptionalIntResultListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalIntResultListParameter.class);
      return null;
    }

    @Override
    public Void visitOptionalLongOutParameterMeta(
        OptionalLongOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalLongOutParameter.class, /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalLongSingleResultParameterMeta(
        OptionalLongSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalLongSingleResultParameter.class);
      return null;
    }

    @Override
    public Void visitOptionalLongResultListParameterMeta(
        OptionalLongResultListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalLongResultListParameter.class);
      return null;
    }

    @Override
    public Void visitOptionalLongInOutParameterMeta(
        OptionalLongInOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalLongInOutParameter.class, /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalLongInParameterMeta(
        OptionalLongInParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalLongInParameter.class, /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalLongListParameterMeta(
        OptionalLongListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s, \"%2$s\"));%n",
          /* 1 */ OptionalLongListParameter.class, /* 2 */ m.getName());
      return null;
    }

    public Void visitOptionalDoubleOutParameterMeta(
        OptionalDoubleOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalDoubleOutParameter.class, /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalDoubleSingleResultParameterMeta(
        OptionalDoubleSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalDoubleSingleResultParameter.class);
      return null;
    }

    @Override
    public Void visitOptionalDoubleResultListParameterMeta(
        OptionalDoubleResultListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.setResultParameter(new %1$s());%n",
          /* 1 */ OptionalDoubleResultListParameter.class);
      return null;
    }

    @Override
    public Void visitOptionalDoubleInOutParameterMeta(
        OptionalDoubleInOutParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalDoubleInOutParameter.class, /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalDoubleInParameterMeta(
        OptionalDoubleInParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s));%n",
          /* 1 */ OptionalDoubleInParameter.class, /* 2 */ m.getName());
      return null;
    }

    @Override
    public Void visitOptionalDoubleListParameterMeta(
        OptionalDoubleListParameterMeta m, AutoModuleQueryMeta p) {
      iprint(
          "__query.addParameter(new %1$s(%2$s, \"%2$s\"));%n",
          /* 1 */ OptionalDoubleListParameter.class, /* 2 */ m.getName());
      return null;
    }
  }

  private class SqlFileSelectQueryFunctionCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlFileSelectQueryMeta m;

    private final QueryReturnMeta resultMeta;

    private final Class<?> commandClass;

    private final String commandName;

    private final String functionParamName;

    private SqlFileSelectQueryFunctionCtTypeVisitor(SqlFileSelectQueryMeta m) {
      this.m = m;
      this.resultMeta = m.getReturnMeta();
      this.commandClass = m.getCommandClass();
      this.commandName = m.getCommandClass().getSimpleName();
      this.functionParamName = m.getFunctionParameterName();
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Void p) {
      ctType
          .getElementCtType()
          .accept(
              new StreamElementCtTypeVisitor(
                  m, resultMeta.getBoxedType(), commandClass, commandName, functionParamName),
              false);
      return null;
    }
  }

  private class StreamElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    private final SqlFileSelectQueryMeta m;

    private final TypeMirror resultBoxedType;

    private final Class<?> commandClass;

    private final String commandName;

    private final String functionParamName;

    private StreamElementCtTypeVisitor(
        SqlFileSelectQueryMeta m,
        TypeMirror resultBoxedType,
        Class<?> commandClass,
        String commandName,
        String functionParamName) {
      this.m = m;
      this.resultBoxedType = resultBoxedType;
      this.commandClass = commandClass;
      this.commandName = commandName;
      this.functionParamName = functionParamName;
    }

    @Override
    public Void visitBasicCtType(BasicCtType basicCtType, final Boolean optional) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%4$s, %2$s>(%5$s, %6$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ getBasicStreamHandler(optional),
          /* 4 */ basicCtType.getBoxedType(),
          /* 5 */ basicCtType.getWrapperSupplierCode(),
          /* 6 */ functionParamName,
          /* 7 */ commandName,
          /* 8 */ methodName);
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Boolean optional) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%9$s, %4$s, %2$s>(%5$s, %6$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ getDomainStreamHandler(optional),
          /* 4 */ ctType.getType(),
          /* 5 */ ctType.getTypeCode(),
          /* 6 */ functionParamName,
          /* 7 */ commandName,
          /* 8 */ methodName,
          /* 9 */ ctType.getBasicCtType().getBoxedType());
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Boolean optional) {
      MapKeyNamingType namingType = m.getMapKeyNamingType();
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%2$s>(%4$s.%5$s, %6$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ MapStreamHandler.class,
          /* 4 */ namingType.getDeclaringClass(),
          /* 5 */ namingType.name(),
          /* 6 */ functionParamName,
          /* 7 */ commandName,
          /* 8 */ methodName);
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Boolean optional) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%4$s, %2$s>(%5$s, %6$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ EntityStreamHandler.class,
          /* 4 */ ctType.getType(),
          /* 5 */ ctType.getTypeCode(),
          /* 6 */ functionParamName,
          /* 7 */ commandName,
          /* 8 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Boolean optional) {
      return ctType.getElementCtType().accept(this, true);
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ OptionalIntStreamHandler.class,
          /* 4 */ functionParamName,
          /* 5 */ commandName,
          /* 6 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ OptionalLongStreamHandler.class,
          /* 4 */ functionParamName,
          /* 5 */ commandName,
          /* 6 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ OptionalDoubleStreamHandler.class,
          /* 4 */ functionParamName,
          /* 5 */ commandName,
          /* 6 */ methodName);
      return null;
    }

    @SuppressWarnings("rawtypes")
    private Class<? extends ScalarStreamHandler> getBasicStreamHandler(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicStreamHandler.class;
      }
      return BasicStreamHandler.class;
    }

    @SuppressWarnings("rawtypes")
    private Class<? extends ScalarStreamHandler> getDomainStreamHandler(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainStreamHandler.class;
      }
      return DomainStreamHandler.class;
    }
  }

  private class SqlFileSelectQueryCollectorCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    private final SqlFileSelectQueryMeta m;

    private final QueryReturnMeta resultMeta;

    private final Class<?> commandClass;

    private final String commandName;

    private final String collectorParamName;

    private SqlFileSelectQueryCollectorCtTypeVisitor(SqlFileSelectQueryMeta m) {
      this.m = m;
      this.resultMeta = m.getReturnMeta();
      this.commandClass = m.getCommandClass();
      this.commandName = m.getCommandClass().getSimpleName();
      this.collectorParamName = m.getCollectorParameterName();
    }

    @Override
    public Void visitBasicCtType(BasicCtType basicCtType, final Boolean optional) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%4$s, %2$s>(%5$s, %6$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultMeta.getBoxedType(),
          /* 3 */ getBasicCollectorHandler(optional),
          /* 4 */ basicCtType.getBoxedType(),
          /* 5 */ basicCtType.getWrapperSupplierCode(),
          /* 6 */ collectorParamName,
          /* 7 */ commandName,
          /* 8 */ methodName);
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Boolean optional) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%9$s, %4$s, %2$s>(%5$s, %6$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultMeta.getBoxedType(),
          /* 3 */ getDomainCollectorHandler(optional),
          /* 4 */ ctType.getType(),
          /* 5 */ ctType.getTypeCode(),
          /* 6 */ collectorParamName,
          /* 7 */ commandName,
          /* 8 */ methodName,
          /* 9 */ ctType.getBasicCtType().getBoxedType());
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Boolean optional) {
      MapKeyNamingType namingType = m.getMapKeyNamingType();
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%2$s>(%4$s.%5$s, %6$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultMeta.getBoxedType(),
          /* 3 */ MapCollectorHandler.class,
          /* 4 */ namingType.getDeclaringClass().getName(),
          /* 5 */ namingType.name(),
          /* 6 */ collectorParamName,
          /* 7 */ commandName,
          /* 8 */ methodName);
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Boolean optional) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%7$s(%8$s, __query, new %3$s<%4$s, %2$s>(%5$s, %6$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultMeta.getBoxedType(),
          /* 3 */ EntityCollectorHandler.class,
          /* 4 */ ctType.getType(),
          /* 5 */ ctType.getTypeCode(),
          /* 6 */ collectorParamName,
          /* 7 */ commandName,
          /* 8 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Boolean optional) {
      return ctType.getElementCtType().accept(this, true);
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultMeta.getBoxedType(),
          /* 3 */ OptionalIntCollectorHandler.class,
          /* 4 */ collectorParamName,
          /* 5 */ commandName,
          /* 6 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultMeta.getBoxedType(),
          /* 3 */ OptionalLongCollectorHandler.class,
          /* 4 */ collectorParamName,
          /* 5 */ commandName,
          /* 6 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%5$s(%6$s, __query, new %3$s<%2$s>(%4$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultMeta.getBoxedType(),
          /* 3 */ OptionalDoubleCollectorHandler.class,
          /* 4 */ collectorParamName,
          /* 5 */ commandName,
          /* 6 */ methodName);
      return null;
    }

    @SuppressWarnings("rawtypes")
    private Class<? extends ScalarCollectorHandler> getBasicCollectorHandler(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicCollectorHandler.class;
      }
      return BasicCollectorHandler.class;
    }

    @SuppressWarnings("rawtypes")
    private Class<? extends ScalarCollectorHandler> getDomainCollectorHandler(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainCollectorHandler.class;
      }
      return DomainCollectorHandler.class;
    }
  }

  private class SqlFileSelectQueryReturnCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    private final SqlFileSelectQueryMeta m;

    private final TypeMirror resultBoxedType;

    private final Class<?> commandClass;

    private final String commandName;

    private SqlFileSelectQueryReturnCtTypeVisitor(SqlFileSelectQueryMeta m) {
      this.m = m;
      this.resultBoxedType = this.m.getReturnMeta().getBoxedType();
      this.commandClass = m.getCommandClass();
      this.commandName = m.getCommandClass().getSimpleName();
    }

    @Override
    public Void visitBasicCtType(final BasicCtType basicCtType, Boolean optional) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%5$s>(%4$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ getBasicSingleResultHandler(optional),
          /* 4 */ basicCtType.getWrapperSupplierCode(),
          /* 5 */ basicCtType.getBoxedType(),
          /* 6 */ commandName,
          /* 7 */ methodName);
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Boolean optional) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%8$s, %5$s>(%4$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ getDomainSingleResultHandler(optional),
          /* 4 */ ctType.getTypeCode(),
          /* 5 */ ctType.getType(),
          /* 6 */ commandName,
          /* 7 */ methodName,
          /* 8 */ ctType.getBasicCtType().getBoxedType());
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Boolean optional) {
      MapKeyNamingType namingType = m.getMapKeyNamingType();
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%6$s(%7$s, __query, new %3$s(%4$s.%5$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ getMapSingleResultHandler(optional),
          /* 4 */ namingType.getDeclaringClass().getName(),
          /* 5 */ namingType.name(),
          /* 6 */ commandName,
          /* 7 */ methodName);
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Boolean optional) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%5$s>(%4$s));%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ getEntitySingleResultHandler(optional),
          /* 4 */ ctType.getTypeCode(),
          /* 5 */ ctType.getType(),
          /* 6 */ commandName,
          /* 7 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Boolean optional) {
      return ctType.getElementCtType().accept(this, true);
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ OptionalIntSingleResultHandler.class,
          /* 4 */ commandName,
          /* 5 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ OptionalLongSingleResultHandler.class,
          /* 4 */ commandName,
          /* 5 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p) {
      iprint(
          "%1$s<%2$s> __command = __support.getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
          /* 1 */ commandClass,
          /* 2 */ resultBoxedType,
          /* 3 */ OptionalDoubleSingleResultHandler.class,
          /* 4 */ commandName,
          /* 5 */ methodName);
      return null;
    }

    @Override
    public Void visitIterableCtType(final IterableCtType iterableCtType, final Boolean __) {
      iterableCtType
          .getElementCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Boolean, RuntimeException>() {

                @Override
                public Void visitBasicCtType(BasicCtType basicCtType, Boolean optional) {
                  iprint(
                      "%1$s<%2$s> __command = __support.getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%4$s>(%5$s));%n",
                      /* 1 */ commandClass,
                      /* 2 */ resultBoxedType,
                      /* 3 */ getBasicResultListHandler(optional),
                      /* 4 */ basicCtType.getBoxedType(),
                      /* 5 */ basicCtType.getWrapperSupplierCode(),
                      /* 6 */ commandName,
                      /* 7 */ methodName);
                  return null;
                }

                @Override
                public Void visitDomainCtType(DomainCtType ctType, Boolean optional) {
                  iprint(
                      "%1$s<%2$s> __command = __support.getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%8$s, %4$s>(%5$s));%n",
                      /* 1 */ commandClass,
                      /* 2 */ resultBoxedType,
                      /* 3 */ getDomainResultListHandler(optional),
                      /* 4 */ ctType.getType(),
                      /* 5 */ ctType.getTypeCode(),
                      /* 6 */ commandName,
                      /* 7 */ methodName,
                      /* 8 */ ctType.getBasicCtType().getBoxedType());
                  return null;
                }

                @Override
                public Void visitMapCtType(MapCtType ctType, Boolean optional) {
                  MapKeyNamingType namingType = m.getMapKeyNamingType();
                  iprint(
                      "%1$s<%2$s> __command = __support.getCommandImplementors().create%6$s(%7$s, __query, new %3$s(%4$s.%5$s));%n",
                      /* 1 */ commandClass,
                      /* 2 */ resultBoxedType,
                      /* 3 */ MapResultListHandler.class,
                      /* 4 */ namingType.getDeclaringClass().getName(),
                      /* 5 */ namingType.name(),
                      /* 6 */ commandName,
                      /* 7 */ methodName);
                  return null;
                }

                @Override
                public Void visitEntityCtType(EntityCtType ctType, Boolean optional) {
                  iprint(
                      "%1$s<%2$s> __command = __support.getCommandImplementors().create%6$s(%7$s, __query, new %3$s<%4$s>(%5$s));%n",
                      /* 1 */ commandClass,
                      /* 2 */ resultBoxedType,
                      /* 3 */ EntityResultListHandler.class,
                      /* 4 */ ctType.getType(),
                      /* 5 */ ctType.getTypeCode(),
                      /* 6 */ commandName,
                      /* 7 */ methodName);
                  return null;
                }

                @Override
                public Void visitOptionalCtType(OptionalCtType ctType, Boolean __) {
                  return ctType.getElementCtType().accept(this, true);
                }

                @Override
                public Void visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p) {
                  iprint(
                      "%1$s<%2$s> __command = __support.getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
                      /* 1 */ commandClass,
                      /* 2 */ resultBoxedType,
                      /* 3 */ OptionalIntResultListHandler.class,
                      /* 4 */ commandName,
                      /* 5 */ methodName);
                  return null;
                }

                @Override
                public Void visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p) {
                  iprint(
                      "%1$s<%2$s> __command = __support.getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
                      /* 1 */ commandClass,
                      /* 2 */ resultBoxedType,
                      /* 3 */ OptionalLongResultListHandler.class,
                      /* 4 */ commandName,
                      /* 5 */ methodName);
                  return null;
                }

                @Override
                public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p) {
                  iprint(
                      "%1$s<%2$s> __command = __support.getCommandImplementors().create%4$s(%5$s, __query, new %3$s());%n",
                      /* 1 */ commandClass,
                      /* 2 */ resultBoxedType,
                      /* 3 */ OptionalDoubleResultListHandler.class,
                      /* 4 */ commandName,
                      /* 5 */ methodName);
                  return null;
                }
              },
              false);
      return null;
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Boolean __) {
      ctType
          .getElementCtType()
          .accept(
              new StreamElementCtTypeVisitor(
                  m,
                  resultBoxedType,
                  commandClass,
                  commandName,
                  Function.class.getName() + ".identity()"),
              false);
      return null;
    }

    @SuppressWarnings("rawtypes")
    private Class<? extends ScalarSingleResultHandler> getBasicSingleResultHandler(
        Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicSingleResultHandler.class;
      }
      return BasicSingleResultHandler.class;
    }

    @SuppressWarnings("rawtypes")
    private Class<? extends ScalarResultListHandler> getBasicResultListHandler(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalBasicResultListHandler.class;
      }
      return BasicResultListHandler.class;
    }

    @SuppressWarnings("rawtypes")
    private Class<? extends ScalarSingleResultHandler> getDomainSingleResultHandler(
        Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainSingleResultHandler.class;
      }
      return DomainSingleResultHandler.class;
    }

    @SuppressWarnings("rawtypes")
    private Class<? extends ScalarResultListHandler> getDomainResultListHandler(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalDomainResultListHandler.class;
      }
      return DomainResultListHandler.class;
    }

    private Class<? extends AbstractSingleResultHandler<?>> getMapSingleResultHandler(
        Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalMapSingleResultHandler.class;
      }
      return MapSingleResultHandler.class;
    }

    @SuppressWarnings("rawtypes")
    private Class<? extends AbstractSingleResultHandler> getEntitySingleResultHandler(
        Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalEntitySingleResultHandler.class;
      }
      return EntitySingleResultHandler.class;
    }
  }
}
