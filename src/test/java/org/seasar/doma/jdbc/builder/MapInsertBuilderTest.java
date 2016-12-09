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
package org.seasar.doma.jdbc.builder;

import java.util.LinkedHashMap;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlParameter;

/**
 * @author bakenezumi
 * 
 */
public class MapInsertBuilderTest extends TestCase {

    public void test() throws Exception {
        MapInsertBuilder builder = MapInsertBuilder.newInstance(new MockConfig(), "Emp");
        builder.execute(new LinkedHashMap(){{
            put("name", "SMITH");
            put("salary", 100);
        }});
    }

    public void testGetSql() throws Exception {
        MapInsertBuilder builder = MapInsertBuilder.newInstance(new MockConfig(), "Emp");

        builder.execute(new LinkedHashMap(){{
            put("name", "SMITH");
            put("salary", 100);
        }});

        String expectedSql = String.format("insert into Emp" + " (name, salary)%n"
                + "values (?, ?)");
        assertEquals(expectedSql, builder.getSql().getRawSql());

        List<? extends SqlParameter> parameters = builder.getSql().getParameters();
        assertEquals(2, parameters.size());
        assertEquals("SMITH", parameters.get(0).getValue());
        assertEquals(100, parameters.get(1).getValue());
    }

}
