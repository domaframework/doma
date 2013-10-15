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
package org.seasar.doma.jdbc;

import junit.framework.TestCase;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;

/**
 * @author backpaper0
 * 
 */
public class SelectOptionsTest extends TestCase {

    public void testForUpdateParameter() throws Exception {
        String[] aliases = null;
        try {
            SelectOptions.get().forUpdate(aliases);
            fail();
        } catch (DomaNullPointerException expected) {
            assertEquals("aliases", expected.getParameterName());
        }
    }

    public void testForUpdateNowaitParameter() throws Exception {
        String[] aliases = null;
        try {
            SelectOptions.get().forUpdateNowait(aliases);
            fail();
        } catch (DomaNullPointerException expected) {
            assertEquals("aliases", expected.getParameterName());
        }
    }

    public void testForUpdateWaitParameter1() throws Exception {
        int waitSeconds = -1;
        try {
            SelectOptions.get().forUpdateWait(waitSeconds);
            fail();
        } catch (DomaIllegalArgumentException expected) {
            assertEquals("waitSeconds", expected.getParameterName());
            assertEquals("waitSeconds < 0", expected.getDescription());
        }
    }

    public void testForUpdateWaitParameter2() throws Exception {
        int waitSeconds = -1;
        String[] aliases = { "a" };
        try {
            SelectOptions.get().forUpdateWait(waitSeconds, aliases);
            fail();
        } catch (DomaIllegalArgumentException expected) {
            assertEquals("waitSeconds", expected.getParameterName());
            assertEquals("waitSeconds < 0", expected.getDescription());
        }
    }

    public void testForUpdateWaitParameter3() throws Exception {
        int waitSeconds = 1;
        String[] aliases = null;
        try {
            SelectOptions.get().forUpdateWait(waitSeconds, aliases);
            fail();
        } catch (DomaNullPointerException expected) {
            assertEquals("aliases", expected.getParameterName());
        }
    }

    public void testOffsetParameter() throws Exception {
        int offset = -1;
        try {
            SelectOptions.get().offset(offset);
            fail();
        } catch (DomaIllegalArgumentException expected) {
            assertEquals("offset", expected.getParameterName());
            assertEquals("offset < 0", expected.getDescription());
        }
    }

    public void testLimitParameter() throws Exception {
        int limit = -1;
        try {
            SelectOptions.get().limit(limit);
            fail();
        } catch (DomaIllegalArgumentException expected) {
            assertEquals("limit", expected.getParameterName());
            assertEquals("limit < 0", expected.getDescription());
        }
    }
}
