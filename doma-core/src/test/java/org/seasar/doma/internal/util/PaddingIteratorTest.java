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
