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

import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import org.seasar.doma.Scope;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.meta.NullElementMeta;
import org.seasar.doma.message.Message;

public class ScopeProcessor implements ElementProcessor<NullElementMeta> {

  private final RoundContext ctx;
  private final ElementProcessorSupport<NullElementMeta> support;

  public ScopeProcessor(RoundContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
    this.support = new ElementProcessorSupport<>(ctx, Scope.class);
  }

  @Override
  public List<NullElementMeta> process(Set<? extends Element> elements) {
    return support.processMethodElements(elements, this::validateEach);
  }

  private NullElementMeta validateEach(ExecutableElement method) {
    if (method.getParameters().isEmpty()) {
      throw new AptException(Message.DOMA4457, method, new Object[] {});
    }
    var modifiers = method.getModifiers();
    if (modifiers.contains(Modifier.STATIC)) {
      throw new AptException(Message.DOMA4458, method, new Object[] {});
    }
    if (!modifiers.contains(Modifier.PUBLIC)) {
      throw new AptException(Message.DOMA4459, method, new Object[] {});
    }
    return NullElementMeta.INSTANCE;
  }
}
