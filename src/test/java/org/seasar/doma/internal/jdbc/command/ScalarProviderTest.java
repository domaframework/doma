package org.seasar.doma.internal.jdbc.command;

import example.holder.PhoneNumber;
import example.holder._PhoneNumber;
import java.lang.reflect.Method;
import junit.framework.TestCase;
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
import org.seasar.doma.jdbc.holder.HolderDesc;
import org.seasar.doma.jdbc.query.SelectQuery;
import org.seasar.doma.wrapper.StringWrapper;

public class ScalarProviderTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testBasic() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("aaa"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("hoge"));
    resultSet.next();

    var provider =
        new ScalarProvider<>(
            () -> new org.seasar.doma.internal.jdbc.scalar.BasicScalar<>(StringWrapper::new, false),
            new MySelectQuery());
    var result = provider.get(resultSet);

    assertEquals("hoge", result);
  }

  public void testOptionalBasic() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("aaa"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("hoge"));
    resultSet.next();

    var provider =
        new ScalarProvider<>(
            () ->
                new org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar<>(StringWrapper::new),
            new MySelectQuery());
    var result = provider.get(resultSet);

    assertEquals("hoge", result.get());
  }

  public void testHolder() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("aaa"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("hoge"));
    resultSet.next();

    HolderDesc<String, PhoneNumber> holderDesc = _PhoneNumber.getSingletonInternal();

    var provider = new ScalarProvider<>(holderDesc::createScalar, new MySelectQuery());
    var result = provider.get(resultSet);

    assertEquals("hoge", result.getValue());
  }

  public void testOptionalHolder() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("aaa"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("hoge"));
    resultSet.next();

    HolderDesc<String, PhoneNumber> holderDesc = _PhoneNumber.getSingletonInternal();

    var provider = new ScalarProvider<>(holderDesc::createOptionalScalar, new MySelectQuery());
    var result = provider.get(resultSet);

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
