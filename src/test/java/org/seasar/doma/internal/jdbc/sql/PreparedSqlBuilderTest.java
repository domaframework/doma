package org.seasar.doma.internal.jdbc.sql;

import java.math.BigDecimal;
import java.util.function.Function;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.holder.HolderDesc;
import org.seasar.doma.jdbc.holder.HolderDescFactory;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.Wrapper;

import example.holder.PhoneNumber;
import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class PreparedSqlBuilderTest extends TestCase {

    private final MockConfig config = new MockConfig();

    public void testAppend() throws Exception {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.SELECT,
                SqlLogType.FORMATTED);
        builder.appendSql("select * from aaa where name = ");
        Wrapper<String> stringWrapper = new StringWrapper();
        builder.appendParameter(
                new ScalarInParameter<>(() -> new BasicScalar<>(stringWrapper, false), "hoge"));

        builder.appendSql(" and salary = ");
        Wrapper<BigDecimal> bigDecimalWrapper = new BigDecimalWrapper();
        builder.appendParameter(new ScalarInParameter<>(
                () -> new BasicScalar<>(bigDecimalWrapper, false), new BigDecimal(100)));
        PreparedSql sql = builder.build(Function.identity());
        assertEquals("select * from aaa where name = ? and salary = ?", sql.toString());
    }

    public void testAppendParameter_holder() throws Exception {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.SELECT,
                SqlLogType.FORMATTED);
        builder.appendSql("select * from aaa where phoneNumber = ");
        HolderDesc<String, PhoneNumber> phoneNumberType = HolderDescFactory
                .getHolderDesc(PhoneNumber.class, new ClassHelper() {
                });
        PhoneNumber phoneNumber = new PhoneNumber("03-1234-5678");
        builder.appendParameter(
                new ScalarInParameter<>(() -> phoneNumberType.createScalar(), phoneNumber));
        PreparedSql sql = builder.build(Function.identity());
        assertEquals("select * from aaa where phoneNumber = ?", sql.toString());
    }

    public void testCutBackSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.SELECT,
                SqlLogType.FORMATTED);
        builder.appendSql("select * from aaa where name = ");
        builder.cutBackSql(14);
        PreparedSql sql = builder.build(Function.identity());
        assertEquals("select * from aaa", sql.toString());
    }
}
