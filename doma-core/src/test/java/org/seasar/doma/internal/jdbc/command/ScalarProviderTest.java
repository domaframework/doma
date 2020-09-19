package org.seasar.doma.internal.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import example.domain.PhoneNumber;
import example.domain._PhoneNumber;
import java.lang.reflect.Method;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.query.SelectQuery;

public class ScalarProviderTest {

  private final MockConfig runtimeConfig = new MockConfig();

  @Test
  public void testBasic() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("aaa"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("hoge"));
    resultSet.next();

    ScalarProvider<String, String> provider =
        new ScalarProvider<>(
            () ->
                new org.seasar.doma.internal.jdbc.scalar.BasicScalar<>(
                    () -> new org.seasar.doma.wrapper.StringWrapper()),
            new MySelectQuery());
    String result = provider.get(resultSet);

    assertEquals("hoge", result);
  }

  @Test
  public void testOptionalBasic() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("aaa"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("hoge"));
    resultSet.next();

    ScalarProvider<String, Optional<String>> provider =
        new ScalarProvider<>(
            () ->
                new org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar<>(
                    () -> new org.seasar.doma.wrapper.StringWrapper()),
            new MySelectQuery());
    Optional<String> result = provider.get(resultSet);

    assertEquals("hoge", result.get());
  }

  @Test
  public void testDomain() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("aaa"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("hoge"));
    resultSet.next();

    DomainType<String, PhoneNumber> domainType = _PhoneNumber.getSingletonInternal();

    ScalarProvider<String, PhoneNumber> provider =
        new ScalarProvider<>(() -> domainType.createScalar(), new MySelectQuery());
    PhoneNumber result = provider.get(resultSet);

    assertEquals("hoge", result.getValue());
  }

  @Test
  public void testOptionalDomain() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("aaa"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("hoge"));
    resultSet.next();

    DomainType<String, PhoneNumber> domainType = _PhoneNumber.getSingletonInternal();

    ScalarProvider<String, Optional<PhoneNumber>> provider =
        new ScalarProvider<>(() -> domainType.createOptionalScalar(), new MySelectQuery());
    Optional<PhoneNumber> result = provider.get(resultSet);

    assertEquals("hoge", result.get().getValue());
  }

  protected class MySelectQuery implements SelectQuery {

    @Override
    public SelectOptions getOptions() {
      return SelectOptions.get();
    }

    @Override
    public Config getConfig() {
      return runtimeConfig;
    }

    @Override
    public String getClassName() {
      return null;
    }

    @Override
    public String getMethodName() {
      return null;
    }

    @Override
    public PreparedSql getSql() {
      return null;
    }

    @Override
    public boolean isResultEnsured() {
      return false;
    }

    @Override
    public boolean isResultMappingEnsured() {
      return false;
    }

    @Override
    public FetchType getFetchType() {
      return FetchType.LAZY;
    }

    @Override
    public int getFetchSize() {
      return 0;
    }

    @Override
    public int getMaxRows() {
      return 0;
    }

    @Override
    public int getQueryTimeout() {
      return 0;
    }

    @Override
    public void prepare() {}

    @Override
    public void complete() {}

    @Override
    public Method getMethod() {
      return null;
    }

    @Override
    public SqlLogType getSqlLogType() {
      return null;
    }

    @Override
    public String comment(String sql) {
      return sql;
    }

    @Override
    public boolean isResultStream() {
      return false;
    }
  }
}
