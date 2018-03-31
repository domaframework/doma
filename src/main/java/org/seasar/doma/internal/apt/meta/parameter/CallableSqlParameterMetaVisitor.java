package org.seasar.doma.internal.apt.meta.parameter;

/** @author taedium */
public interface CallableSqlParameterMetaVisitor<P> {

  void visitScalarInParameterMeta(ScalarInParameterMeta m, P p);

  void visitScalarOutParameterMeta(ScalarOutParameterMeta m, P p);

  void visitScalarInOutParameterMeta(ScalarInOutParameterMeta m, P p);

  void visitScalarListParameterMeta(ScalarListParameterMeta m, P p);

  void visitScalarSingleResultParameterMeta(ScalarSingleResultParameterMeta m, P p);

  void visitScalarResultListParameterMeta(ScalarResultListParameterMeta m, P p);

  void visitEntityListParameterMeta(EntityListParameterMeta m, P p);

  void visitEntityResultListParameterMeta(EntityResultListParameterMeta m, P p);

  void visitMapListParameterMeta(MapListParameterMeta m, P p);

  void visitMapResultListParameterMeta(MapResultListParameterMeta m, P p);
}
