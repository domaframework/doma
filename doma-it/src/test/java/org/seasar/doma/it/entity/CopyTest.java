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

        assertEquals(new Integer(1), dest.getId());
        assertEquals("aaa", dest.getName());
        assertEquals(new BigDecimal(100), dest.getSalary());
        assertEquals(new Integer(2), dest.getVersion());
        assertEquals(Timestamp.valueOf("2001-02-03 12:34:56"), dest
                .getInsertTimestamp());
        assertEquals(Timestamp.valueOf("3001-02-03 12:34:56"), dest
                .getUpdateTimestamp());
    }

    @TxBehavior(TxBehaviorType.NONE)
    public void testFromBeanToEntity() throws Exception {
        EmpBean src = new EmpBean();
        src.setId(1);
        src.setName("aaa");
        src.setSalary(new BigDecimal(100));
        src.setVersion(2);
        src.setInsertTimestamp(Timestamp.valueOf("2001-02-03 12:34:56"));
        src.setUpdateTimestamp(Timestamp.valueOf("3001-02-03 12:34:56"));
        src.setTemp("bbb");
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

        private Integer id;

        private String name;

        private BigDecimal salary;

        private Integer version;

        private Timestamp insertTimestamp;

        private Timestamp updateTimestamp;

        private String temp;

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public Integer getVersion() {
            return version;
        }

        public void setInsertTimestamp(Timestamp insertTimestamp) {
            this.insertTimestamp = insertTimestamp;
        }

        public Timestamp getInsertTimestamp() {
            return insertTimestamp;
        }

        public void setUpdateTimestamp(Timestamp updateTimestamp) {
            this.updateTimestamp = updateTimestamp;
        }

        public Timestamp getUpdateTimestamp() {
            return updateTimestamp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getTemp() {
            return temp;
        }
    }
}
