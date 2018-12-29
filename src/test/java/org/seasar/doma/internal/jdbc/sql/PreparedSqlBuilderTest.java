package org.seasar.doma.internal.jdbc.sql;

import example.domain.PhoneNumber;
import java.math.BigDecimal;
import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.domain.DomainTypeFactory;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.Wrapper;

public class PreparedSqlBuilderTest extends TestCase {

  private final MockConfig config = new MockConfig();

  public void testAppend() throws Exception {
    PreparedSqlBuilder builder =
        new PreparedSqlBuilder(config, SqlKind.SELECT, SqlLogType.FORMATTED);
    builder.appendSql("select * from aaa where name = ");
    Wrapper<String> stringWrapper = new StringWrapper("hoge");
    builder.appendParameter(new BasicInParameter<String>(() -> stringWrapper));

    builder.appendSql(" and salary = ");
    Wrapper<BigDecimal> bigDecimalWrapper = new BigDecimalWrapper(new BigDecimal(100));
    builder.appendParameter(new BasicInParameter<BigDecimal>(() -> bigDecimalWrapper));
    PreparedSql sql = builder.build(Function.identity());
    assertEquals("select * from aaa where name = ? and salary = ?", sql.toString());
  }

  public void testAppendParameter_domain() throws Exception {
    PreparedSqlBuilder builder =
        new PreparedSqlBuilder(config, SqlKind.SELECT, SqlLogType.FORMATTED);
    builder.appendSql("select * from aaa where phoneNumber = ");
    DomainType<String, PhoneNumber> phoneNumberType =
        DomainTypeFactory.getDomainType(PhoneNumber.class, new ClassHelper() {});
    PhoneNumber phoneNumber = new PhoneNumber("03-1234-5678");
    builder.appendParameter(
        new DomainInParameter<String, PhoneNumber>(phoneNumberType, phoneNumber));
    PreparedSql sql = builder.build(Function.identity());
    assertEquals("select * from aaa where phoneNumber = ?", sql.toString());
  }

  public void testCutBackSql() {
    PreparedSqlBuilder builder =
        new PreparedSqlBuilder(config, SqlKind.SELECT, SqlLogType.FORMATTED);
    builder.appendSql("select * from aaa where name = ");
    builder.cutBackSql(14);
    PreparedSql sql = builder.build(Function.identity());
    assertEquals("select * from aaa", sql.toString());
  }
}
