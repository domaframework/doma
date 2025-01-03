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
package org.seasar.doma.jdbc;

import java.util.function.Supplier;
import org.seasar.doma.jdbc.entity.EntityListener;

/**
 * A provider for an {@link EntityListener} object.
 *
 * @author backpaper0
 */
public interface EntityListenerProvider {

  /**
   * Returns an {@link EntityListener} object.
   *
   * <p>This method must not return {@code null}.
   *
   * @param listenerClass the implementation class of {@link EntityListener}
   * @param listenerSupplier the {@link Supplier} object that provides an {@link EntityListener}
   *     object
   * @param <ENTITY> the entity type
   * @param <LISTENER> the entity listener type
   * @return an {@link EntityListener} object
   */
  default <ENTITY, LISTENER extends EntityListener<ENTITY>> LISTENER get(
      Class<LISTENER> listenerClass, Supplier<LISTENER> listenerSupplier) {
    return listenerSupplier.get();
  }
}
