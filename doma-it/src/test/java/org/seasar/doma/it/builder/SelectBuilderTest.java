package org.seasar.doma.it.builder;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.builder.SelectBuilder;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SelectBuilderTest {

    public void testGetSingleResult_Map() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        Map<String, Object> employee = builder
                .getSingleResult(MapKeyNamingType.CAMEL_CASE);
        assertNotNull(employee);
        assertNotNull(employee.get("employeeId"));
        assertNotNull(employee.get("employeeName"));
        assertNotNull(employee.get("hiredate"));
    }

    public void testGetResultList_Map() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        List<Map<String, Object>> employees = builder
                .getResultList(MapKeyNamingType.CAMEL_CASE);
        assertNotNull(employees);
        assertEquals(14, employees.size());
    }

    public void testIterate_Map() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        Integer result = builder.iterate(MapKeyNamingType.CAMEL_CASE,
                new IterationCallback<Integer, Map<String, Object>>() {
                    private int count;

                    public Integer iterate(Map<String, Object> target,
                            IterationContext context) {
                        count++;
                        return count;
                    }
                });
        assertEquals(14, result.intValue());
    }
}
