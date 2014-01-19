package org.seasar.doma.it.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.runner.RunWith;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.builder.SelectBuilder;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SelectBuilderTest {

    public void testGetScalarSingleResult() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        String name = builder.getScalarSingleResult(String.class);
        assertEquals("SMITH", name);
    }

    public void testGetScalarSingleResult_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
        String name = builder.getScalarSingleResult(String.class);
        assertNull(name);
    }

    public void testGetOptionalScalarSingleResult() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        Optional<String> name = builder
                .getOptionalScalarSingleResult(String.class);
        assertEquals("SMITH", name.get());
    }

    public void testGetOptionalScalarSingleResult_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
        Optional<String> name = builder
                .getOptionalScalarSingleResult(String.class);
        assertFalse(name.isPresent());
    }

    public void testGetScalarResultList() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
        List<String> list = builder.getScalarResultList(String.class);
        assertEquals(14, list.size());
        assertEquals("SMITH", list.get(0));
    }

    public void testGetScalarResultList_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select null from EMPLOYEE");
        List<String> list = builder.getScalarResultList(String.class);
        assertEquals(14, list.size());
        assertEquals(null, list.get(0));
    }

    public void testGetOptionalScalarResultList() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
        List<Optional<String>> list = builder
                .getOptionalScalarResultList(String.class);
        assertEquals(14, list.size());
        assertEquals("SMITH", list.get(0).get());
    }

    public void testGetOptionalScalarResultList_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select null from EMPLOYEE");
        List<Optional<String>> list = builder
                .getOptionalScalarResultList(String.class);
        assertEquals(14, list.size());
        assertFalse(list.get(0).isPresent());
    }

    public void testIterateAsScalar() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        String name = builder.iterateAsScalar(String.class, (e, c) -> e);
        assertEquals("SMITH", name);
    }

    public void testIterateAsScalar_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
        String name = builder.iterateAsScalar(String.class, (v, c) -> v);
        assertNull(name);
    }

    public void testIterateAsOptionalScalar() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        Optional<String> name = builder.iterateAsOptionalScalar(String.class, (
                v, c) -> v);
        assertEquals("SMITH", name.get());
    }

    public void testIterateAsOptionalScalar_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select null from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        Optional<String> name = builder.iterateAsOptionalScalar(String.class, (
                v, c) -> v);
        assertFalse(name.isPresent());
    }

    public void testStreamScalar() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        Optional<String> name = builder.streamScalar(String.class,
                stream -> stream.findFirst());
        assertEquals("SMITH", name.get());
    }

    public void testStreamScalar_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select null from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
        Optional<String> name = builder.streamScalar(String.class,
                stream -> stream.findFirst());
        assertFalse(name.isPresent());
    }

    public void testStreamOptionalScalar() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        Optional<Optional<String>> name = builder.streamOptionalScalar(
                String.class, stream -> stream.findFirst());
        assertEquals("SMITH", name.get().get());
    }

    public void testStreamOptionalScalar_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select null from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        Optional<Optional<String>> name = builder.streamOptionalScalar(
                String.class, stream -> stream.findFirst());
        assertFalse(name.get().isPresent());
    }

    public void testGetMapSingleResult() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        Map<String, Object> employee = builder
                .getMapSingleResult(MapKeyNamingType.CAMEL_CASE);
        assertNotNull(employee);
        assertNotNull(employee.get("employeeId"));
        assertNotNull(employee.get("employeeName"));
        assertNotNull(employee.get("hiredate"));
    }

    public void testGetMapSingleResult_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
        Map<String, Object> employee = builder
                .getMapSingleResult(MapKeyNamingType.CAMEL_CASE);
        assertNull(employee);
    }

    public void testGetOptionalMapSingleResult() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        Optional<Map<String, Object>> employee = builder
                .getOptionalMapSingleResult(MapKeyNamingType.CAMEL_CASE);
        assertNotNull(employee);
        assertEquals("SMITH", employee.get().get("employeeName"));
    }

    public void testGetOptionalMapSingleResult_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
        Optional<Map<String, Object>> employee = builder
                .getOptionalMapSingleResult(MapKeyNamingType.CAMEL_CASE);
        assertNotNull(employee);
        assertFalse(employee.isPresent());
    }

    public void testGetMapResultList() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        List<Map<String, Object>> employees = builder
                .getMapResultList(MapKeyNamingType.CAMEL_CASE);
        assertNotNull(employees);
        assertEquals(14, employees.size());
    }

    public void testIterateAsMap() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        Integer result = builder.iterateAsMap(MapKeyNamingType.CAMEL_CASE,
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

    public void testStreamMap() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        Optional<Map<String, Object>> result = builder.streamMap(
                MapKeyNamingType.CAMEL_CASE, stream -> stream.findFirst());
        assertEquals("SMITH", result.get().get("employeeName"));
    }

    public void testGetEntitySingleResult() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        Employee employee = builder.getEntitySingleResult(Employee.class);
        assertNotNull(employee);
        assertEquals("SMITH", employee.getEmployeeName());
    }

    public void testGetEntitySingleResult_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
        Employee employee = builder.getEntitySingleResult(Employee.class);
        assertNull(employee);
    }

    public void testGetOptionalEntitySingleResult() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
        Optional<Employee> employee = builder
                .getOptionalEntitySingleResult(Employee.class);
        assertNotNull(employee);
        assertEquals("SMITH", employee.get().getEmployeeName());
    }

    public void testGetOptionalEntitySingleResult_null() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        builder.sql("where");
        builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
        Optional<Employee> employee = builder
                .getOptionalEntitySingleResult(Employee.class);
        assertNotNull(employee);
        assertFalse(employee.isPresent());
    }

    public void testGetEntityResultList() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        List<Employee> employees = builder.getEntityResultList(Employee.class);
        assertEquals(14, employees.size());
        assertEquals("SMITH", employees.get(0).getEmployeeName());
    }

    public void testIterateAsEntity() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        Integer result = builder.iterateAsEntity(Employee.class,
                new IterationCallback<Integer, Employee>() {
                    private int count;

                    public Integer iterate(Employee target,
                            IterationContext context) {
                        count++;
                        return count;
                    }
                });
        assertEquals(14, result.intValue());
    }

    public void testStreamEntity() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new ItConfig());
        builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
        Optional<Employee> employee = builder.streamEntity(Employee.class,
                stream -> stream.findFirst());
        assertEquals("SMITH", employee.get().getEmployeeName());
    }

    public void testSelectBuilderInDao() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        List<Employee> employees = dao.selectWithBuilder();
        assertEquals(14, employees.size());
    }
}
