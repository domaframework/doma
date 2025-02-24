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
package org.seasar.doma.internal.apt.processor;

import java.util.Set;
import java.util.function.Function;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.meta.NullElementMeta;

public class MyElementProcessor implements ElementProcessor {

  private final RoundContext ctx;
  private final Function<TypeElement, NullElementMeta> handler;
  private final ElementProcessorSupport<NullElementMeta> support;

  protected MyElementProcessor(RoundContext ctx, Function<TypeElement, NullElementMeta> handler) {
    this.ctx = ctx;
    this.handler = handler;
    this.support = new ElementProcessorSupport<>(ctx, MyAnnotation.class);
  }

  @Override
  public void process(Set<? extends Element> elements) {
    support.processTypeElements(elements, handler);
  }
}
