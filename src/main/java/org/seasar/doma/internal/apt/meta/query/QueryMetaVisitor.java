package org.seasar.doma.internal.apt.meta.query;

public interface QueryMetaVisitor<R> {

  R visitSqlFileSelectQueryMeta(SqlFileSelectQueryMeta m);

  R visitSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m);

  R visitSqlFileBatchModifyQueryMeta(SqlFileBatchModifyQueryMeta m);

  R visitSqlFileScriptQueryMeta(SqlFileScriptQueryMeta m);

  R visitAutoModifyQueryMeta(AutoModifyQueryMeta m);

  R visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m);

  R visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m);

  R visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m);

  R visitArrayCreateQueryMeta(ArrayCreateQueryMeta m);

  R visitBlobCreateQueryMeta(BlobCreateQueryMeta m);

  R visitClobCreateQueryMeta(ClobCreateQueryMeta m);

  R visitNClobCreateQueryMeta(NClobCreateQueryMeta m);

  R visitSQLXMLCreateQueryMeta(SQLXMLCreateQueryMeta m);

  R visitDefaultQueryMeta(DefaultQueryMeta m);

  R visitSqlProcessorQueryMeta(SqlProcessorQueryMeta m);
}
