package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.id.BuiltinSequenceIdGenerator;
import org.seasar.doma.jdbc.id.SequenceIdGenerator;

/**
 * Indicates an identifier generator that uses a sequence.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class. This annotation
 * must be used in conjunction with the {@link Id} annotation and the {@link GeneratedValue}
 * annotation.
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Id
 *     &#064;GeneratedValue(strategy = GenerationType.SEQUENCE)
 *     &#064;SequenceGenerator(sequence = &quot;EMPLOYEE_SEQ&quot;)
 *     Integer id;
 *
 *     ...
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SequenceGenerator {

  /** @return the catalog name. */
  String catalog() default "";

  /** @return the schema name. */
  String schema() default "";

  /** @return the sequence name. */
  String sequence();

  /** @return the initial value. */
  long initialValue() default 1;

  /** @return the allocation size. */
  long allocationSize() default 1;

  /** @return the implementation class of the {@link SequenceIdGenerator} interface. */
  Class<? extends SequenceIdGenerator> implementer() default BuiltinSequenceIdGenerator.class;
}
