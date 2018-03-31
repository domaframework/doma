package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import org.seasar.doma.jdbc.ResultMappingException;

/**
 * Indicates a result set that is fetch by stored functions or stored procedures.
 *
 * <p>The annotated parameter type must be {@link List} and it must be one of parameters of the
 * method that is annotated with {@link Function} or {@link Procedure}.
 *
 * <p>
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;Procedure
 *     void fetchEmployees(@In Integer departmentId, &#064;ResultSet List&lt;Employee&gt; employees);
 * }
 * </pre>
 *
 * <pre>
 * EmployeeDao dao = new EmployeeDaoImpl();
 * List&lt;Employee&gt; employees = new ArrayList&lt;Employee&gt;();
 * dao.fetchEmployees(10, employees);
 * for (Employee e : employees) {
 *     ...
 * }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultSet {

  /**
   * Whether to ensure that all entity properties are mapped to columns of a result set.
   *
   * <p>This value is used only if the result set is fetched as an entity or a entity list.
   *
   * <p>If {@code true} and there are some unmapped properties、 {@link ResultMappingException} is
   * thrown from the annotated method.
   */
  boolean ensureResultMapping() default false;
}
