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

    public void testInsert() throws Exception {
        SalEmpDao dao = new SalEmpDao_();
        Integer[] array = new Integer[] { 10, 20, 30, 40 };
        SalEmp entity = new SalEmp_();
        entity.name().set("hoge");
        entity.pay_by_quarter().set(dao.createIntegerArray(array));
        dao.insert(entity);
        List<SalEmp> entities = dao.selectAll();
        assertEquals(3, entities.size());
        entity = entities.get(2);
        assertTrue(Arrays.equals(array, entity.pay_by_quarter().getArray()));
    }

    public void testUpdate() throws Exception {
        SalEmpDao dao = new SalEmpDao_();
        List<SalEmp> entities = dao.selectAll();
        assertEquals(2, entities.size());
        SalEmp entity = entities.get(0);
        Integer[] array = entity.pay_by_quarter().getArray();
        assertEquals(4, array.length);
        array[0] = 10;
        entity.pay_by_quarter().set(dao.createIntegerArray(array));
        dao.update(entity);
    }
}
