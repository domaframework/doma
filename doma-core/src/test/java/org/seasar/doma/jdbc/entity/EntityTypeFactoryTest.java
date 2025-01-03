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
package org.seasar.doma.jdbc.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import example.entity.Emp;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.ClassHelper;

public class EntityTypeFactoryTest {

  private final ClassHelper classHelper = new ClassHelper() {};

  @Test
  public void testGetEntityType() {
    EntityType<Emp> type = EntityTypeFactory.getEntityType(Emp.class, classHelper);
    assertNotNull(type);
  }

  @Test
  public void testGetEntityType_forNestedEntity() {
    EntityType<NotTopLevelEntity.Hoge> type =
        EntityTypeFactory.getEntityType(NotTopLevelEntity.Hoge.class, classHelper);
    assertNotNull(type);
  }

  @Test
  public void testGetEntityType_DomaIllegalArgumentException() {
    try {
      EntityTypeFactory.getEntityType(Object.class, classHelper);
      fail();
    } catch (DomaIllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testGetEntityType_EntityTypeNotFoundException() {
    try {
      EntityTypeFactory.getEntityType(Dept.class, classHelper);
      fail();
    } catch (EntityTypeNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}
