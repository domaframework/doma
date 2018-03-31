package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated field holds original states that are fetched from database.
 *
 * <p>This annotation allows that only modified properties are reflected to SQL UPDATE statements.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class. The field type must
 * be same as the {@link Entity} annotated class.
 *
 * <p>The field must not be modified by application code.
 *
 * <p>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     String name;
 *
 *     &#064;OriginalStates
 *     Employee originalStates;
 *
 *     public String getName() {
 *         return name;
 *     }
 *
 *     public void setName(String name) {
 *         this.name = name;
 *     }
 *
 *     ...
 * }
 * </pre>
 *
 * @see Update
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface OriginalStates {}
