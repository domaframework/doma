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
package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class PaddingIteratorTest {

  @Test
  void testNext_WithElementsAndPadding() {
    Iterator<Integer> baseIterator = Arrays.asList(1, 2, 3).iterator();
    PaddingIterator<Integer> paddingIterator = new PaddingIterator<>(baseIterator, 2);

    assertTrue(paddingIterator.hasNext());
    assertEquals(1, paddingIterator.next());

    assertTrue(paddingIterator.hasNext());
    assertEquals(2, paddingIterator.next());

    assertTrue(paddingIterator.hasNext());
    assertEquals(3, paddingIterator.next());

    assertTrue(paddingIterator.hasNext());
    assertEquals(3, paddingIterator.next());

    assertTrue(paddingIterator.hasNext());
    assertEquals(3, paddingIterator.next());

    assertFalse(paddingIterator.hasNext());
    assertThrows(NoSuchElementException.class, paddingIterator::next);
  }

  @Test
  void testNext_WithOnlyPadding() {
    Iterator<Integer> baseIterator = Collections.emptyIterator();
    PaddingIterator<Integer> paddingIterator = new PaddingIterator<>(baseIterator, 3);

    assertTrue(paddingIterator.hasNext());
    assertNull(paddingIterator.next());

    assertTrue(paddingIterator.hasNext());
    assertNull(paddingIterator.next());

    assertTrue(paddingIterator.hasNext());
    assertNull(paddingIterator.next());

    assertFalse(paddingIterator.hasNext());
    assertThrows(NoSuchElementException.class, paddingIterator::next);
  }

  @Test
  void testNext_WithNoElementsNoPadding() {
    Iterator<Integer> baseIterator = Collections.emptyIterator();
    PaddingIterator<Integer> paddingIterator = new PaddingIterator<>(baseIterator, 0);

    assertFalse(paddingIterator.hasNext());
    assertThrows(NoSuchElementException.class, paddingIterator::next);
  }

  @Test
  void testNext_WithSingleElementAndPadding() {
    Iterator<String> baseIterator = Collections.singletonList("Test").iterator();
    PaddingIterator<String> paddingIterator = new PaddingIterator<>(baseIterator, 1);

    assertTrue(paddingIterator.hasNext());
    assertEquals("Test", paddingIterator.next());

    assertTrue(paddingIterator.hasNext());
    assertEquals("Test", paddingIterator.next());

    assertFalse(paddingIterator.hasNext());
    assertThrows(NoSuchElementException.class, paddingIterator::next);
  }

  @Test
  void testNext_WithMultipleElementsNoPadding() {
    Iterator<Double> baseIterator = Arrays.asList(1.1, 2.2, 3.3).iterator();
    PaddingIterator<Double> paddingIterator = new PaddingIterator<>(baseIterator, 0);

    assertTrue(paddingIterator.hasNext());
    assertEquals(1.1, paddingIterator.next());

    assertTrue(paddingIterator.hasNext());
    assertEquals(2.2, paddingIterator.next());

    assertTrue(paddingIterator.hasNext());
    assertEquals(3.3, paddingIterator.next());

    assertFalse(paddingIterator.hasNext());
    assertThrows(NoSuchElementException.class, paddingIterator::next);
  }

  @Test
  void testConstructor_WithNegativePadding() {
    Iterator<Integer> baseIterator = Collections.emptyIterator();
    assertThrows(IllegalArgumentException.class, () -> new PaddingIterator<>(baseIterator, -1));
  }

  @Test
  void testConstructor_WithNullIterator() {
    assertThrows(NullPointerException.class, () -> new PaddingIterator<>(null, 1));
  }
}
