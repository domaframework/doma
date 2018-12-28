/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.util.ListIterator;
import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/**
 * @author taedium
 * @param <ELEMENT> リストの要素
 */
public class SqlFileBatchDeleteQuery<ELEMENT> extends SqlFileBatchModifyQuery<ELEMENT>
    implements BatchDeleteQuery {

  protected EntityHandler entityHandler;

  protected boolean versionIgnored;

  protected boolean optimisticLockExceptionSuppressed;

  public SqlFileBatchDeleteQuery(Class<ELEMENT> elementClass) {
    super(elementClass, SqlKind.BATCH_DELETE);
  }

  @Override
  public void prepare() {
    super.prepare();
    int size = elements.size();
    if (size == 0) {
      return;
    }
    executable = true;
    sqlExecutionSkipCause = null;
    currentEntity = elements.get(0);
    preDelete();
    prepareSqlFile();
    prepareOptions();
    prepareOptimisticLock();
    prepareSql();
    elements.set(0, currentEntity);
    for (ListIterator<ELEMENT> it = elements.listIterator(1); it.hasNext(); ) {
      currentEntity = it.next();
      preDelete();
      prepareSql();
      it.set(currentEntity);
    }
    assertEquals(size, sqls.size());
  }

  protected void preDelete() {
    if (entityHandler != null) {
      entityHandler.preDelete();
    }
  }

  protected void prepareOptimisticLock() {
    if (entityHandler != null) {
      entityHandler.prepareOptimisticLock();
    }
  }

  @Override
  public void complete() {
    if (entityHandler != null) {
      for (ListIterator<ELEMENT> it = elements.listIterator(); it.hasNext(); ) {
        currentEntity = it.next();
        entityHandler.postDelete();
        it.set(currentEntity);
      }
    }
  }

  @Override
  public void setEntityType(EntityType<ELEMENT> entityType) {
    entityHandler = new EntityHandler(entityType);
  }

  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored = versionIgnored;
  }

  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  protected class EntityHandler {

    protected EntityType<ELEMENT> entityType;

    protected VersionPropertyType<? super ELEMENT, ELEMENT, ?, ?> versionPropertyType;

    protected EntityHandler(EntityType<ELEMENT> entityType) {
      assertNotNull(entityType);
      this.entityType = entityType;
      this.versionPropertyType = entityType.getVersionPropertyType();
    }

    protected void preDelete() {
      SqlFileBatchPreDeleteContext<ELEMENT> context =
          new SqlFileBatchPreDeleteContext<ELEMENT>(entityType, method, config);
      entityType.preDelete(currentEntity, context);
      if (context.getNewEntity() != null) {
        currentEntity = context.getNewEntity();
      }
    }

    protected void postDelete() {
      SqlFileBatchPostDeleteContext<ELEMENT> context =
          new SqlFileBatchPostDeleteContext<ELEMENT>(entityType, method, config);
      entityType.postDelete(currentEntity, context);
      if (context.getNewEntity() != null) {
        currentEntity = context.getNewEntity();
      }
    }

    protected void prepareOptimisticLock() {
      if (versionPropertyType != null && !versionIgnored) {
        if (!optimisticLockExceptionSuppressed) {
          optimisticLockCheckRequired = true;
        }
      }
    }
  }

  protected static class SqlFileBatchPreDeleteContext<E> extends AbstractPreDeleteContext<E> {

    public SqlFileBatchPreDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }

  protected static class SqlFileBatchPostDeleteContext<E> extends AbstractPostDeleteContext<E> {

    public SqlFileBatchPostDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }
}
