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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

import example.domain.PhoneNumber;

/**
 * @author taedium
 * 
 */
public class SqlParserTest extends TestCase {

    private final MockConfig config = new MockConfig();

    public void testBindVariable() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(
                10000)));
        String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where ename = ? and sal = ?",
                sql.getRawSql());
        assertEquals("select * from aaa where ename = 'hoge' and sal = 10000",
                sql.getFormattedSql());
        assertEquals(2, sql.getParameters().size());
        assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
        assertEquals(new BigDecimal(10000), sql.getParameters().get(1)
                .getWrapper().get());
    }

    public void testBindVariable_domain() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("phone", new Value(PhoneNumber.class, new PhoneNumber(
                "01-2345-6789")));
        String testSql = "select * from aaa where phone = /*phone*/'111'";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where phone = ?", sql.getRawSql());
        assertEquals("select * from aaa where phone = '01-2345-6789'",
                sql.getFormattedSql());
        assertEquals(1, sql.getParameters().size());
        assertEquals("01-2345-6789", sql.getParameters().get(0).getWrapper()
                .get());
    }

    public void testBindVariable_in() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name",
                new Value(List.class, Arrays.asList("hoge", "foo")));
        String testSql = "select * from aaa where ename in /*name*/('aaa', 'bbb')";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where ename in (?, ?)", sql.getRawSql());
        assertEquals("select * from aaa where ename in ('hoge', 'foo')",
                sql.getFormattedSql());
        assertEquals(2, sql.getParameters().size());
        assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
        assertEquals("foo", sql.getParameters().get(1).getWrapper().get());
    }

    public void testBindVariable_endsWithBindVariableComment() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        String testSql = "select * from aaa where ename = /*name*/";
        SqlParser parser = new SqlParser(testSql);
        try {
            parser.parse();
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2110, expected.getMessageResource());
        }
    }

    public void testBindVariable_emptyName() throws Exception {
        String testSql = "select * from aaa where ename = /*   */'aaa'";
        SqlParser parser = new SqlParser(testSql);
        try {
            parser.parse();
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2120, expected.getMessageResource());
        }
    }

    public void testBindVariable_stringLiteral() throws Exception {
        String testSql = "select * from aaa where ename = /*name*/'bbb'";
        SqlParser parser = new SqlParser(testSql);
        SqlNode node = parser.parse();
        assertNotNull(node);
    }

    public void testBindVariable_intLiteral() throws Exception {
        String testSql = "select * from aaa where ename = /*name*/10";
        SqlParser parser = new SqlParser(testSql);
        SqlNode node = parser.parse();
        assertNotNull(node);
    }

    public void testBindVariable_floatLiteral() throws Exception {
        String testSql = "select * from aaa where ename = /*name*/.0";
        SqlParser parser = new SqlParser(testSql);
        SqlNode node = parser.parse();
        assertNotNull(node);
    }

    public void testBindVariable_booleanTrueLiteral() throws Exception {
        String testSql = "select * from aaa where ename = /*name*/true";
        SqlParser parser = new SqlParser(testSql);
        SqlNode node = parser.parse();
        assertNotNull(node);
    }

    public void testBindVariable_booleanFalseLiteral() throws Exception {
        String testSql = "select * from aaa where ename = /*name*/false";
        SqlParser parser = new SqlParser(testSql);
        SqlNode node = parser.parse();
        assertNotNull(node);
    }

    public void testBindVariable_nullLiteral() throws Exception {
        String testSql = "select * from aaa where ename = /*name*/null";
        SqlParser parser = new SqlParser(testSql);
        SqlNode node = parser.parse();
        assertNotNull(node);
    }

    public void testBindVariable_illegalLiteral() throws Exception {
        String testSql = "select * from aaa where ename = /*name*/bbb";
        SqlParser parser = new SqlParser(testSql);
        try {
            parser.parse();
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2142, expected.getMessageResource());
        }
    }

    public void testBindVariable_enum() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(MyEnum.class, MyEnum.BBB));
        evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(
                10000)));
        String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where ename = ? and sal = ?",
                sql.getRawSql());
        assertEquals("select * from aaa where ename = 'BBB' and sal = 10000",
                sql.getFormattedSql());
        assertEquals(2, sql.getParameters().size());
        assertEquals(MyEnum.BBB, sql.getParameters().get(0).getWrapper().get());
        assertEquals(new BigDecimal(10000), sql.getParameters().get(1)
                .getWrapper().get());
    }

    public void testEmbeddedVariable() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(
                10000)));
        evaluator.add("orderBy", new Value(String.class,
                "order by name asc, salary"));
        String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals(
                "select * from aaa where ename = ? and sal = ? order by name asc, salary",
                sql.getRawSql());
        assertEquals(
                "select * from aaa where ename = 'hoge' and sal = 10000 order by name asc, salary",
                sql.getFormattedSql());
        assertEquals(2, sql.getParameters().size());
        assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
        assertEquals(new BigDecimal(10000), sql.getParameters().get(1)
                .getWrapper().get());
    }

    public void testEmbeddedVariable_inside() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(
                10000)));
        evaluator.add("table", new Value(String.class, "aaa"));
        String testSql = "select * from /*# table */ where ename = /*name*/'aaa' and sal = /*salary*/-2000";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where ename = ? and sal = ?",
                sql.getRawSql());
        assertEquals("select * from aaa where ename = 'hoge' and sal = 10000",
                sql.getFormattedSql());
        assertEquals(2, sql.getParameters().size());
        assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
        assertEquals(new BigDecimal(10000), sql.getParameters().get(1)
                .getWrapper().get());
    }

    public void testEmbeddedVariable_emptyName() throws Exception {
        String testSql = "select * from aaa where ename = /*#   */'aaa'";
        SqlParser parser = new SqlParser(testSql);
        try {
            parser.parse();
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2121, expected.getMessageResource());
        }
    }

    public void testIf() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        String testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where bbb = ?", sql.getRawSql());
        assertEquals("select * from aaa where bbb = 'hoge'",
                sql.getFormattedSql());
        assertEquals(1, sql.getParameters().size());
        assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    }

    public void testIf_fromClause() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("type", new Value(String.class, "a"));
        String testSql = "select * from /*%if type == \"a\"*/aaa--else bbb/*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa", sql.getRawSql());
        assertEquals("select * from aaa", sql.getFormattedSql());
    }

    public void testIf_selectClause() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("type", new Value(String.class, "a"));
        String testSql = "select /*%if type == \"a\"*/aaa --else bbb /*%end*/from ccc";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select aaa from ccc", sql.getRawSql());
        assertEquals("select aaa from ccc", sql.getFormattedSql());
    }

    public void testIf_removeWhere() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, null));
        String testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa", sql.getRawSql());
        assertEquals("select * from aaa", sql.getFormattedSql());
        assertEquals(0, sql.getParameters().size());
    }

    public void testIf_removeAnd() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, null));
        String testSql = "select * from aaa where \n/*%if name != null*/bbb = /*name*/'ccc' \n--else\n --comment\nand ddd is null\n /*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where \n\n --comment\n ddd is null",
                sql.getRawSql());
        assertEquals("select * from aaa where \n\n --comment\n ddd is null",
                sql.getFormattedSql());
        assertEquals(0, sql.getParameters().size());
    }

    public void testIf_nest() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        String testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%if name == \"hoge\"*/and ddd = eee/*%end*//*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where bbb = ? and ddd = eee",
                sql.getRawSql());
        assertEquals("select * from aaa where bbb = 'hoge' and ddd = eee",
                sql.getFormattedSql());
        assertEquals(1, sql.getParameters().size());
    }

    public void testIf_nestContinuously() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        evaluator.add("name2", new Value(String.class, null));
        String testSql = "select * from aaa where /*%if name != null*//*%if name2 == \"hoge\"*/ ddd = eee/*%end*//*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa", sql.getRawSql());
        assertEquals("select * from aaa", sql.getFormattedSql());
        assertEquals(0, sql.getParameters().size());
    }

    public void testElseifLine() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, ""));
        String testSql = "select * from aaa where /*%if name == null*/bbb is null--elseif name ==\"\"--bbb = /*name*/'ccc'/*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where bbb = ?", sql.getRawSql());
        assertEquals("select * from aaa where bbb = ''", sql.getFormattedSql());
        assertEquals(1, sql.getParameters().size());
        assertEquals("", sql.getParameters().get(0).getWrapper().get());
    }

    public void testElseifBlock() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, ""));
        String testSql = "select * from aaa where /*%if name == null*/bbb is null\n/*%elseif name ==\"\"*/\nbbb = /*name*/'ccc'/*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where \nbbb = ?", sql.getRawSql());
        assertEquals("select * from aaa where \nbbb = ''",
                sql.getFormattedSql());
        assertEquals(1, sql.getParameters().size());
        assertEquals("", sql.getParameters().get(0).getWrapper().get());
    }

    public void testElseLine() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        String testSql = "select * from aaa where /*%if name == null*/bbb is null--elseif name == \"\"----else bbb = /*name*/'ccc'/*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where  bbb = ?", sql.getRawSql());
        assertEquals("select * from aaa where  bbb = 'hoge'",
                sql.getFormattedSql());
        assertEquals(1, sql.getParameters().size());
        assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    }

    public void testElseBlock() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        String testSql = "select * from aaa where /*%if name == null*/bbb is null\n/*%elseif name == \"\"*/\n/*%else*/ bbb = /*name*/'ccc'/*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where  bbb = ?", sql.getRawSql());
        assertEquals("select * from aaa where  bbb = 'hoge'",
                sql.getFormattedSql());
        assertEquals(1, sql.getParameters().size());
        assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    }

    public void testUnion() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String testSql = "select * from aaa where /*%if false*//*%end*/union all select * from bbb";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa union all select * from bbb",
                sql.getRawSql());
    }

    public void testSelect() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        evaluator.add("count", new Value(Integer.class, 5));
        String testSql = "select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = /*name*/'ccc' group by aaa.deptname having count(*) > /*count*/10 order by aaa.name for update bbb";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals(
                "select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = ? group by aaa.deptname having count(*) > ? order by aaa.name for update bbb",
                sql.getRawSql());
        assertEquals(
                "select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = 'hoge' group by aaa.deptname having count(*) > 5 order by aaa.name for update bbb",
                sql.getFormattedSql());
        assertEquals(2, sql.getParameters().size());
        assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
        assertEquals(new Integer(5), sql.getParameters().get(1).getWrapper()
                .get());
    }

    public void testUpdate() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("no", new Value(Integer.class, 10));
        evaluator.add("name", new Value(String.class, "hoge"));
        evaluator.add("id", new Value(Integer.class, 100));
        String testSql = "update aaa set no = /*no*/1, set name = /*name*/'name' where id = /*id*/1";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("update aaa set no = ?, set name = ? where id = ?",
                sql.getRawSql());
        assertEquals(
                "update aaa set no = 10, set name = 'hoge' where id = 100",
                sql.getFormattedSql());
        assertEquals(3, sql.getParameters().size());
        assertEquals(new Integer(10), sql.getParameters().get(0).getWrapper()
                .get());
        assertEquals("hoge", sql.getParameters().get(1).getWrapper().get());
        assertEquals(new Integer(100), sql.getParameters().get(2).getWrapper()
                .get());
    }

    public void testFor() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        ArrayList<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        evaluator.add("names", new Value(List.class, list));
        String testSql = "select * from aaa where /*%for n : names*/name = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals(
                "select * from aaa where name = ? or name = ? or name = ?",
                sql.getRawSql());
        assertEquals(
                "select * from aaa where name = 'aaa' or name = 'bbb' or name = 'ccc'",
                sql.getFormattedSql());
        assertEquals(3, sql.getParameters().size());
        assertEquals("aaa", sql.getParameters().get(0).getWrapper().get());
        assertEquals("bbb", sql.getParameters().get(1).getWrapper().get());
        assertEquals("ccc", sql.getParameters().get(2).getWrapper().get());
    }

    public void testFor_removeWhere() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        ArrayList<String> list = new ArrayList<String>();
        evaluator.add("names", new Value(List.class, list));
        String testSql = "select * from aaa where /*%for n : names*/name = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa", sql.getRawSql());
        assertEquals("select * from aaa", sql.getFormattedSql());
        assertEquals(0, sql.getParameters().size());
    }

    public void testFor_removeOr() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        ArrayList<String> list = new ArrayList<String>();
        evaluator.add("names", new Value(List.class, list));
        String testSql = "select * from aaa where /*%for n : names*/name = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/ or salary > 100";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where   salary > 100", sql.getRawSql());
        assertEquals("select * from aaa where   salary > 100",
                sql.getFormattedSql());
        assertEquals(0, sql.getParameters().size());
    }

    public void testFor_index() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        ArrayList<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        evaluator.add("names", new Value(List.class, list));
        String testSql = "select * from aaa where /*%for n : names*/name/*# n_index */ = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals(
                "select * from aaa where name0 = ? or name1 = ? or name2 = ?",
                sql.getRawSql());
        assertEquals(
                "select * from aaa where name0 = 'aaa' or name1 = 'bbb' or name2 = 'ccc'",
                sql.getFormattedSql());
        assertEquals(3, sql.getParameters().size());
        assertEquals("aaa", sql.getParameters().get(0).getWrapper().get());
        assertEquals("bbb", sql.getParameters().get(1).getWrapper().get());
        assertEquals("ccc", sql.getParameters().get(2).getWrapper().get());
    }

    public void testValidate_ifEnd() throws Exception {
        SqlParser parser = new SqlParser("select * from aaa /*%if true*/");
        try {
            parser.parse();
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2133, expected.getMessageResource());
        }
    }

    public void testValidate_ifEnd_selectClause() throws Exception {
        SqlParser parser = new SqlParser("select /*%if true*/* from aaa");
        try {
            parser.parse();
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2133, expected.getMessageResource());
        }
    }

    public void testValidate_ifEnd_subquery() throws Exception {
        SqlParser parser = new SqlParser(
                "select *, (select /*%if true */ from aaa) x from aaa");
        try {
            parser.parse();
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2133, expected.getMessageResource());
        }
    }

    public void testValidate_forEnd() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa /*%for name : names*/");
        try {
            parser.parse();
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2134, expected.getMessageResource());
        }
    }

    public void testValidate_unclosedParens() throws Exception {
        SqlParser parser = new SqlParser("select * from (select * from bbb");
        try {
            parser.parse();
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2135, expected.getMessageResource());
        }
    }

    public void testValidate_enclosedParensByIfBlock() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from /*%if true*/(select * from bbb)/*%end*/");
        parser.parse();
    }

    public void testParens_removeAnd() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, null));
        String testSql = "select * from aaa where (\n/*%if name != null*/bbb = /*name*/'ccc'\n/*%else*/\nand ddd is null\n /*%end*/)";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where (\n\n ddd is null\n )",
                sql.getRawSql());
        assertEquals("select * from aaa where (\n\n ddd is null\n )",
                sql.getFormattedSql());
        assertEquals(0, sql.getParameters().size());
    }

    public void testEmptyParens() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        SqlParser parser = new SqlParser("select rank()");
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select rank()", sql.getRawSql());
    }

    public void testEmptyParens_whiteSpace() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        SqlParser parser = new SqlParser("select rank(   )");
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select rank(   )", sql.getRawSql());
    }

    public void testManyEol() throws Exception {
        String path = "META-INF/" + getClass().getName().replace('.', '/')
                + "/manyEol.sql";
        String sql = ResourceUtil.getResourceAsString(path);
        SqlParser parser = new SqlParser(sql);
        SqlNode sqlNode = parser.parse();
        assertNotNull(sqlNode);
    }

    public enum MyEnum {
        AAA, BBB, CCC
    }
}
