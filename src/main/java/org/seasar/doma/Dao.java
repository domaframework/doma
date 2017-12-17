package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.doma.jdbc.Config;

/**
 * Indicates a DAO interface.
 * <p>
 * The annotated interface must be a top level interface.
 * <p>
 * 
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 * 
 *     &#064;Insert
 *     int insert(Employee employee);
 * }
 * </pre>
 * 
 * <p>
 * The interface can extend another DAO interface:
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface WorkerDao {
 * 
 *     &#064;Insert
 *     int insert(Worker worker);
 * }
 * </pre>
 * 
 * <pre>
 * &#064;Dao(config = AppConfig.class)
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
     * The runtime configuration.
     * <p>
     * If a user defined class is specified, a generated DAO implementation
     * class has a public no-arg constructor that instantiates the user defined
     * class in the constructor.
     * <p>
     * If it is not specified, a generated DAO implementation class has a public
     * constructor that accepts a single {@link Config} instance as an argument.
     * In this case, the {@link Config} instance can be injected by using
     * {@link AnnotateWith}.
     */
    Class<? extends Config> config() default Config.class;

    /**
     * The access level of the DAO implementation class.
     */
    AccessLevel accessLevel() default AccessLevel.PUBLIC;
}
