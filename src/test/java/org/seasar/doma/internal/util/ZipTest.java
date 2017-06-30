/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import junit.framework.TestCase;

public class ZipTest extends TestCase {

    public void testStream() throws Exception {
        Stream<String> first = Stream.of("a", "b", "c");
        Stream<Integer> second = Stream.of(1, 2, 3, 4, 5);
        Stream<Pair<String, Integer>> stream = Zip.stream(first, second);
        List<Pair<String, Integer>> list = stream.collect(Collectors.toList());
        assertEquals(3, list.size());
        Pair<String, Integer> p0 = list.get(0);
        assertEquals("a", p0.fst);
        assertEquals(Integer.valueOf(1), p0.snd);
        Pair<String, Integer> p1 = list.get(1);
        assertEquals("b", p1.fst);
        assertEquals(Integer.valueOf(2), p1.snd);
        Pair<String, Integer> p2 = list.get(2);
        assertEquals("c", p2.fst);
        assertEquals(Integer.valueOf(3), p2.snd);
    }
}
