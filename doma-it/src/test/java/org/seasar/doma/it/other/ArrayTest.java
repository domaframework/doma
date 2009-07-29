package org.seasar.doma.it.other;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.SalEmpDao;
import org.seasar.doma.it.dao.SalEmpDao_;
import org.seasar.doma.it.entity.SalEmp;
import org.seasar.doma.it.entity.SalEmp_;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

@RunWith(Seasar2.class)
@Prerequisite("#ENV not in {'hsqldb', 'mysql', 'oracle'}")
public class ArrayTest {

    public void testSelect() throws Exception {
        SalEmpDao dao = new SalEmpDao_();
        List<SalEmp> entities = dao.selectAll();
        assertEquals(2, entities.size());
        SalEmp entity = entities.get(0);
        Integer[] array = entity.pay_by_quarter().getArray();
        assertTrue(Arrays.equals(new Integer[] { 10000, 10000, 10000, 10000 },
                array));
        entity = entities.get(1);
        array = entity.pay_by_quarter().getArray();
        assertTrue(Arrays.equals(new Integer[] { 20000, 25000, 25000, 25000 },
                array));
    }

    public void testSelect_2DimesionalArray() throws Exception {
        SalEmpDao dao = new SalEmpDao_();
        List<SalEmp> entities = dao.selectAll();
        assertEquals(2, entities.size());
        SalEmp entity = entities.get(0);
        String[][] array = entity.schedule().getArray();
        assertEquals(2, array.length);
        assertEquals(2, array[0].length);
        assertEquals(2, array[1].length);
        assertEquals("meeting", array[0][0]);
        assertEquals("lunch", array[0][1]);
        assertEquals("training", array[1][0]);
        assertEquals("presentation", array[1][1]);
        entity = entities.get(1);
        array = entity.schedule().getArray();
        assertEquals(2, array.length);
        assertEquals(2, array[0].length);
        assertEquals(2, array[1].length);
        assertEquals("breakfast", array[0][0]);
        assertEquals("consulting", array[0][1]);
        assertEquals("meeting", array[1][0]);
        assertEquals("lunch", array[1][1]);
    }

    public void testInsert() throws Exception {
        SalEmpDao dao = new SalEmpDao_();
        Integer[] array = new Integer[] { 10, 20, 30, 40 };
        SalEmp entity = new SalEmp_();
        entity.name().set("hoge");
        entity.pay_by_quarter().setDomain(dao.createIntegerArray(array));
        dao.insert(entity);
        List<SalEmp> entities = dao.selectAll();
        assertEquals(3, entities.size());
        entity = entities.get(2);
        assertTrue(Arrays.equals(array, entity.pay_by_quarter().getArray()));
    }

    public void testInsert_2DimesionalArray() throws Exception {
        SalEmpDao dao = new SalEmpDao_();
        String[][] array = new String[][] { { "aaa", "bbb" }, { "ccc", "ddd" } };
        SalEmp entity = new SalEmp_();
        entity.name().set("hoge");
        entity.schedule().setDomain(dao.createString2DArray(array));
        dao.insert(entity);
        List<SalEmp> entities = dao.selectAll();
        assertEquals(3, entities.size());
        entity = entities.get(2);
        array = entity.schedule().getArray();
        assertEquals(2, array.length);
        assertEquals(2, array[0].length);
        assertEquals(2, array[1].length);
        assertEquals("aaa", array[0][0]);
        assertEquals("bbb", array[0][1]);
        assertEquals("ccc", array[1][0]);
        assertEquals("ddd", array[1][1]);
    }

    public void testUpdate() throws Exception {
        SalEmpDao dao = new SalEmpDao_();
        List<SalEmp> entities = dao.selectAll();
        assertEquals(2, entities.size());
        SalEmp entity = entities.get(0);
        Integer[] array = entity.pay_by_quarter().getArray();
        assertEquals(4, array.length);
        array[0] = 10;
        entity.pay_by_quarter().setDomain(dao.createIntegerArray(array));
        dao.update(entity);

        entities = dao.selectAll();
        entity = entities.get(0);
        assertEquals(new Integer(10), entity.pay_by_quarter().getArray()[0]);
    }

    public void testUpdate_2DimesionalArray() throws Exception {
        SalEmpDao dao = new SalEmpDao_();
        List<SalEmp> entities = dao.selectAll();
        assertEquals(2, entities.size());
        SalEmp entity = entities.get(0);
        String[][] array = entity.schedule().getArray();
        assertEquals(2, array.length);
        assertEquals(2, array[0].length);
        assertEquals(2, array[1].length);
        array[0][0] = "aaa";
        entity.schedule().setDomain(dao.createString2DArray(array));
        dao.update(entity);

        entities = dao.selectAll();
        entity = entities.get(0);
        assertEquals("aaa", entity.schedule().getArray()[0][0]);
    }
}
