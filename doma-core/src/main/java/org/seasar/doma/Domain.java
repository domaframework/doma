/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a domain class.
 *
 * <p>A domain class is a user-defined type that wraps a basic value. It can be mapped to a database
 * column.
 *
 * <p>Instantiation by constructor:
 *
 * <pre>
 * &#064;Domain(valueType = String.class)
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
 * &#064;Domain(valueType = String.class, factoryMethod = &quot;of&quot;)
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
public @interface Domain {

  /**
   * @return the value type that is wrapped by this domain class.
   */
  Class<?> valueType();

  /**
   * The factory method name.
   *
   * <p>The factory method that accepts the wrapped value as an argument.
   *
   * <p>The default value {@code "new"} means constructor usage.
   *
   * @return the method name
   */
  String factoryMethod() default "new";

  /**
   * The accessor method name.
   *
   * <p>The accessor method returns the wrapped value.
   *
   * @return the method name
   */
  String accessorMethod() default "getValue";

  /**
   * @return whether the constructor or the factory method should accept {@code null} as an
   *     argument.
   */
  boolean acceptNull() default false;
}
