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
package org.seasar.doma.jdbc.criteria.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.seasar.doma.jdbc.criteria.metamodel.DefaultPropertyMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public class NoIdEmp_ implements EntityMetamodel<NoIdEmp> {

  private final _NoIdEmp entityType = new _NoIdEmp();

  public final PropertyMetamodel<Integer> id =
      new DefaultPropertyMetamodel<>(Integer.class, entityType, "id");

  public final PropertyMetamodel<String> name =
      new DefaultPropertyMetamodel<>(String.class, entityType, "name");

  public final PropertyMetamodel<BigDecimal> salary =
      new DefaultPropertyMetamodel<>(BigDecimal.class, entityType, "salary");

  public final PropertyMetamodel<Integer> version =
      new DefaultPropertyMetamodel<>(Integer.class, entityType, "version");

  public _NoIdEmp asType() {
    return entityType;
  }

  @Override
  public List<PropertyMetamodel<?>> allPropertyMetamodels() {
    return Arrays.asList(id, name, salary, version);
  }
}
