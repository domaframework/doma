package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a value holder class.
 * <p>
 * The holder class is the user defined type that wraps a basic value. It can be
 * mapped to a database column.
 * <p>
 * Instantiation by constructor:
 * 
 * <pre>
 * &#064;Holder(valueType = String.class)
 * public class PhoneNumber {
 * 
 *     private final String value;
 * 
 *     public PhoneNumber(String value) {
 *         this.value = value;
 *     }
 * 
 *     public String getValue() {
 *         return value;
 *     }
 * }
 * </pre>
 * 
 * Instantiation by factory method:
 * 
 * <pre>
 * &#064;Holder(valueType = String.class, factoryMethod = &quot;of&quot;)
 * public class PhoneNumber {
 * 
 *     private final String value;
 * 
 *     private PhoneNumber(String value) {
 *         this.value = value;
 *     }
 * 
 *     public String getValue() {
 *         return value;
 *     }
 * 
 *     public static PhoneNumber of(String value) {
 *         return new PhoneNumber(value);
 *     }
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Holder {

    /**
     * The value type that is wrapped by the holder class.
     */
    Class<?> valueType();

    /**
     * The factory method name.
     * <p>
     * The factory method that accepts the wrapped value as an argument.
     * <p>
     * The default value {@code "new"} means constructor usage.
     */
    String factoryMethod() default "new";

    /**
     * The accessor method name.
     * <p>
     * The accessor method returns the wrapped value.
     */
    String accessorMethod() default "getValue";

    /**
     * Whether the constructor or the factory method accepts {@code null} as an
     * argument.
     */
    boolean acceptNull() default false;

}
