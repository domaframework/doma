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
package org.seasar.doma.internal.jdbc.entity;

import java.lang.reflect.Field;
import java.util.Objects;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.FieldUtil;

/**
 * Represents a single segment within a property path that defines how to access a field.
 *
 * <p>A property path segment encapsulates the logic for accessing a specific field within an
 * object, including handling different field types such as regular fields and optional fields. Each
 * segment knows its name and how to create a {@link FieldWrapper} for accessing the corresponding
 * field.
 *
 * <p>This interface provides different implementations for different field access patterns:
 *
 * <ul>
 *   <li>{@link Default} - for regular field access with direct value retrieval and assignment
 *   <li>{@link Optional} - for optional field access that unwraps Optional values and provides
 *       transparent access to the contained value
 * </ul>
 *
 * <p><strong>Usage:</strong> Property path segments are typically used in conjunction with {@link
 * PropertyPath} to build chains of field access operations. Each segment represents one level in
 * the property navigation chain, such as {@code address.street.name} where each dot-separated part
 * corresponds to a segment.
 *
 * <p><strong>Thread Safety:</strong> Implementations of this interface should be immutable and
 * thread-safe. The provided record implementations ({@link Default} and {@link Optional}) are
 * inherently thread-safe due to their immutable nature.
 *
 * <p><strong>Reflection Handling:</strong> Implementations handle reflection-based field access
 * internally, including making private fields accessible when necessary. Callers do not need to
 * manage field accessibility.
 *
 * <p>This interface is used internally by the Doma framework for property mapping and should not be
 * used directly by application code.
 *
 * @see PropertyPath
 * @see FieldWrapper
 * @since 2.0.0
 */
public interface PropertyPathSegment {
  /**
   * Returns the name of this property path segment.
   *
   * <p>The name typically corresponds to a field name in a Java class and is used for
   * identification and debugging purposes. For example, in a property path like {@code
   * person.address.street}, each segment would have names "person", "address", and "street"
   * respectively.
   *
   * @return the segment name, never {@code null} for well-formed segments
   */
  String name();

  /**
   * Creates a field wrapper for the specified field that handles access according to this segment's
   * type.
   *
   * <p>The field wrapper will be configured to handle the specific access pattern required by this
   * segment type. For example:
   *
   * <ul>
   *   <li>{@link Default} segments create wrappers for direct field access
   *   <li>{@link Optional} segments create wrappers that automatically unwrap Optional values
   * </ul>
   *
   * <p>The implementation automatically handles field accessibility, making private fields
   * accessible as needed. The returned wrapper provides a consistent interface for field access
   * regardless of the underlying field's visibility modifiers.
   *
   * @param field the field to wrap, must not be {@code null}
   * @return a field wrapper configured for this segment's access pattern, never {@code null}
   * @throws WrapException if an error occurs while creating the wrapper, such as security
   *     restrictions preventing field access
   * @throws IllegalArgumentException if the field type is incompatible with this segment type
   *     (implementation-specific)
   */
  FieldWrapper wrapField(Field field) throws WrapException;

  /**
   * A default property path segment implementation for regular field access.
   *
   * <p>This implementation provides standard field access without any special handling. It ensures
   * the field is accessible via reflection and creates a wrapper that directly gets and sets field
   * values.
   *
   * @param name the name of the property segment
   */
  record Default(String name) implements PropertyPathSegment {

    public Default {
      Objects.requireNonNull(name);
    }

    /**
     * Creates a field wrapper for regular field access.
     *
     * <p>This method ensures the field is accessible (making it accessible if necessary) and
     * returns a wrapper that provides direct access to the field value.
     *
     * @param field the field to wrap
     * @return a field wrapper for direct field access
     * @throws WrapException if an error occurs while creating the wrapper
     */
    @Override
    public FieldWrapper wrapField(Field field) throws WrapException {
      if (!FieldUtil.isPublic(field)) {
        FieldUtil.setAccessible(field, true);
      }
      return new FieldWrapper() {
        @Override
        public Object get(Object obj) throws WrapException {
          return FieldUtil.get(field, obj);
        }

        @Override
        public void set(Object obj, Object value) throws WrapException {
          FieldUtil.set(field, obj, value);
        }

        @Override
        public Class<?> getType() {
          return field.getType();
        }
      };
    }
  }

  /**
   * A property path segment implementation for optional field access.
   *
   * <p>This implementation handles fields of type {@link java.util.Optional}, providing access to
   * the wrapped value rather than the Optional container itself. When getting values, it unwraps
   * the Optional and returns the contained value (or null if empty). The type returned by the
   * wrapper is the element type rather than Optional.
   *
   * @param name the name of the property segment
   * @param elementClass the class of the element wrapped by the Optional
   */
  record Optional(String name, Class<?> elementClass) implements PropertyPathSegment {

    public Optional {
      Objects.requireNonNull(name);
      Objects.requireNonNull(elementClass);
    }

    /**
     * Creates a field wrapper for optional field access.
     *
     * <p>This method creates a wrapper that handles Optional fields by unwrapping their values. The
     * wrapper's {@code get} method returns the unwrapped value (or null), and the {@code getType}
     * method returns the element type rather than Optional.
     *
     * @param field the Optional field to wrap, must be of type {@link java.util.Optional}
     * @return a field wrapper that unwraps Optional values
     * @throws IllegalArgumentException if the field is not of type {@link java.util.Optional}
     * @throws WrapException if an error occurs while creating the wrapper
     */
    @Override
    public FieldWrapper wrapField(Field field) throws WrapException {
      if (!field.getType().equals(java.util.Optional.class)) {
        throw new IllegalArgumentException("field must be of type java.util.Optional: " + field);
      }

      var segment = new Default(name);
      var fieldWrapper = segment.wrapField(field);

      return new FieldWrapper() {
        @Override
        public Object get(Object obj) throws WrapException {
          var optional = (java.util.Optional<?>) fieldWrapper.get(obj);
          return optional != null ? optional.orElse(null) : null;
        }

        @Override
        public void set(Object obj, Object value) throws WrapException {
          fieldWrapper.set(obj, value);
        }

        @Override
        public Class<?> getType() {
          return elementClass;
        }
      };
    }
  }
}
