package org.seasar.doma.internal.util;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ZipTest {

  @Test
  void stream() {
    List<String> a = Arrays.asList("a", "b", "c");
    List<Integer> b = Arrays.asList(1, 2, 3, 4, 5);
    List<Pair<String, Integer>> result = Zip.stream(a, b).collect(toList());
    assertEquals(3, result.size());
    assertEquals("a", result.get(0).fst);
    assertEquals(Integer.valueOf(1), result.get(0).snd);
    assertEquals("b", result.get(1).fst);
    assertEquals(Integer.valueOf(2), result.get(1).snd);
    assertEquals("c", result.get(2).fst);
    assertEquals(Integer.valueOf(3), result.get(2).snd);
  }
}
