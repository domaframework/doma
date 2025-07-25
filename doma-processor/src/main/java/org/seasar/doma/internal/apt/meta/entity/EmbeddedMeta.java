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

import org.seasar.doma.internal.apt.annot.EmbeddedAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;

/**
 * Represents metadata for an embedded property during annotation processing.
 *
 * <p>This record contains all the necessary information about a property annotated with {@link
 * org.seasar.doma.Embedded} that is discovered during compile-time annotation processing. It
 * encapsulates both the embeddable type metadata and the specific configuration applied through the
 * {@code @Embedded} annotation.
 *
 * <p>This metadata is used by the annotation processor to:
 *
 * <ul>
 *   <li>Generate proper entity metamodel classes
 *   <li>Validate embedded property configurations
 *   <li>Generate code for column mapping
 * </ul>
 *
 * @param name the name of the embedded property field
 * @param embeddableMeta metadata of the embeddable type being embedded
 * @param ctType compile-time type information for the property
 * @param embeddableCtType compile-time type information for the embeddable type
 * @param embeddedAnnot the {@code @Embedded} annotation instance with its configuration
 */
public record EmbeddedMeta(
    String name,
    EmbeddableMeta embeddableMeta,
    CtType ctType,
    EmbeddableCtType embeddableCtType,
    EmbeddedAnnot embeddedAnnot)
    implements EntityFieldMeta, EmbeddableFieldMeta {

  @Override
  public String getName() {
    return name;
  }

  @Override
  public CtType getCtType() {
    return ctType;
  }

  public boolean optional() {
    return ctType instanceof OptionalCtType;
  }
}
