package org.seasar.doma.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a data type (aka domain class).
 *
 * <p>The data type is the user defined type that wraps a basic value. It can be mapped to a
 * database column.
 *
 * <p>This annotation is applied for only record types.
 *
 * <pre>
 * &#064;DataType
 * public record PhoneNumber(String value) {
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataType {}
