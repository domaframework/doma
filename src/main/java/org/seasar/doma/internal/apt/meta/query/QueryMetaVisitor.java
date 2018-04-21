package org.seasar.doma.internal.apt.meta.query;

public interface QueryMetaVisitor<P> {

  void visitSqlTemplateSelectQueryMeta(SqlTemplateSelectQueryMeta m, P p);

  void visitSqlTemplateModifyQueryMeta(SqlTemplateModifyQueryMeta m, P p);

  void visitSqlTemplateBatchModifyQueryMeta(SqlTemplateBatchModifyQueryMeta m, P p);

  void visitStaticScriptQueryMeta(StaticScriptQueryMeta m, P p);

  void visitAutoModifyQueryMeta(AutoModifyQueryMeta m, P p);

  void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m, P p);

  void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m, P p);

  void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m, P p);

  void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m, P p);

  void visitAbstractCreateQueryMeta(AbstractCreateQueryMeta m, P p);

  void visitDefaultQueryMeta(DefaultQueryMeta m, P p);

  void visitSqlProcessorQueryMeta(SqlProcessorQueryMeta m, P p);

  void visitNonAbstractQueryMeta(NonAbstractQueryMeta m, P p);
}
