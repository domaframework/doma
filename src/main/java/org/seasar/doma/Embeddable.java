package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates an embeddable class.
 *
 * <p>The embeddable class can be embedded into an entity class.
 *
 * <p>The embeddable class must have a non-private constructor that accepts all properties of the
 * class as arguments.
 *
 * <pre>
 * &#064;Embeddable
 * public class Address {
 *
 *     &#064;Column(name = &quot;CITY&quot;)
 *     private final String city;
 *
 *     &#064;Column(name = &quot;STREET&quot;)
 *     private final String street;
 *
 *     public Address(String city, String street) {
 *         this.city = city;
 *         this.street = street;
 *     }
 *     ...
 * }
 * </pre>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Id
 *     &#064;Column(name = &quot;ID&quot;)
 *     Integer id;
 *
 *     Address address;
 *
 *     &#064;Version
 *     &#064;Column(name = &quot;VERSION&quot;)
 *     int version;
 *
 *     ...
 * }
 * </pre>
 *
 * <p>The embeddable instance is not required to be thread safe.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Embeddable {}
