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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.AggregateStrategyAnnot;
import org.seasar.doma.internal.apt.generator.Code;

public class AggregateStrategyCtType extends AbstractCtType {

  private final ClassName typeClassName;
  private final AggregateStrategyAnnot aggregateStrategyAnnot;

  AggregateStrategyCtType(
      RoundContext ctx,
      TypeMirror type,
      ClassName typeClassName,
      AggregateStrategyAnnot aggregateStrategyAnnot) {
    super(ctx, type);
    assertNotNull(typeClassName, aggregateStrategyAnnot);
    this.typeClassName = typeClassName;
    this.aggregateStrategyAnnot = aggregateStrategyAnnot;
  }

  public AggregateStrategyAnnot getAggregateStrategyAnnot() {
    return aggregateStrategyAnnot;
  }

  public Code getTypeCode() {
    return new Code(p -> p.print("%1$s.getSingletonInternal()", typeClassName));
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitAggregateStrategyCtType(this, p);
  }
}
