package org.seasar.doma.jdbc.command;

import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.internal.jdbc.sql.ScalarInOutParameter;
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter;
import org.seasar.doma.internal.jdbc.sql.ScalarOutParameter;
import org.seasar.doma.internal.jdbc.sql.ScalarSingleResultParameter;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.AutoFunctionQuery;
import org.seasar.doma.wrapper.IntegerWrapper;

public class FunctionCommandTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testExecute() throws Exception {
    var outParameters = runtimeConfig.dataSource.connection.callableStatement.outParameters;
    outParameters.add(10);
    outParameters.add(null);
    outParameters.add(20);
    outParameters.add(30);

    var aaa = new IntegerWrapper();
    var bbb = new IntegerWrapper(50);
    var ccc = new IntegerWrapper(60);

    var query = new AutoFunctionQuery<Integer>();
    query.setConfig(runtimeConfig);
    query.setCatalogName("xxx");
    query.setSchemaName("yyy");
    query.setFunctionName("aaa");
    query.setResultParameter(
        new ScalarSingleResultParameter<>(() -> new BasicScalar<>(new IntegerWrapper(), false)));
    query.addParameter(new ScalarInParameter<>(() -> new BasicScalar<>(aaa, false), 40));
    query.addParameter(
        new ScalarOutParameter<>(() -> new BasicScalar<>(bbb, false), new Reference<Integer>()));
    query.addParameter(
        new ScalarInOutParameter<>(() -> new BasicScalar<>(ccc, false), new Reference<Integer>()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    var result = new FunctionCommand<Integer>(query).execute();
    query.complete();

    assertNotNull(result);
    assertEquals(Integer.valueOf(10), result);
    assertEquals(Integer.valueOf(40), aaa.get());
    assertEquals(Integer.valueOf(20), bbb.get());
    assertEquals(Integer.valueOf(30), ccc.get());

    var sql = runtimeConfig.dataSource.connection.callableStatement.sql;
    assertEquals("{? = call xxx.yyy.aaa(?, ?, ?)}", sql);
  }
}
