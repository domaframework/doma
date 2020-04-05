package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the tenant id.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class.
 *
 * <p>The column mapped to the annotated field is included in WHERE clause in auto-generated SQL
 * statements
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 *
 *     &#064;TenantId
 *     String tenantId;
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface TenantId {}
