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

import java.util.Objects;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.DomainConverters;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.meta.NullElementMeta;
import org.seasar.doma.message.Message;

public class DomainConvertersProcessor implements ElementProcessor {

  private final RoundContext ctx;
  private final ElementProcessorSupport<NullElementMeta> support;

  public DomainConvertersProcessor(RoundContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
    this.support = new ElementProcessorSupport<>(ctx, DomainConverters.class);
  }

  @Override
  public void process(Set<? extends Element> elements) {
    support.processTypeElements(elements, this::validateEach);
  }

  private NullElementMeta validateEach(TypeElement typeElement) {
    var convertersMirror = ctx.getAnnotations().newDomainConvertersAnnot(typeElement);
    for (var convType : convertersMirror.getValueValue()) {
      var convElement = ctx.getMoreTypes().toTypeElement(convType);
      if (convElement == null) {
        continue;
      }
      if (convElement.getAnnotation(ExternalDomain.class) == null) {
        throw new AptException(
            Message.DOMA4196,
            typeElement,
            convertersMirror.getAnnotationMirror(),
            new Object[] {convElement.getQualifiedName()});
      }
    }
    return NullElementMeta.INSTANCE;
  }
}
