package org.seasar.doma.it.other;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.SalEmpDao;
import org.seasar.doma.it.entity.SalEmp;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

@RunWith(Seasar2.class)
@Prerequisite("#ENV not in {'hsqldb', 'h2', 'mysql', 'oracle', 'db2', 'mssql2008', 'sqlite'}")
public class ArrayTest {

    @Test
    public void testSelect() throws Exception {
        SalEmpDao dao = SalEmpDao.get();
        List<SalEmp> entities = dao.selectAll();
        assertEquals(2, entities.size());
        SalEmp entity = entities.get(0);
        Integer[] array = (Integer[]) entity.getPayByQuarter().getArray();
        assertTrue(Arrays.equals(new Integer[] { 10000, 10000, 10000, 10000 },
                array));
        entity = entities.get(1);
        array = (Integer[]) entity.getPayByQuarter().getArray();
        assertTrue(Arrays.equals(new Integer[] { 20000, 25000, 25000, 25000 },
                array));
    }

    @Test
    public void testSelect_2DimesionalArray() throws Exception {
        SalEmpDao dao = SalEmpDao.get();
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
    public void testInsert() throws Exception {
        SalEmpDao dao = SalEmpDao.get();
        Integer[] array = new Integer[] { 10, 20, 30, 40 };
        SalEmp entity = new SalEmp();
        entity.setName("hoge");
        entity.setPayByQuarter(dao.createIntegerArray(array));
        dao.insert(entity);
        List<SalEmp> entities = dao.selectAll();
        assertEquals(3, entities.size());
        entity = entities.get(2);
        assertTrue(Arrays.equals(array, (Integer[]) entity.getPayByQuarter()
                .getArray()));
    }

    @Test
    public void testInsert_2DimesionalArray() throws Exception {
        SalEmpDao dao = SalEmpDao.get();
        String[][] array = new String[][] { { "aaa", "bbb" }, { "ccc", "ddd" } };
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
    public void testUpdate() throws Exception {
        SalEmpDao dao = SalEmpDao.get();
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
        assertEquals(new Integer(10), ((Integer[]) entity.getPayByQuarter()
                .getArray())[0]);
    }

    @Test
    public void testUpdate_2DimesionalArray() throws Exception {
        SalEmpDao dao = SalEmpDao.get();
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
        assertEquals("aaa",
                ((String[][]) entity.getSchedule().getArray())[0][0]);
    }
}
