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

import org.seasar.doma.internal.apt.cttype.CtType;

/**
 * Represents metadata for entity fields during annotation processing. This sealed interface is
 * implemented by {@link EmbeddedMeta} for embedded properties and {@link EntityPropertyMeta} for
 * regular entity properties.
 */
public sealed interface EntityFieldMeta permits EmbeddedMeta, EntityPropertyMeta {

  /**
   * Returns the name of the field.
   *
   * @return the field name
   */
  String getName();

  /**
   * Returns the compile-time type information for this field.
   *
   * @return the compile-time type
   */
  CtType getCtType();
}
