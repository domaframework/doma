package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate a version property that is used for optimistic locking.
 * <p>
 * The annotated field must be a member of an {@link Entity} annotated class.
 * <p>
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 * 
 *     &#064;Version
 *     &#064;Column(name = &quot;VERSION_NO&quot;)
 *     int versionNo;
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface Version {
}
