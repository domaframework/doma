/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class CompositeIteratorTest extends TestCase {

    public void testNext() throws Exception {
        List<String> list1 = Arrays.asList("aaa", "bbb");
        List<String> list2 = Arrays.asList("ccc");
        List<Iterator<? extends String>> iteratorList = new ArrayList<Iterator<? extends String>>();
        iteratorList.add(list1.iterator());
        iteratorList.add(list2.iterator());

        CompositeIterator<String> iterator = new CompositeIterator<String>(
                iteratorList);
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            fail();
        } catch (NoSuchElementException expected) {
        }
    }

    public void testRemove() throws Exception {
        List<String> list1 = new ArrayList<String>();
        list1.add("aaa");
        list1.add("bbb");
        List<String> list2 = new ArrayList<String>();
        list2.add("ccc");
        List<Iterator<? extends String>> iteratorList = new ArrayList<Iterator<? extends String>>();
        iteratorList.add(list1.iterator());
        iteratorList.add(list2.iterator());

        CompositeIterator<String> iterator = new CompositeIterator<String>(
                iteratorList);
        try {
            iterator.remove();
            fail();
        } catch (IllegalStateException expected) {
        }
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        iterator.remove();
        assertEquals(Arrays.asList("bbb"), list1);

        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        iterator.remove();
        assertTrue(list1.isEmpty());

        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        iterator.remove();
        assertTrue(list2.isEmpty());
    }
}
