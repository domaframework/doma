package org.seasar.doma.internal.apt.meta.parameter;

/**
 * @author taedium
 * 
 */
public interface CallableSqlParameterMetaVisitor<R, P> {

    R visitScalarInParameterMeta(ScalarInParameterMeta m, P p);

    R visitScalarOutParameterMeta(ScalarOutParameterMeta m, P p);

    R visitScalarInOutParameterMeta(ScalarInOutParameterMeta m, P p);

    R visitScalarListParameterMeta(ScalarListParameterMeta m, P p);

    R visitScalarSingleResultParameterMeta(ScalarSingleResultParameterMeta m, P p);

    R visitScalarResultListParameterMeta(ScalarResultListParameterMeta m, P p);

    R visitEntityListParameterMeta(EntityListParameterMeta m, P p);

    R visitEntityResultListParameterMeta(EntityResultListParameterMeta m, P p);

    R visitMapListParameterMeta(MapListParameterMeta m, P p);

    R visitMapResultListParameterMeta(MapResultListParameterMeta m, P p);

}
