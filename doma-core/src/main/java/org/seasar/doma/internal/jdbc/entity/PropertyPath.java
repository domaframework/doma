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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a path to a property within an object graph, composed of one or more segments.
 *
 * <p>A property path is used to navigate through object hierarchies, particularly when dealing with
 * embeddable objects that may be nested within entities. Each segment of the path represents a step
 * in the navigation from one object to another.
 *
 * <p>For example, a property path like "address.street" would consist of two segments: one for
 * "address" and another for "street", allowing navigation from an entity to its embedded address
 * object and then to the street field within that address.
 *
 * <p>This record is immutable and provides factory methods for creating and combining paths.
 *
 * <p>This class is used internally by the Doma framework for property mapping and should not be
 * used directly by application code.
 *
 * @param segments the list of path segments that make up this property path, must not be empty
 * @see PropertyPathSegment
 * @see PropertyField
 */
public record PropertyPath(List<? extends PropertyPathSegment> segments) {
  public PropertyPath {
    if (segments.isEmpty()) {
      throw new IllegalArgumentException("segments");
    }
  }

  /**
   * Returns the full name of this property path as a dot-separated string.
   *
   * <p>For example, if this path has segments "address" and "street", this method would return
   * "address.street".
   *
   * @return the full property path name with segments joined by dots
   */
  public String name() {
    return segments.stream().map(PropertyPathSegment::name).collect(Collectors.joining("."));
  }

  /**
   * Creates a new property path by combining an existing path with an additional segment.
   *
   * <p>This method allows for building longer property paths by appending segments to existing
   * paths. The original path is not modified; a new immutable path is returned.
   *
   * @param path the existing property path to extend
   * @param segment the segment to append to the path
   * @return a new property path containing all segments from the original path plus the new segment
   * @throws NullPointerException if either path or segment is null
   */
  public static PropertyPath combine(PropertyPath path, PropertyPathSegment segment) {
    Objects.requireNonNull(path);
    Objects.requireNonNull(segment);
    var segments = Stream.concat(path.segments.stream(), Stream.of(segment)).toList();
    return new PropertyPath(segments);
  }

  /**
   * Creates a property path from a dot-separated string representation.
   *
   * <p>This factory method parses a string like "address.street" into a property path with
   * appropriate segments. Each segment is created as a default property path segment.
   *
   * @param path the dot-separated string representation of the property path
   * @return a new property path with segments parsed from the string
   * @throws NullPointerException if path is null
   */
  public static PropertyPath of(String path) {
    Objects.requireNonNull(path);
    var segments = Arrays.stream(path.split("\\.")).map(PropertyPathSegment.Default::new).toList();
    return new PropertyPath(segments);
  }

  /**
   * Returns the string representation of this property path.
   *
   * <p>This is equivalent to calling {@link #name()}.
   *
   * @return the full property path name with segments joined by dots
   */
  @Override
  public String toString() {
    return name();
  }
}
