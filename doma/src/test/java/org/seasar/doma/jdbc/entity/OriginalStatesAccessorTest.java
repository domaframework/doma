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
package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class OriginalStatesAccessorTest extends TestCase {

    public void testGet() throws Exception {
        Hoge states = new Hoge();
        Hoge hoge = new Hoge();
        hoge.setOriginalStates(states);
        OriginalStatesAccessor<Hoge> accessor = new OriginalStatesAccessor<OriginalStatesAccessorTest.Hoge>(
                Hoge.class, "originalStates");
        assertSame(states, accessor.get(hoge));
    }

    public void testSet() throws Exception {
        Hoge states = new Hoge();
        Hoge hoge = new Hoge();
        OriginalStatesAccessor<Hoge> accessor = new OriginalStatesAccessor<OriginalStatesAccessorTest.Hoge>(
                Hoge.class, "originalStates");
        accessor.set(hoge, states);
        assertSame(states, hoge.getOriginalStates());
    }

    public static class Hoge {

        private Hoge originalStates;

        public Hoge getOriginalStates() {
            return originalStates;
        }

        public void setOriginalStates(Hoge originalStates) {
            this.originalStates = originalStates;
        }
    }
}
