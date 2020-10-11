package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a IN parameter of stored functions or stored procedures.
 *
 * <p>The annotated parameter must be one of the parameters of the method that is annotated with
 * {@link Function} or {@link Procedure}.
 *
 * <pre>
 * &#064;Dao
 * public interface EmployeeDao {
 *
 *     &#064;Procedure
 *     void updateSalary(&#064;In Integer id, &#064;In BigDecimal salary);
 * }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface In {}
