package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.box;
import static org.seasar.doma.internal.apt.generator.CodeHelper.entityDesc;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import java.util.function.Function;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.FetchType;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.ScalarCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.StreamCtType;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.parameter.CallableSqlParameterMetaVisitor;
import org.seasar.doma.internal.apt.meta.parameter.EntityListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.EntityResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.MapListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.MapResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.ScalarInOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.ScalarInParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.ScalarListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.ScalarOutParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.ScalarResultListParameterMeta;
import org.seasar.doma.internal.apt.meta.parameter.ScalarSingleResultParameterMeta;
import org.seasar.doma.internal.apt.meta.query.AbstractCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.query.ArrayCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoFunctionQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoModuleQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoProcedureQueryMeta;
import org.seasar.doma.internal.apt.meta.query.DefaultQueryMeta;
import org.seasar.doma.internal.apt.meta.query.NonAbstractQueryMeta;
import org.seasar.doma.internal.apt.meta.query.QueryMeta;
import org.seasar.doma.internal.apt.meta.query.QueryMetaVisitor;
import org.seasar.doma.internal.apt.meta.query.QueryParameterMeta;
import org.seasar.doma.internal.apt.meta.query.QueryReturnMeta;
import org.seasar.doma.internal.apt.meta.query.SqlProcessorQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlTemplateBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlTemplateModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlTemplateSelectQueryMeta;
import org.seasar.doma.internal.apt.meta.query.StaticScriptQueryMeta;
import org.seasar.doma.internal.jdbc.command.EntityCollectorHandler;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityStreamHandler;
import org.seasar.doma.internal.jdbc.command.MapCollectorHandler;
import org.seasar.doma.internal.jdbc.command.MapResultListHandler;
import org.seasar.doma.internal.jdbc.command.MapSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.MapStreamHandler;
import org.seasar.doma.internal.jdbc.command.OptionalEntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.OptionalMapSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.ScalarCollectorHandler;
import org.seasar.doma.internal.jdbc.command.ScalarResultListHandler;
import org.seasar.doma.internal.jdbc.command.ScalarSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.ScalarStreamHandler;
import org.seasar.doma.internal.jdbc.sql.EntityListParameter;
import org.seasar.doma.internal.jdbc.sql.EntityResultListParameter;
import org.seasar.doma.internal.jdbc.sql.MapListParameter;
import org.seasar.doma.internal.jdbc.sql.MapResultListParameter;
import org.seasar.doma.internal.jdbc.sql.ScalarInOutParameter;
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter;
import org.seasar.doma.internal.jdbc.sql.ScalarListParameter;
import org.seasar.doma.internal.jdbc.sql.ScalarOutParameter;
import org.seasar.doma.internal.jdbc.sql.ScalarResultListParameter;
import org.seasar.doma.internal.jdbc.sql.ScalarSingleResultParameter;

public class DaoImplMethodBodyGenerator implements QueryMetaVisitor<String> {

  private final CodeSpec codeSpec;

  private final Printer printer;

  private final DaoMeta daoMeta;

  public DaoImplMethodBodyGenerator(CodeSpec codeSpec, Printer printer, DaoMeta daoMeta) {
    assertNotNull(codeSpec, printer, daoMeta);
    this.codeSpec = codeSpec;
    this.printer = printer;
    this.daoMeta = daoMeta;
  }

  @Override
  public void visitSqlTemplateSelectQueryMeta(
      final SqlTemplateSelectQueryMeta m, String methodName) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    printer.iprint(
        "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getQueryClass().getSimpleName(),
        /* 3 */ methodName);
    printer.iprint("__query.setMethod(%1$s);%n", methodName);
    printer.iprint("__query.setConfig(__config);%n");
    if (m.getSelectOptionsCtType() != null) {
      printer.iprint("__query.setOptions(%1$s);%n", m.getSelectOptionsParameterName());
    }
    if (m.getEntityCtType() != null) {
      printer.iprint("__query.setEntityDesc(%1$s);%n", entityDesc(m.getEntityCtType()));
    }

    printAddParameterStatements(m.getParameterMetas());

    printer.iprint("__query.setCallerClassName(\"%1$s\");%n", codeSpec);
    printer.iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    printer.iprint("__query.setResultEnsured(%1$s);%n", m.getEnsureResult());
    printer.iprint("__query.setResultMappingEnsured(%1$s);%n", m.getEnsureResultMapping());
    if (m.getSelectStrategyType() == SelectType.RETURN) {
      printer.iprint(
          "__query.setFetchType(%1$s.%2$s);%n",
          /* 1 */ FetchType.class.getName(), /* 2 */ FetchType.LAZY);
    } else {
      printer.iprint(
          "__query.setFetchType(%1$s.%2$s);%n",
          /* 1 */ FetchType.class.getName(), /* 2 */ m.getFetchType());
    }
    printer.iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    printer.iprint("__query.setMaxRows(%1$s);%n", m.getMaxRows());
    printer.iprint("__query.setFetchSize(%1$s);%n", m.getFetchSize());
    printer.iprint(
        "__query.setSqlLogType(%1$s.%2$s);%n",
        /* 1 */ m.getSqlLogType().getClass().getName(), /* 2 */ m.getSqlLogType());
    if (m.isResultStream()) {
      printer.iprint("__query.setResultStream(true);%n");
    }
    printer.iprint("__query.prepare();%n");

    var returnMeta = m.getReturnMeta();

    if (m.getSelectStrategyType() == SelectType.RETURN) {
      var returnCtType = returnMeta.getCtType();
      returnCtType.accept(new ReturnStrategyGenerator(m, methodName), false);
      printer.iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
      printer.iprint("__query.complete();%n");
      printer.iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", codeSpec, m.getName());
      printer.iprint("return __result;%n");
    } else {
      if (m.getSelectStrategyType() == SelectType.STREAM) {
        var functionCtType = m.getFunctionCtType();
        functionCtType.getTargetCtType().accept(new StreamStrategyGenerator(m, methodName), null);
      } else if (m.getSelectStrategyType() == SelectType.COLLECT) {
        var collectorCtType = m.getCollectorCtType();
        collectorCtType
            .getTargetCtType()
            .accept(new CollectStrategyGenerator(m, methodName), false);
      }
      if ("void".equals(returnMeta.getTypeName())) {
        printer.iprint("__command.execute();%n");
        printer.iprint("__query.complete();%n");
        printer.iprint("exiting(\"%1$s\", \"%2$s\", null);%n", codeSpec, m.getName());
      } else {
        printer.iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
        printer.iprint("__query.complete();%n");
        printer.iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", codeSpec, m.getName());
        printer.iprint("return __result;%n");
      }
    }

    printThrowingStatements(m);
  }

  @Override
  public void visitStaticScriptQueryMeta(StaticScriptQueryMeta m, String methodName) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    printer.iprint(
        "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getQueryClass().getSimpleName(),
        /* 3 */ methodName);
    printer.iprint("__query.setMethod(%1$s);%n", methodName);
    printer.iprint("__query.setConfig(__config);%n");
    printer.iprint("__query.setCallerClassName(\"%1$s\");%n", codeSpec);
    printer.iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    printer.iprint("__query.setBlockDelimiter(\"%1$s\");%n", m.getBlockDelimiter());
    printer.iprint("__query.setHaltOnError(%1$s);%n", m.getHaltOnError());
    printer.iprint(
        "__query.setSqlLogType(%1$s.%2$s);%n",
        /* 1 */ m.getSqlLogType().getClass().getName(), /* 2 */ m.getSqlLogType());
    printer.iprint("__query.prepare();%n");
    printer.iprint(
        "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getCommandClass().getSimpleName(),
        /* 3 */ methodName);
    printer.iprint("__command.execute();%n");
    printer.iprint("__query.complete();%n");
    printer.iprint("exiting(\"%1$s\", \"%2$s\", null);%n", codeSpec, m.getName());

    printThrowingStatements(m);
  }

  @Override
  public void visitAutoModifyQueryMeta(AutoModifyQueryMeta m, String methodName) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    printer.iprint(
        "%1$s<%2$s> __query = getQueryImplementors().create%4$s(%5$s, %3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getEntityCtType().getTypeName(),
        /* 3 */ entityDesc(m.getEntityCtType()),
        /* 4 */ m.getQueryClass().getSimpleName(),
        /* 5 */ methodName);
    printer.iprint("__query.setMethod(%1$s);%n", methodName);
    printer.iprint("__query.setConfig(__config);%n");
    printer.iprint("__query.setEntity(%1$s);%n", m.getEntityParameterName());
    printer.iprint("__query.setCallerClassName(\"%1$s\");%n", codeSpec);
    printer.iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    printer.iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    printer.iprint(
        "__query.setSqlLogType(%1$s.%2$s);%n",
        /* 1 */ m.getSqlLogType().getClass().getName(), /* 2 */ m.getSqlLogType());

    var excludeNull = m.getExcludeNull();
    if (excludeNull != null) {
      printer.iprint("__query.setNullExcluded(%1$s);%n", excludeNull);
    }

    var ignoreVersion = m.getIgnoreVersion();
    if (ignoreVersion != null) {
      printer.iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
    }

    var include = m.getInclude();
    if (include != null) {
      printer.iprint("__query.setIncludedPropertyNames(%1$s);%n", toCSVFormat(include));
    }

    var exclude = m.getExclude();
    if (exclude != null) {
      printer.iprint("__query.setExcludedPropertyNames(%1$s);%n", toCSVFormat(m.getExclude()));
    }

    var includeUnchanged = m.getIncludeUnchanged();
    if (includeUnchanged != null) {
      printer.iprint("__query.setUnchangedPropertyIncluded(%1$s);%n", includeUnchanged);
    }

    var suppressOptimisticLockException = m.getSuppressOptimisticLockException();
    if (suppressOptimisticLockException != null) {
      printer.iprint(
          "__query.setOptimisticLockExceptionSuppressed(%1$s);%n", suppressOptimisticLockException);
    }

    printer.iprint("__query.prepare();%n");
    printer.iprint(
        "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getCommandClass().getSimpleName(),
        /* 3 */ methodName);

    var entityCtType = m.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      printer.iprint("int __count = __command.execute();%n");
      printer.iprint("__query.complete();%n");
      printer.iprint(
          "%1$s __result = new %1$s(__count, __query.getEntity());%n",
          m.getReturnMeta().getTypeName());
    } else {
      printer.iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getTypeName());
      printer.iprint("__query.complete();%n");
    }

    printer.iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", codeSpec, m.getName());
    printer.iprint("return __result;%n");

    printThrowingStatements(m);
  }

  @Override
  public void visitSqlTemplateModifyQueryMeta(SqlTemplateModifyQueryMeta m, String methodName) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    printer.iprint(
        "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getQueryClass().getSimpleName(),
        /* 3 */ methodName);
    printer.iprint("__query.setMethod(%1$s);%n", methodName);
    printer.iprint("__query.setConfig(__config);%n");

    printAddParameterStatements(m.getParameterMetas());

    printer.iprint("__query.setCallerClassName(\"%1$s\");%n", codeSpec);
    printer.iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    printer.iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    printer.iprint(
        "__query.setSqlLogType(%1$s.%2$s);%n",
        /* 1 */ m.getSqlLogType().getClass().getName(), /* 2 */ m.getSqlLogType());

    if (m.getEntityParameterName() != null && m.getEntityCtType() != null) {
      printer.iprint(
          "__query.setEntityAndEntityDesc(\"%1$s\", %2$s, %3$s);%n",
          /* 1 */ m.getEntityParameterName(),
          /* 2 */ m.getEntityParameterName(),
          /* 3 */ entityDesc(m.getEntityCtType()));
    }

    var excludeNull = m.getExcludeNull();
    if (excludeNull != null) {
      printer.iprint("__query.setNullExcluded(%1$s);%n", excludeNull);
    }

    var ignoreVersion = m.getIgnoreVersion();
    if (ignoreVersion != null) {
      printer.iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
    }

    var include = m.getInclude();
    if (include != null) {
      printer.iprint("__query.setIncludedPropertyNames(%1$s);%n", toCSVFormat(include));
    }

    var exclude = m.getExclude();
    if (exclude != null) {
      printer.iprint("__query.setExcludedPropertyNames(%1$s);%n", toCSVFormat(m.getExclude()));
    }

    var includeUnchanged = m.getIncludeUnchanged();
    if (includeUnchanged != null) {
      printer.iprint("__query.setUnchangedPropertyIncluded(%1$s);%n", includeUnchanged);
    }

    var suppressOptimisticLockException = m.getSuppressOptimisticLockException();
    if (suppressOptimisticLockException != null) {
      printer.iprint(
          "__query.setOptimisticLockExceptionSuppressed(%1$s);%n", suppressOptimisticLockException);
    }

    printer.iprint("__query.prepare();%n");
    printer.iprint(
        "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getCommandClass().getSimpleName(),
        /* 3 */ methodName);

    var entityCtType = m.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      printer.iprint("int __count = __command.execute();%n");
      printer.iprint("__query.complete();%n");
      printer.iprint(
          "%1$s __result = new %1$s(__count, __query.getEntity(%2$s.class));%n",
          /* 1 */ m.getReturnMeta().getTypeName(), /* 2 */ entityCtType.getQualifiedName());
    } else {
      printer.iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getTypeName());
      printer.iprint("__query.complete();%n");
    }

    printer.iprint(
        "exiting(\"%1$s\", \"%2$s\", __result);%n", /* 1 */ codeSpec, /* 2 */ m.getName());
    printer.iprint("return __result;%n");

    printThrowingStatements(m);
  }

  @Override
  public void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m, String methodName) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    printer.iprint(
        "%1$s<%2$s> __query = getQueryImplementors().create%4$s(%5$s, %3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getEntityCtType().getTypeName(),
        /* 3 */ entityDesc(m.getEntityCtType()),
        /* 4 */ m.getQueryClass().getSimpleName(),
        /* 5 */ methodName);
    printer.iprint("__query.setMethod(%1$s);%n", methodName);
    printer.iprint("__query.setConfig(__config);%n");
    printer.iprint("__query.setEntities(%1$s);%n", m.getEntitiesParameterName());
    printer.iprint("__query.setCallerClassName(\"%1$s\");%n", codeSpec);
    printer.iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    printer.iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    printer.iprint("__query.setBatchSize(%1$s);%n", m.getBatchSize());
    printer.iprint(
        "__query.setSqlLogType(%1$s.%2$s);%n",
        /* 1 */ m.getSqlLogType().getClass().getName(), /* 2 */ m.getSqlLogType());

    var ignoreVersion = m.getIgnoreVersion();
    if (ignoreVersion != null) {
      printer.iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
    }

    var include = m.getInclude();
    if (include != null) {
      printer.iprint("__query.setIncludedPropertyNames(%1$s);%n", toCSVFormat(include));
    }

    var exclude = m.getExclude();
    if (exclude != null) {
      printer.iprint("__query.setExcludedPropertyNames(%1$s);%n", toCSVFormat(exclude));
    }

    var suppressOptimisticLockException = m.getSuppressOptimisticLockException();
    if (suppressOptimisticLockException != null) {
      printer.iprint(
          "__query.setOptimisticLockExceptionSuppressed(%1$s);%n", suppressOptimisticLockException);
    }

    printer.iprint("__query.prepare();%n");
    printer.iprint(
        "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getCommandClass().getSimpleName(),
        /* 3 */ methodName);

    var entityCtType = m.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      printer.iprint("int[] __counts = __command.execute();%n");
      printer.iprint("__query.complete();%n");
      printer.iprint(
          "%1$s __result = new %1$s(__counts, __query.getEntities());%n",
          m.getReturnMeta().getTypeName());
    } else {
      printer.iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getTypeName());
      printer.iprint("__query.complete();%n");
    }

    printer.iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", codeSpec, m.getName());
    printer.iprint("return __result;%n");

    printThrowingStatements(m);
  }

  @Override
  public void visitSqlTemplateBatchModifyQueryMeta(
      SqlTemplateBatchModifyQueryMeta m, String methodName) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    printer.iprint(
        "%1$s<%2$s> __query = getQueryImplementors().create%4$s(%5$s, %3$s.class);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getElementCtType().getTypeName(),
        /* 3 */ m.getElementCtType().getQualifiedName(),
        /* 4 */ m.getQueryClass().getSimpleName(),
        /* 5 */ methodName);
    printer.iprint("__query.setMethod(%1$s);%n", methodName);
    printer.iprint("__query.setConfig(__config);%n");
    printer.iprint("__query.setElements(%1$s);%n", m.getElementsParameterName());
    printer.iprint("__query.setParameterName(\"%1$s\");%n", m.getElementsParameterName());
    printer.iprint("__query.setCallerClassName(\"%1$s\");%n", codeSpec);
    printer.iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    printer.iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    printer.iprint("__query.setBatchSize(%1$s);%n", m.getBatchSize());
    printer.iprint(
        "__query.setSqlLogType(%1$s.%2$s);%n",
        /* 1 */ m.getSqlLogType().getClass().getName(), /* 2 */ m.getSqlLogType());

    if (m.getEntityCtType() != null) {
      printer.iprint("__query.setEntityDesc(%1$s);%n", entityDesc(m.getEntityCtType()));
    }

    var ignoreVersion = m.getIgnoreVersion();
    if (ignoreVersion != null) {
      printer.iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
    }

    var include = m.getInclude();
    if (include != null) {
      printer.iprint("__query.setIncludedPropertyNames(%1$s);%n", toCSVFormat(include));
    }

    var exclude = m.getExclude();
    if (exclude != null) {
      printer.iprint("__query.setExcludedPropertyNames(%1$s);%n", toCSVFormat(exclude));
    }

    var suppressOptimisticLockException = m.getSuppressOptimisticLockException();
    if (suppressOptimisticLockException != null) {
      printer.iprint(
          "__query.setOptimisticLockExceptionSuppressed(%1$s);%n", suppressOptimisticLockException);
    }

    printer.iprint("__query.prepare();%n");
    printer.iprint(
        "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getCommandClass().getSimpleName(),
        /* 3 */ methodName);

    var entityCtType = m.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      printer.iprint("int[] __counts = __command.execute();%n");
      printer.iprint("__query.complete();%n");
      printer.iprint(
          "%1$s __result = new %1$s(__counts, __query.getEntities());%n",
          m.getReturnMeta().getTypeName());
    } else {
      printer.iprint("%1$s __result = __command.execute();%n", m.getReturnMeta().getTypeName());
      printer.iprint("__query.complete();%n");
    }

    printer.iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", codeSpec, m.getName());
    printer.iprint("return __result;%n");

    printThrowingStatements(m);
  }

  @Override
  public void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m, String methodName) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    var returnMeta = m.getReturnMeta();
    printer.iprint(
        "%1$s<%2$s> __query = getQueryImplementors().create%3$s(%4$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ box(returnMeta.getTypeName()),
        /* 3 */ m.getQueryClass().getSimpleName(),
        /* 4 */ methodName);

    printer.iprint("__query.setMethod(%1$s);%n", methodName);
    printer.iprint("__query.setConfig(__config);%n");
    printer.iprint("__query.setCatalogName(\"%1$s\");%n", m.getCatalogName());
    printer.iprint("__query.setSchemaName(\"%1$s\");%n", m.getSchemaName());
    printer.iprint("__query.setFunctionName(\"%1$s\");%n", m.getFunctionName());
    printer.iprint("__query.setQuoteRequired(%1$s);%n", m.isQuoteRequired());
    var parameterGenerator = new CallableSqlParameterStatementGenerator();
    m.getResultParameterMeta().accept(parameterGenerator, m);
    for (var parameterMeta : m.getCallableSqlParameterMetas()) {
      parameterMeta.accept(parameterGenerator, m);
    }
    printer.iprint("__query.setCallerClassName(\"%1$s\");%n", codeSpec);
    printer.iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    printer.iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    printer.iprint(
        "__query.setSqlLogType(%1$s.%2$s);%n",
        /* 1 */ m.getSqlLogType().getClass().getName(), /* 2 */ m.getSqlLogType());
    printer.iprint("__query.prepare();%n");
    printer.iprint(
        "%1$s<%2$s> __command = getCommandImplementors().create%3$s(%4$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ box(returnMeta.getTypeName()),
        /* 3 */ m.getCommandClass().getSimpleName(),
        /* 4 */ methodName);
    printer.iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
    printer.iprint("__query.complete();%n");
    printer.iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", codeSpec, m.getName());
    printer.iprint("return __result;%n");

    printThrowingStatements(m);
  }

  @Override
  public void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m, String methodName) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    printer.iprint(
        "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
        m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
    printer.iprint("__query.setMethod(%1$s);%n", methodName);
    printer.iprint("__query.setConfig(__config);%n");
    printer.iprint("__query.setCatalogName(\"%1$s\");%n", m.getCatalogName());
    printer.iprint("__query.setSchemaName(\"%1$s\");%n", m.getSchemaName());
    printer.iprint("__query.setProcedureName(\"%1$s\");%n", m.getProcedureName());
    printer.iprint("__query.setQuoteRequired(%1$s);%n", m.isQuoteRequired());
    var parameterGenerator = new CallableSqlParameterStatementGenerator();
    for (var parameterMeta : m.getCallableSqlParameterMetas()) {
      parameterMeta.accept(parameterGenerator, m);
    }
    printer.iprint("__query.setCallerClassName(\"%1$s\");%n", codeSpec);
    printer.iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    printer.iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
    printer.iprint(
        "__query.setSqlLogType(%1$s.%2$s);%n",
        /* 1 */ m.getSqlLogType().getClass().getName(), /* 2 */ m.getSqlLogType());
    printer.iprint("__query.prepare();%n");
    printer.iprint(
        "%1$s __command = getCommandImplementors().create%2$s(%3$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getCommandClass().getSimpleName(),
        /* 3 */ methodName);
    printer.iprint("__command.execute();%n");
    printer.iprint("__query.complete();%n");
    printer.iprint("exiting(\"%1$s\", \"%2$s\", null);%n", codeSpec, m.getName());

    printThrowingStatements(m);
  }

  @Override
  public void visitAbstractCreateQueryMeta(AbstractCreateQueryMeta m, String methodName) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    var returnMeta = m.getReturnMeta();
    printer.iprint(
        "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getQueryClass().getSimpleName(),
        /* 3 */ methodName);
    printer.iprint("__query.setMethod(%1$s);%n", methodName);
    printer.iprint("__query.setConfig(__config);%n");
    printer.iprint("__query.setCallerClassName(\"%1$s\");%n", codeSpec);
    printer.iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    printer.iprint("__query.prepare();%n");
    printer.iprint(
        "%1$s<%2$s> __command = getCommandImplementors().create%3$s(%4$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ returnMeta.getTypeName(),
        /* 3 */ m.getCommandClass().getSimpleName(),
        /* 4 */ methodName);
    printer.iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
    printer.iprint("__query.complete();%n");
    printer.iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", codeSpec, m.getName());
    printer.iprint("return __result;%n");

    printThrowingStatements(m);
  }

  @Override
  public void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m, String methodName) {
    printArrayCreateEnteringStatements(m);
    printPrerequisiteStatements(m);

    var returnMeta = m.getReturnMeta();
    printer.iprint(
        "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
        /* 1 */ m.getQueryClass().getName(),
        /* 2 */ m.getQueryClass().getSimpleName(),
        /* 3 */ methodName);
    printer.iprint("__query.setMethod(%1$s);%n", methodName);
    printer.iprint("__query.setConfig(__config);%n");
    printer.iprint("__query.setCallerClassName(\"%1$s\");%n", codeSpec);
    printer.iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    printer.iprint("__query.setTypeName(\"%1$s\");%n", m.getArrayTypeName());
    printer.iprint("__query.setElements(%1$s);%n", m.getParameterName());
    printer.iprint("__query.prepare();%n");
    printer.iprint(
        "%1$s<%2$s> __command = getCommandImplementors().create%3$s(%4$s, __query);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ box(returnMeta.getTypeName()),
        /* 3 */ m.getCommandClass().getSimpleName(),
        /* 4 */ methodName);
    printer.iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
    printer.iprint("__query.complete();%n");
    printer.iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", codeSpec, m.getName());
    printer.iprint("return __result;%n");

    printThrowingStatements(m);
  }

  @Override
  public void visitDefaultQueryMeta(DefaultQueryMeta m, String methodName) {
    printEnteringStatements(m);

    var returnMeta = m.getReturnMeta();
    if ("void".equals(returnMeta.getTypeName())) {
      printer.iprint("Object __result = null;%n");
      printer.iprint("");
    } else {
      printer.iprint("%1$s __result = ", returnMeta.getTypeName());
    }
    printer.print("%1$s.super.%2$s(", daoMeta.getDaoElement().getQualifiedName(), m.getName());
    for (var it = m.getParameterMetas().iterator(); it.hasNext(); ) {
      var parameterMeta = it.next();
      printer.print("%1$s", parameterMeta.getName());
      if (it.hasNext()) {
        printer.print(", ");
      }
    }
    printer.print(");%n");
    printer.iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", codeSpec, m.getName());
    if (!"void".equals(returnMeta.getTypeName())) {
      printer.iprint("return __result;%n");
    }

    printThrowingStatements(m);
  }

  @Override
  public void visitSqlProcessorQueryMeta(SqlProcessorQueryMeta m, String methodName) {
    printEnteringStatements(m);
    printPrerequisiteStatements(m);

    printer.iprint(
        "%1$s __query = getQueryImplementors().create%2$s(%3$s);%n",
        m.getQueryClass().getName(), m.getQueryClass().getSimpleName(), methodName);
    printer.iprint("__query.setMethod(%1$s);%n", methodName);
    printer.iprint("__query.setConfig(__config);%n");

    printAddParameterStatements(m.getParameterMetas());

    printer.iprint("__query.setCallerClassName(\"%1$s\");%n", codeSpec);
    printer.iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
    printer.iprint("__query.prepare();%n");

    var returnMeta = m.getReturnMeta();
    printer.iprint(
        "%1$s<%2$s> __command = getCommandImplementors().create%3$s(%4$s, __query, %5$s);%n",
        /* 1 */ m.getCommandClass().getName(),
        /* 2 */ m.getBiFunctionCtType().getResultCtType().getTypeName(),
        /* 3 */ m.getCommandClass().getSimpleName(),
        /* 4 */ methodName,
        /* 5 */ m.getBiFunctionParameterName());

    if ("void".equals(returnMeta.getTypeName())) {
      printer.iprint("__command.execute();%n");
      printer.iprint("__query.complete();%n");
      printer.iprint("exiting(\"%1$s\", \"%2$s\", null);%n", codeSpec, m.getName());
    } else {
      printer.iprint("%1$s __result = __command.execute();%n", returnMeta.getTypeName());
      printer.iprint("__query.complete();%n");
      printer.iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", codeSpec, m.getName());
      printer.iprint("return __result;%n");
    }

    printThrowingStatements(m);
  }

  @Override
  public void visitNonAbstractQueryMeta(NonAbstractQueryMeta m, String s) {
    printEnteringStatements(m);

    var returnMeta = m.getReturnMeta();
    if ("void".equals(returnMeta.getTypeName())) {
      printer.iprint("Object __result = null;%n");
      printer.iprint("");
    } else {
      printer.iprint("%1$s __result = ", returnMeta.getTypeName());
    }
    printer.print("super.%1$s(", m.getName());
    for (var it = m.getParameterMetas().iterator(); it.hasNext(); ) {
      var parameterMeta = it.next();
      printer.print("%1$s", parameterMeta.getName());
      if (it.hasNext()) {
        printer.print(", ");
      }
    }
    printer.print(");%n");
    printer.iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", codeSpec, m.getName());
    if (!"void".equals(returnMeta.getTypeName())) {
      printer.iprint("return __result;%n");
    }

    printThrowingStatements(m);
  }

  private void printEnteringStatements(QueryMeta m) {
    printer.iprint("entering(\"%1$s\", \"%2$s\"", codeSpec, m.getName());
    for (var parameterMeta : m.getParameterMetas()) {
      printer.print(", %1$s", parameterMeta.getName());
    }
    printer.print(");%n");
    printer.iprint("try {%n");
    printer.indent();
  }

  private void printArrayCreateEnteringStatements(ArrayCreateQueryMeta m) {
    printer.iprint(
        "entering(\"%1$s\", \"%2$s\", (Object)%3$s);%n",
        codeSpec, m.getName(), m.getParameterName());
    printer.iprint("try {%n");
    printer.indent();
  }

  private void printThrowingStatements(QueryMeta m) {
    printer.unindent();
    printer.iprint("} catch (%1$s __e) {%n", RuntimeException.class.getName());
    printer.indent();
    printer.iprint("throwing(\"%1$s\", \"%2$s\", __e);%n", codeSpec, m.getName());
    printer.iprint("throw __e;%n");
    printer.unindent();
    printer.iprint("}%n");
  }

  void printPrerequisiteStatements(QueryMeta m) {
    for (var parameterMeta : m.getParameterMetas()) {
      if (parameterMeta.isNullable()) {
        continue;
      }
      var paramName = parameterMeta.getName();
      printer.iprint("if (%1$s == null) {%n", paramName);
      printer.iprint(
          "    throw new %1$s(\"%2$s\");%n",
          /* 1 */ DomaNullPointerException.class.getName(), /* 2 */ paramName);
      printer.iprint("}%n");
    }
  }

  void printAddParameterStatements(List<QueryParameterMeta> ParameterMetas) {
    for (var parameterMeta : ParameterMetas) {
      if (parameterMeta.isBindable()) {
        var ctType = parameterMeta.getCtType();
        ctType.accept(
            new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

              @Override
              protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
                printer.iprint(
                    "__query.addParameter(\"%1$s\", %2$s.class, %1$s);%n",
                    /* 1 */ parameterMeta.getName(), /* 2 */ ctType.getQualifiedName());
                return null;
              }

              @Override
              public Void visitOptionalCtType(OptionalCtType ctType, Void p)
                  throws RuntimeException {
                printer.iprint(
                    "__query.addParameter(\"%1$s\", %2$s.class, %1$s.orElse(null));%n",
                    /* 1 */ parameterMeta.getName(),
                    /* 2 */ ctType.getElementCtType().getQualifiedName());
                return null;
              }

              @Override
              public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                  throws RuntimeException {
                printer.iprint(
                    "__query.addParameter(\"%1$s\", %2$s.class, %1$s.isPresent() ? %1$s.getAsInt() : null);%n",
                    /* 1 */ parameterMeta.getName(), /* 2 */ Integer.class.getName());
                return null;
              }

              @Override
              public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
                  throws RuntimeException {
                printer.iprint(
                    "__query.addParameter(\"%1$s\", %2$s.class, %1$s.isPresent() ? %1$s.getAsLong() : null);%n",
                    /* 1 */ parameterMeta.getName(), /* 2 */ Long.class.getName());
                return null;
              }

              @Override
              public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
                  throws RuntimeException {
                printer.iprint(
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
    final var buf = new StringBuilder();
    if (values.size() > 0) {
      for (var value : values) {
        buf.append("\"");
        buf.append(value);
        buf.append("\", ");
      }
      buf.setLength(buf.length() - 2);
    }
    return buf.toString();
  }

  private class CallableSqlParameterStatementGenerator
      implements CallableSqlParameterMetaVisitor<AutoModuleQueryMeta> {

    @Override
    public void visitScalarListParameterMeta(
        final ScalarListParameterMeta m, AutoModuleQueryMeta p) {
      printer.iprint(
          "__query.addParameter(new %1$s<>(%2$s, %3$s, \"%3$s\"));%n",
          /* 1 */ ScalarListParameter.class.getName(),
          /* 2 */ m.getScalarCtType().accept(new ScalarSupplierCodeBuilder(), m.isOptional()),
          /* 3 */ m.getName());
    }

    @Override
    public void visitEntityListParameterMeta(EntityListParameterMeta m, AutoModuleQueryMeta p) {
      var entityCtType = m.getEntityCtType();
      printer.iprint(
          "__query.addParameter(new %1$s<%2$s>(%3$s, %4$s, \"%4$s\", %5$s));%n",
          /* 1 */ EntityListParameter.class.getName(),
          /* 2 */ entityCtType.getTypeName(),
          /* 3 */ entityDesc(entityCtType),
          /* 4 */ m.getName(),
          /* 5 */ m.getEnsureResultMapping());
    }

    @Override
    public void visitMapListParameterMeta(MapListParameterMeta m, AutoModuleQueryMeta p) {
      var namingType = p.getMapKeyNamingType();
      printer.iprint(
          "__query.addParameter(new %1$s(%2$s.%3$s, %4$s, \"%4$s\"));%n",
          /* 1 */ MapListParameter.class.getName(),
          /* 2 */ namingType.getDeclaringClass().getName(),
          /* 3 */ namingType.name(),
          /* 4 */ m.getName());
    }

    @Override
    public void visitScalarInOutParameterMeta(
        final ScalarInOutParameterMeta m, AutoModuleQueryMeta p) {
      printer.iprint(
          "__query.addParameter(new %1$s<>(%2$s, %3$s));%n",
          /* 1 */ ScalarInOutParameter.class.getName(),
          /* 2 */ m.getScalarCtType().accept(new ScalarSupplierCodeBuilder(), m.isOptional()),
          /* 3 */ m.getName());
    }

    @Override
    public void visitScalarOutParameterMeta(final ScalarOutParameterMeta m, AutoModuleQueryMeta p) {
      printer.iprint(
          "__query.addParameter(new %1$s<>(%2$s, %3$s));%n",
          /* 1 */ ScalarOutParameter.class.getName(),
          /* 2 */ m.getScalarCtType().accept(new ScalarSupplierCodeBuilder(), m.isOptional()),
          /* 3 */ m.getName());
    }

    @Override
    public void visitScalarInParameterMeta(final ScalarInParameterMeta m, AutoModuleQueryMeta p) {
      printer.iprint(
          "__query.addParameter(new %1$s<>(%2$s, %3$s));%n",
          /* 1 */ ScalarInParameter.class.getName(),
          /* 2 */ m.getScalarCtType().accept(new ScalarSupplierCodeBuilder(), m.isOptional()),
          /* 3 */ m.getName());
    }

    @Override
    public void visitScalarResultListParameterMeta(
        ScalarResultListParameterMeta m, AutoModuleQueryMeta p) {
      printer.iprint(
          "__query.setResultParameter(new %1$s<>(%2$s));%n",
          /* 1 */ ScalarResultListParameter.class.getName(),
          /* 2 */ m.getScalarCtType().accept(new ScalarSupplierCodeBuilder(), m.isOptional()));
    }

    @Override
    public void visitEntityResultListParameterMeta(
        EntityResultListParameterMeta m, AutoModuleQueryMeta p) {
      var entityCtType = m.getEntityCtType();
      printer.iprint(
          "__query.setResultParameter(new %1$s<%2$s>(%3$s, %4$s));%n",
          /* 1 */ EntityResultListParameter.class.getName(),
          /* 2 */ entityCtType.getTypeName(),
          /* 3 */ entityDesc(entityCtType),
          /* 4 */ m.getEnsureResultMapping());
    }

    @Override
    public void visitMapResultListParameterMeta(
        MapResultListParameterMeta m, AutoModuleQueryMeta p) {
      var namingType = p.getMapKeyNamingType();
      printer.iprint(
          "__query.setResultParameter(new %1$s(%2$s.%3$s));%n",
          /* 1 */ MapResultListParameter.class.getName(),
          /* 2 */ namingType.getDeclaringClass().getName(),
          /* 3 */ namingType.name());
    }

    @Override
    public void visitScalarSingleResultParameterMeta(
        ScalarSingleResultParameterMeta m, AutoModuleQueryMeta p) {
      printer.iprint(
          "__query.setResultParameter(new %1$s<>(%2$s));%n",
          /* 1 */ ScalarSingleResultParameter.class.getName(),
          /* 2 */ m.getScalarCtType().accept(new ScalarSupplierCodeBuilder(), m.isOptional()));
    }
  }

  private class StreamStrategyGenerator extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlTemplateSelectQueryMeta m;

    private final String methodName;

    private final QueryReturnMeta returnMeta;

    private final String commandClassName;

    private final String commandName;

    private final String functionParamName;

    private StreamStrategyGenerator(SqlTemplateSelectQueryMeta m, String methodName) {
      this.m = m;
      this.methodName = methodName;
      this.returnMeta = m.getReturnMeta();
      this.commandClassName = m.getCommandClass().getName();
      this.commandName = m.getCommandClass().getSimpleName();
      this.functionParamName = m.getFunctionParameterName();
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Void p) throws RuntimeException {
      ctType
          .getElementCtType()
          .accept(
              new StreamHandlerGenerator(
                  m,
                  methodName,
                  returnMeta.getTypeName(),
                  commandClassName,
                  commandName,
                  functionParamName),
              false);
      return null;
    }
  }

  private class StreamHandlerGenerator
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    private final SqlTemplateSelectQueryMeta m;

    private final String methodName;

    private final String returnBoxedTypeName;

    private final String commandClassName;

    private final String commandName;

    private final String functionParamName;

    private StreamHandlerGenerator(
        SqlTemplateSelectQueryMeta m,
        String methodName,
        String returnTypeName,
        String commandClassName,
        String commandName,
        String functionParamName) {
      this.m = m;
      this.methodName = methodName;
      this.returnBoxedTypeName = box(returnTypeName);
      this.commandClassName = commandClassName;
      this.commandName = commandName;
      this.functionParamName = functionParamName;
    }

    @Override
    public Void visitScalarCtType(ScalarCtType ctType, Boolean p) throws RuntimeException {
      printer.iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<>(%4$s, %5$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ returnBoxedTypeName,
          /* 3 */ ScalarStreamHandler.class.getName(),
          /* 4 */ ctType.accept(new ScalarSupplierCodeBuilder(), p),
          /* 5 */ functionParamName,
          /* 6 */ commandName,
          /* 7 */ methodName);
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Boolean optional) throws RuntimeException {
      var namingType = m.getMapKeyNamingType();
      printer.iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<>(%4$s.%5$s, %6$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ returnBoxedTypeName,
          /* 3 */ MapStreamHandler.class.getName(),
          /* 4 */ namingType.getDeclaringClass().getName(),
          /* 5 */ namingType.name(),
          /* 6 */ functionParamName,
          /* 7 */ commandName,
          /* 8 */ methodName);
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Boolean optional) throws RuntimeException {
      printer.iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<>(%4$s, %5$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ returnBoxedTypeName,
          /* 3 */ EntityStreamHandler.class.getName(),
          /* 4 */ entityDesc(ctType),
          /* 5 */ functionParamName,
          /* 6 */ commandName,
          /* 7 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Boolean optional)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, true);
    }
  }

  private class CollectStrategyGenerator
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    private final SqlTemplateSelectQueryMeta m;

    private final String methodName;

    private final QueryReturnMeta returnMeta;

    private final String commandClassName;

    private final String commandName;

    private final String collectorParamName;

    private CollectStrategyGenerator(SqlTemplateSelectQueryMeta m, String methodName) {
      this.m = m;
      this.methodName = methodName;
      this.returnMeta = m.getReturnMeta();
      this.commandClassName = m.getCommandClass().getName();
      this.commandName = m.getCommandClass().getSimpleName();
      this.collectorParamName = m.getCollectorParameterName();
    }

    @Override
    public Void visitScalarCtType(ScalarCtType ctType, Boolean p) throws RuntimeException {
      printer.iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<>(%4$s, %5$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ box(returnMeta.getTypeName()),
          /* 3 */ ScalarCollectorHandler.class.getName(),
          /* 4 */ ctType.accept(new ScalarSupplierCodeBuilder(), p),
          /* 5 */ collectorParamName,
          /* 6 */ commandName,
          /* 7 */ methodName);
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Boolean optional) throws RuntimeException {
      var namingType = m.getMapKeyNamingType();
      printer.iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%7$s(%8$s, __query, new %3$s<>(%4$s.%5$s, %6$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ box(returnMeta.getTypeName()),
          /* 3 */ MapCollectorHandler.class.getName(),
          /* 4 */ namingType.getDeclaringClass().getName(),
          /* 5 */ namingType.name(),
          /* 6 */ collectorParamName,
          /* 7 */ commandName,
          /* 8 */ methodName);
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Boolean optional) throws RuntimeException {
      printer.iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<>(%4$s, %5$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ box(returnMeta.getTypeName()),
          /* 3 */ EntityCollectorHandler.class.getName(),
          /* 4 */ entityDesc(ctType),
          /* 5 */ collectorParamName,
          /* 6 */ commandName,
          /* 7 */ methodName);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Boolean optional)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, true);
    }
  }

  private class ReturnStrategyGenerator
      extends SimpleCtTypeVisitor<Void, Boolean, RuntimeException> {

    private final SqlTemplateSelectQueryMeta m;

    private final String methodName;

    private final String returnTypeName;

    private final String commandClassName;

    private final String commandName;

    private ReturnStrategyGenerator(SqlTemplateSelectQueryMeta m, String methodName) {
      this.m = m;
      this.methodName = methodName;
      this.returnTypeName = m.getReturnMeta().getTypeName();
      this.commandClassName = m.getCommandClass().getName();
      this.commandName = m.getCommandClass().getSimpleName();
    }

    @Override
    public Void visitScalarCtType(ScalarCtType ctType, Boolean p) throws RuntimeException {
      printer.iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%5$s(%6$s, __query, new %3$s<>(%4$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ box(returnTypeName),
          /* 3 */ ScalarSingleResultHandler.class.getName(),
          /* 4 */ ctType.accept(new ScalarSupplierCodeBuilder(), p),
          /* 5 */ commandName,
          /* 6 */ methodName);
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Boolean optional) throws RuntimeException {
      var namingType = m.getMapKeyNamingType();
      printer.iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s(%4$s.%5$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ box(returnTypeName),
          /* 3 */ getMapSingleResultHandlerName(optional),
          /* 4 */ namingType.getDeclaringClass().getName(),
          /* 5 */ namingType.name(),
          /* 6 */ commandName,
          /* 7 */ methodName);
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Boolean optional) throws RuntimeException {
      printer.iprint(
          "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s<>(%4$s));%n",
          /* 1 */ commandClassName,
          /* 2 */ box(returnTypeName),
          /* 3 */ getEntitySingleResultHandlerName(optional),
          /* 4 */ entityDesc(ctType),
          /* 5 */ null,
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
    public Void visitIterableCtType(final IterableCtType iterableCtType, final Boolean __)
        throws RuntimeException {
      iterableCtType
          .getElementCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Boolean, RuntimeException>() {

                @Override
                public Void visitScalarCtType(ScalarCtType ctType, Boolean p)
                    throws RuntimeException {
                  printer.iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%5$s(%6$s, __query, new %3$s<>(%4$s));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ box(returnTypeName),
                      /* 3 */ ScalarResultListHandler.class.getName(),
                      /* 4 */ ctType.accept(new ScalarSupplierCodeBuilder(), p),
                      /* 5 */ commandName,
                      /* 6 */ methodName);
                  return null;
                }

                @Override
                public Void visitMapCtType(MapCtType ctType, Boolean optional)
                    throws RuntimeException {
                  var namingType = m.getMapKeyNamingType();
                  printer.iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%6$s(%7$s, __query, new %3$s(%4$s.%5$s));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ box(returnTypeName),
                      /* 3 */ MapResultListHandler.class.getName(),
                      /* 4 */ namingType.getDeclaringClass().getName(),
                      /* 5 */ namingType.name(),
                      /* 6 */ commandName,
                      /* 7 */ methodName);
                  return null;
                }

                @Override
                public Void visitEntityCtType(EntityCtType ctType, Boolean optional)
                    throws RuntimeException {
                  printer.iprint(
                      "%1$s<%2$s> __command = getCommandImplementors().create%5$s(%6$s, __query, new %3$s<>(%4$s));%n",
                      /* 1 */ commandClassName,
                      /* 2 */ box(returnTypeName),
                      /* 3 */ EntityResultListHandler.class.getName(),
                      /* 4 */ entityDesc(ctType),
                      /* 5 */ commandName,
                      /* 6 */ methodName);
                  return null;
                }

                @Override
                public Void visitOptionalCtType(OptionalCtType ctType, Boolean __)
                    throws RuntimeException {
                  return ctType.getElementCtType().accept(this, true);
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
              new StreamHandlerGenerator(
                  m,
                  methodName,
                  returnTypeName,
                  commandClassName,
                  commandName,
                  Function.class.getName() + ".identity()"),
              false);
      return null;
    }

    private String getMapSingleResultHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalMapSingleResultHandler.class.getName();
      }
      return MapSingleResultHandler.class.getName();
    }

    private String getEntitySingleResultHandlerName(Boolean optional) {
      if (Boolean.TRUE == optional) {
        return OptionalEntitySingleResultHandler.class.getName();
      }
      return EntitySingleResultHandler.class.getName();
    }
  }
}
