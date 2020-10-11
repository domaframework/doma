package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a SQL template.
 *
 * <p>This annotation can be combined with following annotations:
 *
 * <ul>
 *   <li>{@link org.seasar.doma.BatchDelete}
 *   <li>{@link org.seasar.doma.BatchInsert}
 *   <li>{@link org.seasar.doma.BatchUpdate}
 *   <li>{@link org.seasar.doma.Delete}
 *   <li>{@link org.seasar.doma.Insert}
 *   <li>{@link org.seasar.doma.Update}
 *   <li>{@link org.seasar.doma.Script}
 *   <li>{@link org.seasar.doma.Select}
 *   <li>{@link org.seasar.doma.SqlProcessor}
 * </ul>
 *
 * <pre>
 * &#064;Dao
 * public interface EmployeeDao {
 *
 *     &#064;Sql("select name from employee where age = &#047;* age *&#047;0)")
 *     &#064;Select
 *     List&lt;String&gt; selectNamesByAge(int age);
 *
 *     &#064;Sql("insert into employee (name) values (&#047;* employee.name *&#047;'test data')")
 *     &#064;Insert
 *     int insert(Employee employee);
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sql {

  /** @return the SQL template */
  String value();
}
