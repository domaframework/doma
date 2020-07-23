package org.seasar.doma.internal.apt.meta.parameter;

public interface CallableSqlParameterMetaVisitor<R, P> {

  R visitBasicInParameterMeta(BasicInParameterMeta m, P p);

  R visitBasicOutParameterMeta(BasicOutParameterMeta m, P p);

  R visitBasicInOutParameterMeta(BasicInOutParameterMeta m, P p);

  R visitBasicListParameterMeta(BasicListParameterMeta m, P p);

  R visitBasicSingleResultParameterMeta(BasicSingleResultParameterMeta m, P p);

  R visitBasicResultListParameterMeta(BasicResultListParameterMeta m, P p);

  R visitDomainInParameterMeta(DomainInParameterMeta m, P p);

  R visitDomainOutParameterMeta(DomainOutParameterMeta m, P p);

  R visitDomainInOutParameterMeta(DomainInOutParameterMeta m, P p);

  R visitDomainListParameterMeta(DomainListParameterMeta m, P p);

  R visitDomainSingleResultParameterMeta(DomainSingleResultParameterMeta m, P p);

  R visitDomainResultListParameterMeta(DomainResultListParameterMeta m, P p);

  R visitEntityListParameterMeta(EntityListParameterMeta m, P p);

  R visitEntityResultListParameterMeta(EntityResultListParameterMeta m, P p);

  R visitMapListParameterMeta(MapListParameterMeta m, P p);

  R visitMapResultListParameterMeta(MapResultListParameterMeta m, P p);

  R visitOptionalBasicInParameterMeta(OptionalBasicInParameterMeta m, P p);

  R visitOptionalBasicOutParameterMeta(OptionalBasicOutParameterMeta m, P p);

  R visitOptionalBasicInOutParameterMeta(OptionalBasicInOutParameterMeta m, P p);

  R visitOptionalBasicListParameterMeta(OptionalBasicListParameterMeta m, P p);

  R visitOptionalBasicSingleResultParameterMeta(OptionalBasicSingleResultParameterMeta m, P p);

  R visitOptionalBasicResultListParameterMeta(OptionalBasicResultListParameterMeta m, P p);

  R visitOptionalDomainInParameterMeta(OptionalDomainInParameterMeta m, P p);

  R visitOptionalDomainOutParameterMeta(OptionalDomainOutParameterMeta m, P p);

  R visitOptionalDomainInOutParameterMeta(OptionalDomainInOutParameterMeta m, P p);

  R visitOptionalDomainListParameterMeta(OptionalDomainListParameterMeta m, P p);

  R visitOptionalDomainSingleResultParameterMeta(OptionalDomainSingleResultParameterMeta m, P p);

  R visitOptionalDomainResultListParameterMeta(OptionalDomainResultListParameterMeta m, P p);

  R visitOptionalIntInParameterMeta(OptionalIntInParameterMeta m, P p);

  R visitOptionalIntOutParameterMeta(OptionalIntOutParameterMeta m, P p);

  R visitOptionalIntInOutParameterMeta(OptionalIntInOutParameterMeta m, P p);

  R visitOptionalIntListParameterMeta(OptionalIntListParameterMeta m, P p);

  R visitOptionalIntSingleResultParameterMeta(OptionalIntSingleResultParameterMeta m, P p);

  R visitOptionalIntResultListParameterMeta(OptionalIntResultListParameterMeta m, P p);

  R visitOptionalLongInParameterMeta(OptionalLongInParameterMeta m, P p);

  R visitOptionalLongOutParameterMeta(OptionalLongOutParameterMeta m, P p);

  R visitOptionalLongInOutParameterMeta(OptionalLongInOutParameterMeta m, P p);

  R visitOptionalLongListParameterMeta(OptionalLongListParameterMeta m, P p);

  R visitOptionalLongSingleResultParameterMeta(OptionalLongSingleResultParameterMeta m, P p);

  R visitOptionalLongResultListParameterMeta(OptionalLongResultListParameterMeta m, P p);

  R visitOptionalDoubleInParameterMeta(OptionalDoubleInParameterMeta m, P p);

  R visitOptionalDoubleOutParameterMeta(OptionalDoubleOutParameterMeta m, P p);

  R visitOptionalDoubleInOutParameterMeta(OptionalDoubleInOutParameterMeta m, P p);

  R visitOptionalDoubleListParameterMeta(OptionalDoubleListParameterMeta m, P p);

  R visitOptionalDoubleSingleResultParameterMeta(OptionalDoubleSingleResultParameterMeta m, P p);

  R visitOptionalDoubleResultListParameterMeta(OptionalDoubleResultListParameterMeta m, P p);
}
