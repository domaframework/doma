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
package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public interface SubSelectContext<RESULT> {

  SelectContext get();

  class Single<PROPERTY> implements SubSelectContext<PropertyMetamodel<PROPERTY>> {
    private final SelectContext context;
    private final PropertyMetamodel<PROPERTY> propertyMetamodel;

    public Single(SelectContext context, PropertyMetamodel<PROPERTY> propertyMetamodel) {
      this.context = Objects.requireNonNull(context);
      this.propertyMetamodel = Objects.requireNonNull(propertyMetamodel);
    }

    @Override
    public SelectContext get() {
      return context;
    }

    public PropertyMetamodel<PROPERTY> getPropertyMetamodel() {
      return propertyMetamodel;
    }
  }
}
