package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated field is not mapped to a column.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class.
 *
 * <p>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 *     &#064;Transient
 *     Integer tempNumber;
 *
 *     ...
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface Transient {}
