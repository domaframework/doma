package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a DAO interface.
 *
 * <p>The annotated interface must be a top level interface.
 *
 * <pre>
 * &#064;Dao
 * public interface EmployeeDao {
 *
 *     &#064;Insert
 *     int insert(Employee employee);
 * }
 * </pre>
 *
 * <p>The interface can extend another DAO interface:
 *
 * <pre>
 * &#064;Dao
 * public interface WorkerDao {
 *
 *     &#064;Insert
 *     int insert(Worker worker);
 * }
 * </pre>
 *
 * <pre>
 * &#064;Dao
 * public interface EmployeeDao extends WorkerDao {
 *
 *     &#064;Insert
 *     int insert(Employee employee);
 * }
 * </pre>
 *
 * @see ArrayFactory
 * @see BatchDelete
 * @see BatchInsert
 * @see BatchUpdate
 * @see BlobFactory
 * @see ClobFactory
 * @see Delete
 * @see Function
 * @see Insert
 * @see NClobFactory
 * @see Procedure
 * @see Select
 * @see Script
 * @see SqlProcessor
 * @see Update
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dao {

  /**
   * @return the access level of the DAO implementation class.
   */
  AccessLevel accessLevel() default AccessLevel.PUBLIC;
}
