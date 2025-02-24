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
package org.seasar.doma.it.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.AnimalDao;
import org.seasar.doma.it.dao.AnimalDaoImpl;
import org.seasar.doma.it.entity.Animal;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class AgeTest {

  @Test
  public void test(Config config) {
    AnimalDao dao = new AnimalDaoImpl(config);
    Animal animal = new Animal();
    animal.id = 1;
    animal.name = "a";
    animal.age = new Age(10);
    dao.insert(animal);
    Animal newAnimal = dao.selectById(1);
    assertEquals(new Age(10), newAnimal.age);
  }

  @Test
  public void test_null(Config config) {
    AnimalDao dao = new AnimalDaoImpl(config);
    Animal animal = new Animal();
    animal.id = 1;
    animal.name = "a";
    animal.age = null;
    dao.insert(animal);
    Animal newAnimal = dao.selectById(1);
    assertNull(newAnimal.age);
  }
}
