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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PropertyPathTest {

  @Test
  public void testConstructor_emptySegments() {
    List<PropertyPathSegment> emptySegments = Collections.emptyList();

    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> new PropertyPath(emptySegments));
    assertEquals("segments", ex.getMessage());
  }

  @Test
  public void testConstructor_singleSegment() {
    PropertyPathSegment segment = new PropertyPathSegment.Default("name");
    List<PropertyPathSegment> segments = List.of(segment);

    PropertyPath path = new PropertyPath(segments);

    assertNotNull(path);
    assertEquals(1, path.segments().size());
    assertEquals("name", path.segments().get(0).name());
  }

  @Test
  public void testConstructor_multipleSegments() {
    List<PropertyPathSegment> segments =
        Arrays.asList(
            new PropertyPathSegment.Default("address"),
            new PropertyPathSegment.Default("city"),
            new PropertyPathSegment.Default("name"));

    PropertyPath path = new PropertyPath(segments);

    assertNotNull(path);
    assertEquals(3, path.segments().size());
    assertEquals("address", path.segments().get(0).name());
    assertEquals("city", path.segments().get(1).name());
    assertEquals("name", path.segments().get(2).name());
  }

  @Test
  public void testName_singleSegment() {
    PropertyPathSegment segment = new PropertyPathSegment.Default("email");
    PropertyPath path = new PropertyPath(List.of(segment));

    assertEquals("email", path.name());
  }

  @Test
  public void testName_multipleSegments() {
    List<PropertyPathSegment> segments =
        Arrays.asList(
            new PropertyPathSegment.Default("employee"),
            new PropertyPathSegment.Default("department"),
            new PropertyPathSegment.Default("name"));
    PropertyPath path = new PropertyPath(segments);

    assertEquals("employee.department.name", path.name());
  }

  @Test
  public void testName_withOptionalSegment() {
    List<PropertyPathSegment> segments =
        Arrays.asList(
            new PropertyPathSegment.Default("person"),
            new PropertyPathSegment.Optional("address", String.class),
            new PropertyPathSegment.Default("street"));
    PropertyPath path = new PropertyPath(segments);

    assertEquals("person.address.street", path.name());
  }

  @Test
  public void testName_mixedSegmentTypes() {
    List<PropertyPathSegment> segments =
        Arrays.asList(
            new PropertyPathSegment.Default("order"),
            new PropertyPathSegment.Optional("customer", Customer.class),
            new PropertyPathSegment.Default("address"),
            new PropertyPathSegment.Optional("zipCode", String.class));
    PropertyPath path = new PropertyPath(segments);

    assertEquals("order.customer.address.zipCode", path.name());
  }

  @Test
  public void testSegmentsImmutability() {
    List<PropertyPathSegment> originalSegments =
        Arrays.asList(
            new PropertyPathSegment.Default("field1"), new PropertyPathSegment.Default("field2"));
    PropertyPath path = new PropertyPath(originalSegments);

    // Verify that the segments list is the same reference (record behavior)
    assertEquals(originalSegments, path.segments());
  }

  @Test
  public void testCombine_singleSegmentPath() {
    PropertyPath basePath = new PropertyPath(List.of(new PropertyPathSegment.Default("user")));
    PropertyPathSegment newSegment = new PropertyPathSegment.Default("name");

    PropertyPath combinedPath = PropertyPath.combine(basePath, newSegment);

    assertEquals(2, combinedPath.segments().size());
    assertEquals("user", combinedPath.segments().get(0).name());
    assertEquals("name", combinedPath.segments().get(1).name());
    assertEquals("user.name", combinedPath.name());
  }

  @Test
  public void testCombine_multipleSegmentPath() {
    List<PropertyPathSegment> baseSegments =
        Arrays.asList(
            new PropertyPathSegment.Default("order"), new PropertyPathSegment.Default("customer"));
    PropertyPath basePath = new PropertyPath(baseSegments);
    PropertyPathSegment newSegment = new PropertyPathSegment.Default("address");

    PropertyPath combinedPath = PropertyPath.combine(basePath, newSegment);

    assertEquals(3, combinedPath.segments().size());
    assertEquals("order", combinedPath.segments().get(0).name());
    assertEquals("customer", combinedPath.segments().get(1).name());
    assertEquals("address", combinedPath.segments().get(2).name());
    assertEquals("order.customer.address", combinedPath.name());
  }

  @Test
  public void testCombine_withOptionalSegment() {
    PropertyPath basePath = new PropertyPath(List.of(new PropertyPathSegment.Default("entity")));
    PropertyPathSegment optionalSegment = new PropertyPathSegment.Optional("detail", String.class);

    PropertyPath combinedPath = PropertyPath.combine(basePath, optionalSegment);

    assertEquals(2, combinedPath.segments().size());
    assertEquals("entity", combinedPath.segments().get(0).name());
    assertEquals("detail", combinedPath.segments().get(1).name());
    assertEquals("entity.detail", combinedPath.name());
  }

  @Test
  public void testCombine_nullPath() {
    PropertyPathSegment segment = new PropertyPathSegment.Default("test");

    assertThrows(NullPointerException.class, () -> PropertyPath.combine(null, segment));
  }

  @Test
  public void testCombine_nullSegment() {
    PropertyPath path = new PropertyPath(List.of(new PropertyPathSegment.Default("test")));

    assertThrows(NullPointerException.class, () -> PropertyPath.combine(path, null));
  }

  @Test
  public void testOf_singleSegment() {
    PropertyPath path = PropertyPath.of("username");

    assertEquals(1, path.segments().size());
    assertEquals("username", path.segments().get(0).name());
    assertEquals("username", path.name());
  }

  @Test
  public void testOf_multipleSegments() {
    PropertyPath path = PropertyPath.of("user.profile.email");

    assertEquals(3, path.segments().size());
    assertEquals("user", path.segments().get(0).name());
    assertEquals("profile", path.segments().get(1).name());
    assertEquals("email", path.segments().get(2).name());
    assertEquals("user.profile.email", path.name());
  }

  @Test
  public void testOf_emptyString() {
    PropertyPath path = PropertyPath.of("");

    assertEquals(1, path.segments().size());
    assertEquals("", path.segments().get(0).name());
    assertEquals("", path.name());
  }

  @Test
  public void testOf_nullString() {
    assertThrows(NullPointerException.class, () -> PropertyPath.of(null));
  }

  @Test
  public void testToString_singleSegment() {
    PropertyPath path = new PropertyPath(List.of(new PropertyPathSegment.Default("id")));

    assertEquals("id", path.toString());
  }

  @Test
  public void testToString_multipleSegments() {
    List<PropertyPathSegment> segments =
        Arrays.asList(
            new PropertyPathSegment.Default("company"),
            new PropertyPathSegment.Default("department"),
            new PropertyPathSegment.Default("manager"),
            new PropertyPathSegment.Default("name"));
    PropertyPath path = new PropertyPath(segments);

    assertEquals("company.department.manager.name", path.toString());
  }

  @Test
  public void testToString_equalsName() {
    PropertyPath path = PropertyPath.of("account.balance");

    assertEquals(path.name(), path.toString());
  }

  // Dummy class for testing Optional segments
  private static class Customer {}
}
