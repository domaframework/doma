package org.seasar.doma.internal.apt.meta;

/**
 * @author taedium
 * 
 */
public interface QueryMetaVisitor<R, P> {

    R visistSqlFileSelectQueryMeta(SqlFileSelectQueryMeta m, P p);

    R visistSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m, P p);

    R visitSqlFileBatchModifyQueryMeta(SqlFileBatchModifyQueryMeta m, P p);

    R visistAutoModifyQueryMeta(AutoModifyQueryMeta m, P p);

    R visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m, P p);

    R visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m, P p);

    R visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m, P p);

}
