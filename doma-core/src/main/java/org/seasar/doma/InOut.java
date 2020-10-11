package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.Reference;

/**
 * Indicates a INOUT parameter of stored functions or stored procedures.
 *
 * <p>The annotated parameter type must be {@link Reference}. The annotated parameter must be one of
 * the parameters of the method that is annotated with {@link Function} or {@link Procedure}.
 *
 * <pre>
 * &#064;Dao
 * public interface EmployeeDao {
 *
 *     &#064;Procedure
 *     void updateSalary(&#064;In Integer id, &#064;InOut Reference&lt;BigDecimal&gt; salary);
 * }
 * </pre>
 *
 * <pre>
 * EmployeeDao dao = new EmployeeDaoImpl(config);
 * Reference&lt;BigDecimal&gt; salaryRef = new Reference&lt;BigDecimal&gt;(new BigDecimal(1000));
 * dao.updateSalary(1, salaryRef);
 * BigDecimal salary = salaryRef.get();
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface InOut {}
