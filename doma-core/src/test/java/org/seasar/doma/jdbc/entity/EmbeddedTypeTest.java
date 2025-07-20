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
package org.seasar.doma.jdbc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

class EmbeddedTypeTest {

  @Test
  void testConstructor_validParameters() {
    String prefix = "test_";
    Map<String, ColumnType> columnTypeMap =
        Map.of("prop1", new ColumnType("col1", null, null, null));

    EmbeddedType embeddedType = new EmbeddedType(prefix, columnTypeMap);

    assertEquals(prefix, embeddedType.prefix());
    assertEquals(columnTypeMap, embeddedType.columnTypeMap());
  }

  @Test
  void testConstructor_emptyPrefix() {
    String prefix = "";
    Map<String, ColumnType> columnTypeMap = Collections.emptyMap();

    EmbeddedType embeddedType = new EmbeddedType(prefix, columnTypeMap);

    assertEquals(prefix, embeddedType.prefix());
    assertEquals(columnTypeMap, embeddedType.columnTypeMap());
  }

  @Test
  void testConstructor_nullPrefix_throwsException() {
    Map<String, ColumnType> columnTypeMap = Collections.emptyMap();

    assertThrows(NullPointerException.class, () -> new EmbeddedType(null, columnTypeMap));
  }

  @Test
  void testConstructor_nullColumnTypeMap_throwsException() {
    String prefix = "test_";

    assertThrows(NullPointerException.class, () -> new EmbeddedType(prefix, null));
  }

  @Test
  void testMerge_withNullParent_returnsSameInstance() {
    String prefix = "child_";
    Map<String, ColumnType> columnTypeMap =
        Map.of("prop1", new ColumnType("col1", null, null, null));
    EmbeddedType child = new EmbeddedType(prefix, columnTypeMap);

    EmbeddedType result = child.merge(null);

    assertSame(child, result);
  }

  @Test
  void testMerge_concatenatesPrefixes() {
    String parentPrefix = "parent_";
    String childPrefix = "child_";
    EmbeddedType parent = new EmbeddedType(parentPrefix, Collections.emptyMap());
    EmbeddedType child = new EmbeddedType(childPrefix, Collections.emptyMap());

    EmbeddedType result = child.merge(parent);

    assertEquals("parent_child_", result.prefix());
  }

  @Test
  void testMerge_emptyPrefixes() {
    String parentPrefix = "";
    String childPrefix = "";
    EmbeddedType parent = new EmbeddedType(parentPrefix, Collections.emptyMap());
    EmbeddedType child = new EmbeddedType(childPrefix, Collections.emptyMap());

    EmbeddedType result = child.merge(parent);

    assertEquals("", result.prefix());
  }

  @Test
  void testMerge_parentEmptyPrefix() {
    String parentPrefix = "";
    String childPrefix = "child_";
    EmbeddedType parent = new EmbeddedType(parentPrefix, Collections.emptyMap());
    EmbeddedType child = new EmbeddedType(childPrefix, Collections.emptyMap());

    EmbeddedType result = child.merge(parent);

    assertEquals("child_", result.prefix());
  }

  @Test
  void testMerge_childEmptyPrefix() {
    String parentPrefix = "parent_";
    String childPrefix = "";
    EmbeddedType parent = new EmbeddedType(parentPrefix, Collections.emptyMap());
    EmbeddedType child = new EmbeddedType(childPrefix, Collections.emptyMap());

    EmbeddedType result = child.merge(parent);

    assertEquals("parent_", result.prefix());
  }

  @Test
  void testMerge_mergesColumnTypeMaps_parentOverridesChild() {
    ColumnType childColumnType = new ColumnType("child_col", true, false, false);
    ColumnType parentColumnType = new ColumnType("parent_col", false, true, true);
    ColumnType childOnlyColumnType = new ColumnType("child_only_col", null, null, null);
    ColumnType parentOnlyColumnType = new ColumnType("parent_only_col", null, null, null);

    Map<String, ColumnType> childMap =
        Map.of(
            "common_prop", childColumnType,
            "child_only_prop", childOnlyColumnType);
    Map<String, ColumnType> parentMap =
        Map.of(
            "common_prop", parentColumnType,
            "parent_only_prop", parentOnlyColumnType);

    EmbeddedType parent = new EmbeddedType("parent_", parentMap);
    EmbeddedType child = new EmbeddedType("child_", childMap);

    EmbeddedType result = child.merge(parent);

    Map<String, ColumnType> resultMap = result.columnTypeMap();
    assertEquals(3, resultMap.size());
    assertEquals(parentColumnType, resultMap.get("common_prop")); // Parent overrides child
    assertEquals(childOnlyColumnType, resultMap.get("child_only_prop"));
    assertEquals(parentOnlyColumnType, resultMap.get("parent_only_prop"));
  }

  @Test
  void testMerge_emptyColumnTypeMaps() {
    EmbeddedType parent = new EmbeddedType("parent_", Collections.emptyMap());
    EmbeddedType child = new EmbeddedType("child_", Collections.emptyMap());

    EmbeddedType result = child.merge(parent);

    assertTrue(result.columnTypeMap().isEmpty());
  }

  @Test
  void testMerge_parentEmptyColumnTypeMap() {
    ColumnType childColumnType = new ColumnType("child_col", null, null, null);
    Map<String, ColumnType> childMap = Map.of("prop1", childColumnType);

    EmbeddedType parent = new EmbeddedType("parent_", Collections.emptyMap());
    EmbeddedType child = new EmbeddedType("child_", childMap);

    EmbeddedType result = child.merge(parent);

    assertEquals(1, result.columnTypeMap().size());
    assertEquals(childColumnType, result.columnTypeMap().get("prop1"));
  }

  @Test
  void testMerge_childEmptyColumnTypeMap() {
    ColumnType parentColumnType = new ColumnType("parent_col", null, null, null);
    Map<String, ColumnType> parentMap = Map.of("prop1", parentColumnType);

    EmbeddedType parent = new EmbeddedType("parent_", parentMap);
    EmbeddedType child = new EmbeddedType("child_", Collections.emptyMap());

    EmbeddedType result = child.merge(parent);

    assertEquals(1, result.columnTypeMap().size());
    assertEquals(parentColumnType, result.columnTypeMap().get("prop1"));
  }

  @Test
  void testMerge_returnsImmutableColumnTypeMap() {
    ColumnType columnType = new ColumnType("col", null, null, null);
    Map<String, ColumnType> parentMap = Map.of("prop1", columnType);

    EmbeddedType parent = new EmbeddedType("parent_", parentMap);
    EmbeddedType child = new EmbeddedType("child_", Collections.emptyMap());

    EmbeddedType result = child.merge(parent);

    assertThrows(
        UnsupportedOperationException.class,
        () -> result.columnTypeMap().put("new_prop", new ColumnType("new_col", null, null, null)));
  }

  @Test
  void testMerge_returnsNewInstance() {
    EmbeddedType parent = new EmbeddedType("parent_", Collections.emptyMap());
    EmbeddedType child = new EmbeddedType("child_", Collections.emptyMap());

    EmbeddedType result = child.merge(parent);

    assertNotSame(child, result);
    assertNotSame(parent, result);
  }

  @Test
  void testMerge_complexScenario() {
    // Child has multiple properties with various overrides
    ColumnType childCol1 = new ColumnType("child_col1", true, false, false);
    ColumnType childCol2 = new ColumnType("child_col2", false, true, true);
    Map<String, ColumnType> childMap =
        Map.of(
            "prop1", childCol1,
            "prop2", childCol2);

    // Parent has overlapping and additional properties
    ColumnType parentCol1 = new ColumnType("parent_col1", false, true, true); // Overrides child
    ColumnType parentCol3 = new ColumnType("parent_col3", null, null, false);
    Map<String, ColumnType> parentMap =
        Map.of(
            "prop1", parentCol1, // This should override child's prop1
            "prop3", parentCol3);

    EmbeddedType parent = new EmbeddedType("parent_", parentMap);
    EmbeddedType child = new EmbeddedType("child_", childMap);

    EmbeddedType result = child.merge(parent);

    assertEquals("parent_child_", result.prefix());
    assertEquals(3, result.columnTypeMap().size());
    assertEquals(parentCol1, result.columnTypeMap().get("prop1")); // Parent override
    assertEquals(childCol2, result.columnTypeMap().get("prop2")); // Child only
    assertEquals(parentCol3, result.columnTypeMap().get("prop3")); // Parent only
  }
}
