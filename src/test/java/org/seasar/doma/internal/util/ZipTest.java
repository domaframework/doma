package org.seasar.doma.internal.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import junit.framework.TestCase;

public class ZipTest extends TestCase {

  public void testStream() throws Exception {
    var first = Stream.of("a", "b", "c");
    var second = Stream.of(1, 2, 3, 4, 5);
    var stream = Zip.stream(first, second);
    var list = stream.collect(Collectors.toList());
    assertEquals(3, list.size());
    var p0 = list.get(0);
    assertEquals("a", p0.fst);
    assertEquals(Integer.valueOf(1), p0.snd);
    var p1 = list.get(1);
    assertEquals("b", p1.fst);
    assertEquals(Integer.valueOf(2), p1.snd);
    var p2 = list.get(2);
    assertEquals("c", p2.fst);
    assertEquals(Integer.valueOf(3), p2.snd);
  }
}
