package org.seasar.doma.internal.apt.meta;

/** @author taedium */
public interface QueryMetaVisitor<R, P> {

  R visitSqlFileSelectQueryMeta(SqlFileSelectQueryMeta m, P p);

  R visitSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m, P p);

  R visitSqlFileBatchModifyQueryMeta(SqlFileBatchModifyQueryMeta m, P p);

  R visitSqlFileScriptQueryMeta(SqlFileScriptQueryMeta m, P p);

  R visitAutoModifyQueryMeta(AutoModifyQueryMeta m, P p);

  R visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m, P p);

  R visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m, P p);

  R visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m, P p);

  R visitArrayCreateQueryMeta(ArrayCreateQueryMeta m, P p);

  R visitAbstractCreateQueryMeta(AbstractCreateQueryMeta m, P p);

  R visitDefaultQueryMeta(DefaultQueryMeta m, P p);

  R visitSqlProcessorQueryMeta(SqlProcessorQueryMeta m, P p);
}
