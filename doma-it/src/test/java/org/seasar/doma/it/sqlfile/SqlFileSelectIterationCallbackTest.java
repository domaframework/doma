package org.seasar.doma.it.sqlfile;

import static junit.framework.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDao_;
import org.seasar.doma.it.domain.SalaryDomain;
import org.seasar.doma.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;

import doma.jdbc.IterationCallback;
import doma.jdbc.IterationContext;
import doma.jdbc.SelectOptions;

@RunWith(Seasar2.class)
public class SqlFileSelectIterationCallbackTest {

    @Test
    public void testEntity() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Integer count = dao
                .selectAll(new IterationCallback<Integer, Employee>() {

                    int count;

                    @Override
                    public Integer iterate(Employee target,
                            IterationContext context) {
                        count++;
                        return count;
                    }
                });
        assertEquals(new Integer(14), count);
    }

    @Test
    public void testEntity_limitOffset() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Integer count = dao.selectAll(
                new IterationCallback<Integer, Employee>() {

                    int count;

                    @Override
                    public Integer iterate(Employee target,
                            IterationContext context) {
                        count++;
                        return count;
                    }
                }, SelectOptions.get().limit(5).offset(3));
        assertEquals(new Integer(5), count);
    }

    @Test
    public void testDomain() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        SalaryDomain total = dao
                .selectAllSalary(new IterationCallback<SalaryDomain, SalaryDomain>() {

                    BigDecimal total = BigDecimal.ZERO;

                    @Override
                    public SalaryDomain iterate(SalaryDomain target,
                            IterationContext context) {
                        if (target.isNotNull()) {
                            total = total.add(target.get());
                        }
                        return new SalaryDomain(total);
                    }
                });
        assertTrue(new SalaryDomain(new BigDecimal("29025")).eq(total));
    }

    @Test
    public void testDomain_limitOffset() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        SalaryDomain total = dao.selectAllSalary(
                new IterationCallback<SalaryDomain, SalaryDomain>() {

                    BigDecimal total = BigDecimal.ZERO;

                    @Override
                    public SalaryDomain iterate(SalaryDomain target,
                            IterationContext context) {
                        if (target.isNotNull()) {
                            total = total.add(target.get());
                        }
                        return new SalaryDomain(total);
                    }
                }, SelectOptions.get().limit(5).offset(3));
        assertTrue(new SalaryDomain(new BigDecimal("12525")).eq(total));
    }
}
