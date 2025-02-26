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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.dao.RoomDao;
import org.seasar.doma.it.dao.RoomDaoImpl;
import org.seasar.doma.it.entity.Room;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.domain.DomainTypeFactory;

@ExtendWith(IntegrationTestEnvironment.class)
public class StringArrayTest {

  @Test
  public void testGetExternalDomainType(Config config) throws Exception {
    DomainType<Object, String[]> domainType =
        DomainTypeFactory.getExternalDomainType(String[].class, new ClassHelper() {});
    Scalar<Object, String[]> scalar = domainType.createScalar();
    assertNotNull(scalar);
  }

  @Test
  @Run(onlyIf = {Dbms.H2})
  public void insert(Config config) {
    RoomDao dao = new RoomDaoImpl(config);
    Room room = new Room();
    room.id = 1;
    room.colors = new String[] {"red", "blue"};
    dao.insert(room);

    Room room2 = dao.selectById(1);
    assertNotNull(room2);
    assertNotNull(room2.colors);
    assertEquals(2, room2.colors.length);
    assertEquals("red", room2.colors[0]);
    assertEquals("blue", room2.colors[1]);
  }

  @Test
  @Run(onlyIf = {Dbms.H2})
  public void selectByColors(Config config) {
    RoomDao dao = new RoomDaoImpl(config);
    Room room = new Room();
    room.id = 1;
    room.colors = new String[] {"red", "blue"};
    dao.insert(room);

    Room room2 = dao.selectByColors(new String[] {"red", "blue"});
    assertNotNull(room2);
    assertNotNull(room2.colors);
    assertEquals(2, room2.colors.length);
    assertEquals("red", room2.colors[0]);
    assertEquals("blue", room2.colors[1]);
  }
}
