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
package org.seasar.doma.jdbc.criteria.command;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;

class EntityKeyTest {

  private final EntityMetamodelImpl entityDef = new EntityMetamodelImpl();

  @Test
  void testEquals() {
    EntityKey key1 = new EntityKey(entityDef, Arrays.asList("a", 1));
    EntityKey key2 = new EntityKey(entityDef, Arrays.asList("a", 1));
    assertEquals(key1, key2);
    assertEquals(key2, key1);
  }

  @Test
  void testEquals_empty() {
    EntityKey key1 = new EntityKey(entityDef, new ArrayList<>());
    EntityKey key2 = new EntityKey(entityDef, new ArrayList<>());
    assertEquals(key1, key2);
    assertEquals(key2, key1);
  }

  @Test
  void testEquals_null() {
    EntityKey key1 = new EntityKey(entityDef, Arrays.asList(null, null));
    EntityKey key2 = new EntityKey(entityDef, Arrays.asList(null, null));
    assertEquals(key1, key2);
    assertEquals(key2, key1);
  }

  @Test
  void testNotEquals() {
    EntityKey key1 = new EntityKey(entityDef, Arrays.asList("a", 1));
    EntityKey key2 = new EntityKey(entityDef, Arrays.asList("a", 2));
    assertNotEquals(key1, key2);
    assertNotEquals(key2, key1);
  }

  @Test
  void testNotEquals_size() {
    EntityKey key1 = new EntityKey(entityDef, Arrays.asList("a", 1));
    EntityKey key2 = new EntityKey(entityDef, Arrays.asList("a", 1, 2));
    assertNotEquals(key1, key2);
    assertNotEquals(key2, key1);
  }

  @Test
  void testHashCode() {
    EntityKey key1 = new EntityKey(entityDef, Arrays.asList("a", 1));
    EntityKey key2 = new EntityKey(entityDef, Arrays.asList("a", 1));
    assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  void testHashCode_empty() {
    EntityKey key1 = new EntityKey(entityDef, new ArrayList<>());
    EntityKey key2 = new EntityKey(entityDef, new ArrayList<>());
    assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  void testHashCode_nulls() {
    EntityKey key1 = new EntityKey(entityDef, Arrays.asList(null, null));
    EntityKey key2 = new EntityKey(entityDef, Arrays.asList(null, null));
    assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  void testNotEquals_notEquals() {
    EntityKey key1 = new EntityKey(entityDef, Arrays.asList("a", 1));
    EntityKey key2 = new EntityKey(entityDef, Arrays.asList("a", 2));
    assertNotEquals(key1, key2);
    assertNotEquals(key1.hashCode(), key2.hashCode());
  }

  static class EntityMetamodelImpl implements EntityMetamodel<Object> {

    @Override
    public EntityType<Object> asType() {
      return null;
    }

    @Override
    public List<PropertyMetamodel<?>> allPropertyMetamodels() {
      return null;
    }
  }
}
