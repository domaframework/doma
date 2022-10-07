package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CombinationsTest {

  @Test
  void test() {
    Combinations<String> combinations = new Combinations<>();
    combinations.add(new Pair<>("A", "B"));
    combinations.add(new Pair<>("A", "C"));

    assertTrue(combinations.contains(new Pair<>("A", "B")));
    assertTrue(combinations.contains(new Pair<>("B", "A")));
    assertTrue(combinations.contains(new Pair<>("A", "C")));
    assertTrue(combinations.contains(new Pair<>("C", "A")));
    assertFalse(combinations.contains(new Pair<>("B", "C")));
    assertFalse(combinations.contains(new Pair<>("C", "B")));
  }
}
