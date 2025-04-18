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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.Query;

public class EntityResultListParameter<ENTITY> extends AbstractResultListParameter<ENTITY> {

  final EntityType<ENTITY> entityType;
  final boolean resultMappingEnsured;

  public EntityResultListParameter(EntityType<ENTITY> entityType, boolean resultMappingEnsured) {
    super(new ArrayList<>());
    assertNotNull(entityType);
    this.entityType = entityType;
    this.resultMappingEnsured = resultMappingEnsured;
  }

  @Override
  public List<ENTITY> getResult() {
    return list;
  }

  @Override
  public EntityProvider<ENTITY> createObjectProvider(Query query) {
    return new EntityProvider<>(entityType, query, resultMappingEnsured);
  }
}
