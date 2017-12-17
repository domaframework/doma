package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a strategy to generate identifiers.
 * <p>
 * The annotated field must be a member of an {@link Entity} annotated class and
 * the field must be annotated with {@link Id}.
 * <p>
 * The additional annotation is required according to the {@code strategy}
 * value:
 * <ul>
 * <li>the {@link SequenceGenerator} annotation is required, if
 * {@link GenerationType#SEQUENCE} is specified.
 * <li>the {@link TableGenerator} annotation is required, if
 * {@link GenerationType#TABLE} is specified.
 * </ul>
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
 * 
 * @see GenerationType
 * @see SequenceGenerator
 * @see TableGenerator
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedValue {

    /**
     * The strategy how to generate identifiers.
     */
    GenerationType strategy();
}
