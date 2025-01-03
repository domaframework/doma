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
package org.seasar.doma.it.entity;

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class CompKeyDeptListener implements EntityListener<CompKeyDept> {

  @Override
  public void preDelete(CompKeyDept entity, PreDeleteContext<CompKeyDept> context) {
    CompKeyDept newEntity =
        new CompKeyDept(
            entity.departmentId1,
            entity.departmentId2,
            entity.departmentNo,
            entity.departmentName + "_preD",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }

  @Override
  public void preInsert(CompKeyDept entity, PreInsertContext<CompKeyDept> context) {
    CompKeyDept newEntity =
        new CompKeyDept(
            entity.departmentId1,
            entity.departmentId2,
            entity.departmentNo,
            entity.departmentName + "_preI(" + initialLetters(context.getDuplicateKeyType()) + ")",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }

  @Override
  public void preUpdate(CompKeyDept entity, PreUpdateContext<CompKeyDept> context) {
    CompKeyDept newEntity =
        new CompKeyDept(
            entity.departmentId1,
            entity.departmentId2,
            entity.departmentNo,
            entity.departmentName + "_preU",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }

  @Override
  public void postInsert(CompKeyDept entity, PostInsertContext<CompKeyDept> context) {
    CompKeyDept newEntity =
        new CompKeyDept(
            entity.departmentId1,
            entity.departmentId2,
            entity.departmentNo,
            entity.departmentName + "_postI(" + initialLetters(context.getDuplicateKeyType()) + ")",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }

  @Override
  public void postUpdate(CompKeyDept entity, PostUpdateContext<CompKeyDept> context) {
    CompKeyDept newEntity =
        new CompKeyDept(
            entity.departmentId1,
            entity.departmentId2,
            entity.departmentNo,
            entity.departmentName + "_postU",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }

  @Override
  public void postDelete(CompKeyDept entity, PostDeleteContext<CompKeyDept> context) {
    CompKeyDept newEntity =
        new CompKeyDept(
            entity.departmentId1,
            entity.departmentId2,
            entity.departmentNo,
            entity.departmentName + "_postD",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }

  private String initialLetters(DuplicateKeyType duplicateKeyType) {
    return duplicateKeyType.name().substring(0, 1);
  }
}
