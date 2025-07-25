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

import java.lang.reflect.Field;
import java.util.LinkedList;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.jdbc.entity.EntityPropertyAccessException;
import org.seasar.doma.jdbc.entity.EntityPropertyNotFoundException;

public class PropertyField<ENTITY> {

  protected final String path;

  protected final Class<ENTITY> entityClass;

  protected final LinkedList<FieldWrapper> fields = new LinkedList<>();

  public PropertyField(String path, Class<ENTITY> entityClass) {
    this(PropertyPath.of(path), entityClass);
  }

  public PropertyField(PropertyPath path, Class<ENTITY> entityClass) {
    AssertionUtil.assertNotNull(path, entityClass);
    this.path = path.name();
    this.entityClass = entityClass;
    Class<?> clazz = entityClass;
    for (PropertyPathSegment segment : path.segments()) {
      FieldWrapper field = getField(clazz, segment);
      fields.add(field);
      clazz = field.getType();
    }
    AssertionUtil.assertTrue(fields.size() > 0);
  }

  private FieldWrapper getField(Class<?> clazz, PropertyPathSegment segment) {
    Field field = findField(clazz, segment.name());
    if (field == null) {
      throw new EntityPropertyNotFoundException(clazz.getName(), segment.name());
    }
    try {
      return segment.wrapField(field);
    } catch (WrapException wrapException) {
      throw new EntityPropertyAccessException(
          wrapException.getCause(), clazz.getName(), segment.name());
    }
  }

  private Field findField(Class<?> clazz, String name) {
    for (Class<?> cl = clazz; cl != Object.class; cl = cl.getSuperclass()) {
      try {
        return cl.getDeclaredField(name);
      } catch (NoSuchFieldException ignored) {
      }
    }
    return null;
  }

  public Object getValue(ENTITY entity) {
    AssertionUtil.assertNotNull(entity);
    Object value = entity;
    for (FieldWrapper field : fields) {
      if (value == null) {
        break;
      }
      value = getFieldValue(field, value);
    }
    return value;
  }

  private Object getFieldValue(FieldWrapper field, Object target) {
    try {
      return field.get(target);
    } catch (WrapException wrapException) {
      throw new EntityPropertyAccessException(
          wrapException.getCause(), entityClass.getName(), path);
    }
  }

  public void setValue(ENTITY entity, Object value) {
    AssertionUtil.assertNotNull(entity);
    if (fields.size() > 1) {
      throw new UnsupportedOperationException();
    }
    setFieldValue(fields.getFirst(), entity, value);
  }

  private void setFieldValue(FieldWrapper field, ENTITY entity, Object value) {
    try {
      field.set(entity, value);
    } catch (WrapException wrapException) {
      throw new EntityPropertyAccessException(
          wrapException.getCause(), entityClass.getName(), path);
    }
  }

  public boolean isPrimitive() {
    FieldWrapper field = fields.getLast();
    return field.getType().isPrimitive();
  }
}
