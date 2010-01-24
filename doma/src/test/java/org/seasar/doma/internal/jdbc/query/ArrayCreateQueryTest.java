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
package org.seasar.doma.internal.jdbc.query;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;

/**
 * @author taedium
 * 
 */
public class ArrayCreateQueryTest extends TestCase {

    private final MockConfig config = new MockConfig();

    public void testPrepare() throws Exception {
        ArrayCreateQuery query = new ArrayCreateQuery();
        query.setConfig(config);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setTypeName("varchar");
        query.setElements(new String[] {});
        query.prepare();
    }
}
