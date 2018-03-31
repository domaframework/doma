package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates an entity identifier that is mapped to a primary key of a database table.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class.
 *
 * <p>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Id
 *     &#064;Column(name = &quot;ID&quot;)
 *     String id;
 *     ...
 * }
 * </pre>
 *
 * @see GeneratedValue
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface Id {}
