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
package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;

class EntityFieldMetaFactory {

  private final RoundContext ctx;

  private final EntityMeta entityMeta;

  private final VariableElement fieldElement;

  public EntityFieldMetaFactory(
      RoundContext ctx, EntityMeta entityMeta, VariableElement fieldElement) {
    assertNotNull(ctx, entityMeta, fieldElement);
    this.ctx = ctx;
    this.entityMeta = entityMeta;
    this.fieldElement = fieldElement;
  }

  public EntityFieldMeta createEntityFieldMeta() {
    var ctType = ctx.getCtTypes().newCtType(fieldElement.asType());
    if (ctType instanceof OptionalCtType optionalCtType) {
      if (optionalCtType.getElementCtType() instanceof EmbeddableCtType embeddableCtType) {
        return createEmbeddedMeta(optionalCtType, embeddableCtType);
      }
    } else if (ctType instanceof EmbeddableCtType embeddableCtType) {
      return createEmbeddedMeta(embeddableCtType, embeddableCtType);
    }
    return createEntityPropertyMeta();
  }

  private EmbeddedMeta createEmbeddedMeta(CtType ctType, EmbeddableCtType embeddableCtType) {
    var factory = new EmbeddedMetaFactory(ctx, fieldElement, ctType, embeddableCtType);
    return factory.createEmbeddedMeta();
  }

  private EntityPropertyMeta createEntityPropertyMeta() {
    var factory = new EntityPropertyMetaFactory(ctx, entityMeta, fieldElement);
    return factory.createEntityPropertyMeta();
  }
}
