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
package org.seasar.doma.internal.jdbc.sql;

import java.math.BigDecimal;
import java.util.function.Function;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.domain.DomainTypeFactory;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.Wrapper;

import example.domain.PhoneNumber;

/**
 * @author taedium
 * 
 */
public class PreparedSqlBuilderTest extends TestCase {

    private final MockConfig config = new MockConfig();

    public void testAppend() throws Exception {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config,
                SqlKind.SELECT, SqlLogType.FORMATTED);
        builder.appendSql("select * from aaa where name = ");
        Wrapper<String> stringWrapper = new StringWrapper("hoge");
        builder.appendParameter(new BasicInParameter<String>(
                () -> stringWrapper));

        builder.appendSql(" and salary = ");
        Wrapper<BigDecimal> bigDecimalWrapper = new BigDecimalWrapper(
                new BigDecimal(100));
        builder.appendParameter(new BasicInParameter<BigDecimal>(
                () -> bigDecimalWrapper));
        PreparedSql sql = builder.build(Function.identity());
        assertEquals("select * from aaa where name = ? and salary = ?",
                sql.toString());
    }

    public void testAppendParameter_domain() throws Exception {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config,
                SqlKind.SELECT, SqlLogType.FORMATTED);
        builder.appendSql("select * from aaa where phoneNumber = ");
        DomainType<String, PhoneNumber> phoneNumberType = DomainTypeFactory
                .getDomainType(PhoneNumber.class, new ClassHelper() {
                });
        PhoneNumber phoneNumber = new PhoneNumber("03-1234-5678");
        builder.appendParameter(new DomainInParameter<String, PhoneNumber>(
                phoneNumberType, phoneNumber));
        PreparedSql sql = builder.build(Function.identity());
        assertEquals("select * from aaa where phoneNumber = ?", sql.toString());
    }

    public void testCutBackSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config,
                SqlKind.SELECT, SqlLogType.FORMATTED);
        builder.appendSql("select * from aaa where name = ");
        builder.cutBackSql(14);
        PreparedSql sql = builder.build(Function.identity());
        assertEquals("select * from aaa", sql.toString());
    }
}
