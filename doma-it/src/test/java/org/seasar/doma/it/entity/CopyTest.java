package org.seasar.doma.it.entity;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.junit.runner.RunWith;
import org.seasar.doma.copy.CopyUtil;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.unit.annotation.TxBehaviorType;

@RunWith(Seasar2.class)
public class CopyTest {

    @TxBehavior(TxBehaviorType.NONE)
    public void testFromEntityToBean() throws Exception {
        Emp src = new Emp_();
        src.id().set(1);
        src.name().set("aaa");
        src.salary().set(new BigDecimal(100));
        src.version().set(2);
        src.insertTimestamp().set(Timestamp.valueOf("2001-02-03 12:34:56"));
        src.updateTimestamp().set(Timestamp.valueOf("3001-02-03 12:34:56"));
        src.temp().set("bbb");
        EmpBean dest = new EmpBean();
        CopyUtil.copy(src, dest);

        assertEquals(new Integer(1), dest.id);
        assertEquals("aaa", dest.name);
        assertEquals(new BigDecimal(100), dest.salary);
        assertEquals(new Integer(2), dest.version);
        assertEquals(Timestamp.valueOf("2001-02-03 12:34:56"),
                dest.insertTimestamp);
        assertEquals(Timestamp.valueOf("3001-02-03 12:34:56"),
                dest.updateTimestamp);
    }

    @TxBehavior(TxBehaviorType.NONE)
    public void testFromBeanToEntity() throws Exception {
        EmpBean src = new EmpBean();
        src.id = 1;
        src.name = "aaa";
        src.salary = new BigDecimal(100);
        src.version = 2;
        src.insertTimestamp = Timestamp.valueOf("2001-02-03 12:34:56");
        src.updateTimestamp = Timestamp.valueOf("3001-02-03 12:34:56");
        src.temp = "bbb";
        Emp dest = new Emp_();
        CopyUtil.copy(src, dest);

        assertEquals(new Integer(1), dest.id().get());
        assertEquals("aaa", dest.name().get());
        assertEquals(new BigDecimal(100), dest.salary().get());
        assertEquals(new Integer(2), dest.version().get());
        assertEquals(Timestamp.valueOf("2001-02-03 12:34:56"), dest
                .insertTimestamp().get());
        assertEquals(Timestamp.valueOf("3001-02-03 12:34:56"), dest
                .updateTimestamp().get());
    }

    public static class EmpBean {

        public Integer id;

        public String name;

        public BigDecimal salary;

        public Integer version;

        public Timestamp insertTimestamp;

        public Timestamp updateTimestamp;

        public String temp;
    }
}
