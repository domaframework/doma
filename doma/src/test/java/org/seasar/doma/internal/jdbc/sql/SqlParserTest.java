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
package org.seasar.doma.internal.jdbc.sql;

import java.math.BigDecimal;
import java.util.Arrays;

import junit.framework.TestCase;

import org.seasar.doma.domain.BuiltinBigDecimalDomain;
import org.seasar.doma.domain.BuiltinIntegerDomain;
import org.seasar.doma.domain.BuiltinStringDomain;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class SqlParserTest extends TestCase {

    private final MockConfig config = new MockConfig();

    public void testBindVariable() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new BuiltinStringDomain("hoge"));
        evaluator.add("salary", new BuiltinBigDecimalDomain(new BigDecimal(
                10000)));
        String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, evaluator)
                .build(sqlNode);
        assertEquals("select * from aaa where ename = ? and sal = ?", sql
                .getRawSql());
        assertEquals("select * from aaa where ename = 'hoge' and sal = 10000",
                sql.getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(2, sql.getParameters().size());
        assertEquals(new BuiltinStringDomain("hoge"), sql.getParameters()
                .get(0).getDomain());
        assertEquals(new BuiltinBigDecimalDomain(new BigDecimal(10000)), sql
                .getParameters().get(1).getDomain());
    }

    public void testBindVariable_in() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", Arrays.asList(new BuiltinStringDomain("hoge"),
                new BuiltinStringDomain("foo")));
        String testSql = "select * from aaa where ename in /*name*/('aaa', 'bbb')";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, evaluator)
                .build(sqlNode);
        assertEquals("select * from aaa where ename in (?, ?)", sql.getRawSql());
        assertEquals("select * from aaa where ename in ('hoge', 'foo')", sql
                .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(2, sql.getParameters().size());
        assertEquals(new BuiltinStringDomain("hoge"), sql.getParameters()
                .get(0).getDomain());
        assertEquals(new BuiltinStringDomain("foo"), sql.getParameters().get(1)
                .getDomain());
    }

    public void testEmbeddedVariable() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new BuiltinStringDomain("hoge"));
        evaluator.add("salary", new BuiltinBigDecimalDomain(new BigDecimal(
                10000)));
        evaluator.add("orderBy", new BuiltinStringDomain(
                "order by name asc, salary"));
        String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, evaluator)
                .build(sqlNode);
        assertEquals(
                "select * from aaa where ename = ? and sal = ? order by name asc, salary",
                sql.getRawSql());
        assertEquals(
                "select * from aaa where ename = 'hoge' and sal = 10000 order by name asc, salary",
                sql.getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(2, sql.getParameters().size());
        assertEquals(new BuiltinStringDomain("hoge"), sql.getParameters()
                .get(0).getDomain());
        assertEquals(new BuiltinBigDecimalDomain(new BigDecimal(10000)), sql
                .getParameters().get(1).getDomain());
    }

    public void testEmbeddedVariable_containsSingleQuote() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new BuiltinStringDomain("hoge"));
        evaluator.add("salary", new BuiltinBigDecimalDomain(new BigDecimal(
                10000)));
        evaluator.add("orderBy", new BuiltinStringDomain("aaa'"));
        String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        try {
            new NodePreparedSqlBuilder(config, evaluator).build(sqlNode);
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(DomaMessageCode.DOMA2116, expected.getMessageCode());
        }
    }

    public void testIf() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new BuiltinStringDomain("hoge"));
        String testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, evaluator)
                .build(sqlNode);
        assertEquals("select * from aaa where bbb = ?", sql.getRawSql());
        assertEquals("select * from aaa where bbb = 'hoge'", sql
                .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(1, sql.getParameters().size());
        assertEquals(new BuiltinStringDomain("hoge"), sql.getParameters()
                .get(0).getDomain());
    }

    public void testIf_removeWhere() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new BuiltinStringDomain());
        String testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, evaluator)
                .build(sqlNode);
        assertEquals("select * from aaa", sql.getRawSql());
        assertEquals("select * from aaa", sql.getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(0, sql.getParameters().size());
    }

    public void testIf_nest() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new BuiltinStringDomain("hoge"));
        String testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%if name == \"hoge\"*/and ddd = eee/*%end*//*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, evaluator)
                .build(sqlNode);
        assertEquals("select * from aaa where bbb = ? and ddd = eee", sql
                .getRawSql());
        assertEquals("select * from aaa where bbb = 'hoge' and ddd = eee", sql
                .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(1, sql.getParameters().size());
    }

    public void testIf_nestContinuously() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new BuiltinStringDomain("hoge"));
        evaluator.add("name2", new BuiltinStringDomain());
        String testSql = "select * from aaa where /*%if name != null*/ /*%if name2 == \"hoge\"*/ ddd = eee/*%end*//*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, evaluator)
                .build(sqlNode);
        assertEquals("select * from aaa", sql.getRawSql());
        assertEquals("select * from aaa", sql.getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(0, sql.getParameters().size());
    }

    public void testElseif() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new BuiltinStringDomain(""));
        String testSql = "select * from aaa where /*%if name == null*/bbb is null--elseif name ==\"\"--bbb = /*name*/'ccc'/*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, evaluator)
                .build(sqlNode);
        assertEquals("select * from aaa where bbb = ?", sql.getRawSql());
        assertEquals("select * from aaa where bbb = ''", sql.getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(1, sql.getParameters().size());
        assertEquals(new BuiltinStringDomain(""), sql.getParameters().get(0)
                .getDomain());
    }

    public void testElse() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new BuiltinStringDomain("hoge"));
        String testSql = "select * from aaa where /*%if name == null*/bbb is null--elseif name == \"\"----else bbb = /*name*/'ccc'/*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, evaluator)
                .build(sqlNode);
        assertEquals("select * from aaa where  bbb = ?", sql.getRawSql());
        assertEquals("select * from aaa where  bbb = 'hoge'", sql
                .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(1, sql.getParameters().size());
        assertEquals(new BuiltinStringDomain("hoge"), sql.getParameters()
                .get(0).getDomain());
    }

    public void testSelect() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new BuiltinStringDomain("hoge"));
        evaluator.add("count", new BuiltinIntegerDomain(5));
        String testSql = "select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = /*name*/'ccc' group by aaa.deptname having count(*) > /*count*/10 order by aaa.name for update bbb";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, evaluator)
                .build(sqlNode);
        assertEquals(
                "select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = ? group by aaa.deptname having count(*) > ? order by aaa.name for update bbb",
                sql.getRawSql());
        assertEquals(
                "select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = 'hoge' group by aaa.deptname having count(*) > 5 order by aaa.name for update bbb",
                sql.getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(2, sql.getParameters().size());
        assertEquals(new BuiltinStringDomain("hoge"), sql.getParameters()
                .get(0).getDomain());
        assertEquals(new BuiltinIntegerDomain(5), sql.getParameters().get(1)
                .getDomain());
    }

    public void testUpdate() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("no", new BuiltinIntegerDomain(10));
        evaluator.add("name", new BuiltinStringDomain("hoge"));
        evaluator.add("id", new BuiltinIntegerDomain(100));
        String testSql = "update aaa set no = /*no*/1, set name = /*name*/'name' where id = /*id*/1";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, evaluator)
                .build(sqlNode);
        assertEquals("update aaa set no = ?, set name = ? where id = ?", sql
                .getRawSql());
        assertEquals(
                "update aaa set no = 10, set name = 'hoge' where id = 100", sql
                        .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(3, sql.getParameters().size());
        assertEquals(new BuiltinIntegerDomain(10), sql.getParameters().get(0)
                .getDomain());
        assertEquals(new BuiltinStringDomain("hoge"), sql.getParameters()
                .get(1).getDomain());
        assertEquals(new BuiltinIntegerDomain(100), sql.getParameters().get(2)
                .getDomain());
    }
}
