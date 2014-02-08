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

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;

/**
 * @author taedium
 * 
 */
public class UpdateBuilderTest extends TestCase {

    public void test() throws Exception {
        UpdateBuilder builder = UpdateBuilder.newInstance(new MockConfig());
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").param(String.class, "SMIHT").sql(",");
        builder.sql("salary = ")
                .param(BigDecimal.class, new BigDecimal("1000"));
        builder.sql("where");
        builder.sql("ID = ").param(int.class, 10);
        builder.execute();
    }
}
