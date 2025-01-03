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
package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

public class Parent3EntityListener implements EntityListener<Parent3Entity> {

  @Override
  public void preInsert(Parent3Entity entity, PreInsertContext<Parent3Entity> context) {}

  @Override
  public void preUpdate(Parent3Entity entity, PreUpdateContext<Parent3Entity> context) {}

  @Override
  public void preDelete(Parent3Entity entity, PreDeleteContext<Parent3Entity> context) {}

  @Override
  public void postInsert(Parent3Entity entity, PostInsertContext<Parent3Entity> context) {}

  @Override
  public void postUpdate(Parent3Entity entity, PostUpdateContext<Parent3Entity> context) {}

  @Override
  public void postDelete(Parent3Entity entity, PostDeleteContext<Parent3Entity> context) {}
}
