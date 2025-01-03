/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.cttype;

public interface CtTypeVisitor<R, P, TH extends Throwable> {

  R visitAnyCtType(AnyCtType ctType, P p) throws TH;

  R visitBatchResultCtType(BatchResultCtType ctType, P p) throws TH;

  R visitBasicCtType(BasicCtType ctType, P p) throws TH;

  R visitDomainCtType(DomainCtType ctType, P p) throws TH;

  R visitEmbeddableCtType(EmbeddableCtType ctType, P p) throws TH;

  R visitEntityCtType(EntityCtType ctType, P p) throws TH;

  R visitIterableCtType(IterableCtType ctType, P p) throws TH;

  R visitArrayCtType(ArrayCtType ctType, P p) throws TH;

  R visitCollectorCtType(CollectorCtType ctType, P p) throws TH;

  R visitReferenceCtType(ReferenceCtType ctType, P p) throws TH;

  R visitResultCtType(ResultCtType ctType, P p) throws TH;

  R visitSelectOptionsCtType(SelectOptionsCtType ctType, P p) throws TH;

  R visitMapCtType(MapCtType ctType, P p) throws TH;

  R visitNoneCtType(NoneCtType ctType, P p) throws TH;

  R visitOptionalCtType(OptionalCtType ctType, P p) throws TH;

  R visitOptionalIntCtType(OptionalIntCtType ctType, P p) throws TH;

  R visitOptionalLongCtType(OptionalLongCtType ctType, P p) throws TH;

  R visitOptionalDoubleCtType(OptionalDoubleCtType ctType, P p) throws TH;

  R visitFunctionCtType(FunctionCtType ctType, P p) throws TH;

  R visitStreamCtType(StreamCtType ctType, P p) throws TH;

  R visitBiFunctionCtType(BiFunctionCtType ctType, P p) throws TH;

  R visitConfigCtType(ConfigCtType ctType, P p) throws TH;

  R visitPreparedSqlCtType(PreparedSqlCtType ctType, P p) throws TH;

  R visitMultiResultCtType(MultiResultCtType ctType, P p) throws TH;
}
