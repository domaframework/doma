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
package org.seasar.doma.internal.jdbc.entity;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityPropertyNotDefinedException;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.PostUpdateContext;

public abstract class AbstractPostUpdateContext<E> extends AbstractEntityListenerContext<E>
    implements PostUpdateContext<E> {

  protected AbstractPostUpdateContext(EntityType<E> entityType, Method method, Config config) {
    super(entityType, method, config);
  }

  protected void validatePropertyDefined(String propertyName) {
    assertNotNull(propertyName);
    if (!isPropertyDefinedInternal(propertyName)) {
      throw new EntityPropertyNotDefinedException(
          entityType.getEntityClass().getName(), propertyName);
    }
  }
}
