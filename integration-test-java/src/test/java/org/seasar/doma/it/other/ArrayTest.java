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
package org.seasar.doma.it.other;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.dao.SalEmpDao;
import org.seasar.doma.it.dao.SalEmpDaoImpl;
import org.seasar.doma.it.entity.SalEmp;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
@Run(
    unless = {
      Dbms.HSQLDB,
      Dbms.H2,
      Dbms.MYSQL,
      Dbms.MYSQL8,
      Dbms.ORACLE,
      Dbms.DB2,
      Dbms.SQLSERVER,
      Dbms.SQLITE
    })
public class ArrayTest {

  @Test
  public void testSelect(Config config) throws Exception {
    SalEmpDao dao = new SalEmpDaoImpl(config);
    List<SalEmp> entities = dao.selectAll();
    assertEquals(2, entities.size());
    SalEmp entity = entities.get(0);
    Integer[] array = (Integer[]) entity.getPayByQuarter().getArray();
    assertArrayEquals(new Integer[] {10000, 10000, 10000, 10000}, array);
    entity = entities.get(1);
    array = (Integer[]) entity.getPayByQuarter().getArray();
    assertArrayEquals(new Integer[] {20000, 25000, 25000, 25000}, array);
  }

  @Test
  public void testSelect_2DimensionalArray(Config config) throws Exception {
    SalEmpDao dao = new SalEmpDaoImpl(config);
    List<SalEmp> entities = dao.selectAll();
    assertEquals(2, entities.size());
    SalEmp entity = entities.get(0);
    String[][] array = (String[][]) entity.getSchedule().getArray();
    assertEquals(2, array.length);
    assertEquals(2, array[0].length);
    assertEquals(2, array[1].length);
    assertEquals("meeting", array[0][0]);
    assertEquals("lunch", array[0][1]);
    assertEquals("training", array[1][0]);
    assertEquals("presentation", array[1][1]);
    entity = entities.get(1);
    array = (String[][]) entity.getSchedule().getArray();
    assertEquals(2, array.length);
    assertEquals(2, array[0].length);
    assertEquals(2, array[1].length);
    assertEquals("breakfast", array[0][0]);
    assertEquals("consulting", array[0][1]);
    assertEquals("meeting", array[1][0]);
    assertEquals("lunch", array[1][1]);
  }

  @Test
  public void testInsert(Config config) throws Exception {
    SalEmpDao dao = new SalEmpDaoImpl(config);
    Integer[] array = new Integer[] {10, 20, 30, 40};
    SalEmp entity = new SalEmp();
    entity.setName("hoge");
    entity.setPayByQuarter(dao.createIntegerArray(array));
    dao.insert(entity);
    List<SalEmp> entities = dao.selectAll();
    assertEquals(3, entities.size());
    entity = entities.get(2);
    assertArrayEquals(array, (Integer[]) entity.getPayByQuarter().getArray());
  }

  @Test
  public void testInsert_2DimensionalArray(Config config) throws Exception {
    SalEmpDao dao = new SalEmpDaoImpl(config);
    String[][] array = new String[][] {{"aaa", "bbb"}, {"ccc", "ddd"}};
    SalEmp entity = new SalEmp();
    entity.setName("hoge");
    entity.setSchedule(dao.createString2DArray(array));
    dao.insert(entity);
    List<SalEmp> entities = dao.selectAll();
    assertEquals(3, entities.size());
    entity = entities.get(2);
    array = (String[][]) entity.getSchedule().getArray();
    assertEquals(2, array.length);
    assertEquals(2, array[0].length);
    assertEquals(2, array[1].length);
    assertEquals("aaa", array[0][0]);
    assertEquals("bbb", array[0][1]);
    assertEquals("ccc", array[1][0]);
    assertEquals("ddd", array[1][1]);
  }

  @Test
  public void testUpdate(Config config) throws Exception {
    SalEmpDao dao = new SalEmpDaoImpl(config);
    List<SalEmp> entities = dao.selectAll();
    assertEquals(2, entities.size());
    SalEmp entity = entities.get(0);
    Integer[] array = (Integer[]) entity.getPayByQuarter().getArray();
    assertEquals(4, array.length);
    array[0] = 10;
    entity.setPayByQuarter(dao.createIntegerArray(array));
    dao.update(entity);

    entities = dao.selectAll();
    entity = entities.get(0);
    assertEquals(Integer.valueOf(10), ((Integer[]) entity.getPayByQuarter().getArray())[0]);
  }

  @Test
  public void testUpdate_2DimensionalArray(Config config) throws Exception {
    SalEmpDao dao = new SalEmpDaoImpl(config);
    List<SalEmp> entities = dao.selectAll();
    assertEquals(2, entities.size());
    SalEmp entity = entities.get(0);
    String[][] array = (String[][]) entity.getSchedule().getArray();
    assertEquals(2, array.length);
    assertEquals(2, array[0].length);
    assertEquals(2, array[1].length);
    array[0][0] = "aaa";
    entity.setSchedule(dao.createString2DArray(array));
    dao.update(entity);

    entities = dao.selectAll();
    entity = entities.get(0);
    assertEquals("aaa", ((String[][]) entity.getSchedule().getArray())[0][0]);
  }
}
