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

import org.seasar.doma.internal.WrapException;

/**
 * A wrapper interface for accessing and manipulating object fields.
 *
 * <p>This interface provides a unified way to get and set field values on objects, abstracting the
 * underlying reflection operations. It handles different types of fields including regular fields
 * and optional fields, providing type-safe access patterns.
 *
 * <p>Implementations of this interface are typically created by {@link PropertyPathSegment}
 * implementations to handle field access for specific property path segments.
 *
 * <p>This interface is used internally by the Doma framework for property mapping and should not be
 * used directly by application code.
 *
 * @see PropertyPathSegment
 * @see PropertyPath
 */
public interface FieldWrapper {
  /**
   * Retrieves the value of this field from the specified object.
   *
   * @param obj the object from which to retrieve the field value
   * @return the field value, which may be null
   * @throws WrapException if an error occurs during field access
   */
  Object get(Object obj) throws WrapException;

  /**
   * Sets the value of this field on the specified object.
   *
   * @param obj the object on which to set the field value
   * @param value the value to set, which may be null
   * @throws WrapException if an error occurs during field access
   */
  void set(Object obj, Object value) throws WrapException;

  /**
   * Returns the type of this field.
   *
   * <p>For optional fields, this returns the element type rather than the Optional type itself.
   *
   * @return the field type
   */
  Class<?> getType();
}
