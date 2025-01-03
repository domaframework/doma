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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class InsertContext implements Context {
  public final EntityMetamodel<?> entityMetamodel;
  public final List<EntityMetamodel<?>> entityMetamodels;
  public final Map<Operand.Prop, Operand.Param> values = new LinkedHashMap<>();
  public final InsertSettings settings;
  public SelectContext selectContext;
  public OnDuplicateContext onDuplicateContext = new OnDuplicateContext();

  public InsertContext(EntityMetamodel<?> entityMetamodel) {
    this(entityMetamodel, new InsertSettings());
  }

  public InsertContext(EntityMetamodel<?> entityMetamodel, InsertSettings settings) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entityMetamodels = Collections.singletonList(entityMetamodel);
    this.settings = Objects.requireNonNull(settings);
  }

  public class OnDuplicateContext {
    public DuplicateKeyType duplicateKeyType = DuplicateKeyType.EXCEPTION;
    public List<PropertyMetamodel<?>> keys = Collections.emptyList();
    public final Map<Operand.Prop, Operand> setValues = new LinkedHashMap<>();
  }

  @Override
  public List<EntityMetamodel<?>> getEntityMetamodels() {
    return entityMetamodels;
  }

  @Override
  public InsertSettings getSettings() {
    return settings;
  }
}
