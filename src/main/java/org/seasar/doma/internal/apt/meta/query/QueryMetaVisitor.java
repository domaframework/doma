package org.seasar.doma.internal.apt.meta.query;

/**
 * @author taedium
 * 
 */
public interface QueryMetaVisitor<P> {

    void visitSqlFileSelectQueryMeta(SqlFileSelectQueryMeta m, P p);

    void visitSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m, P p);

    void visitSqlFileBatchModifyQueryMeta(SqlFileBatchModifyQueryMeta m, P p);

    void visitSqlFileScriptQueryMeta(SqlFileScriptQueryMeta m, P p);

    void visitAutoModifyQueryMeta(AutoModifyQueryMeta m, P p);

    void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m, P p);

    void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m, P p);

    void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m, P p);

    void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m, P p);

    void visitAbstractCreateQueryMeta(AbstractCreateQueryMeta m, P p);

    void visitDefaultQueryMeta(DefaultQueryMeta m, P p);

    void visitSqlProcessorQueryMeta(SqlProcessorQueryMeta m, P p);
}
