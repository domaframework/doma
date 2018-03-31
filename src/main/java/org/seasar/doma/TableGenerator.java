package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.id.BuiltinTableIdGenerator;
import org.seasar.doma.jdbc.id.TableIdGenerator;

/**
 * Indicates an identifier generator that uses a table.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class. This annotation
 * must be used in conjunction with the {@link Id} annotation and the {@link GeneratedValue}
 * annotation.
 *
 * <p>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Id
 *     &#064;GeneratedValue(strategy = GenerationType.TABLE)
 *     &#064;TableGenerator(pkColumnValue = &quot;EMPLOYEE_ID&quot;)
 *     Integer id;
 *
 *     ...
 * }
 * </pre>
 *
 * @author taedium
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableGenerator {

  /** The catalog name. */
  String catalog() default "";

  /** The schema name. */
  String schema() default "";

  /** The table name. */
  String table() default "ID_GENERATOR";

  /** The column name that is the primary key. */
  String pkColumnName() default "PK";

  /** The column name that has generated identifiers. */
  String valueColumnName() default "VALUE";

  /** The value of the primary key column. */
  String pkColumnValue();

  /** The initial value. */
  long initialValue() default 1;

  /** The allocated size. */
  long allocationSize() default 1;

  /** The implementation class of the {@link TableIdGenerator} interface. */
  Class<? extends TableIdGenerator> implementer() default BuiltinTableIdGenerator.class;
}
