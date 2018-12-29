package org.seasar.doma.jdbc.dialect;

import junit.framework.TestCase;
import org.seasar.doma.jdbc.PreparedSql;

public class PostgresDialectTest extends TestCase {

  public void testGetIdentitySelectSql_quoteNotRequired_idQuoteNotRequired() throws Exception {
    PostgresDialect dialect = new PostgresDialect();
    PreparedSql sql = dialect.getIdentitySelectSql("aaa", "bbb", "ccc", "DDD", false, false);
    assertEquals(
        "select currval(pg_catalog.pg_get_serial_sequence('aaa.bbb.ccc', 'ddd'))", sql.getRawSql());
  }

  public void testGetIdentitySelectSql_quoteRequired_idQuoteNotRequired() throws Exception {
    PostgresDialect dialect = new PostgresDialect();
    PreparedSql sql = dialect.getIdentitySelectSql("aaa", "bbb", "ccc", "DDD", true, false);
    assertEquals(
        "select currval(pg_catalog.pg_get_serial_sequence('\"aaa\".\"bbb\".\"ccc\"', 'ddd'))",
        sql.getRawSql());
  }

  public void testGetIdentitySelectSql_quoteNotRequired_idQuoteRequired() throws Exception {
    PostgresDialect dialect = new PostgresDialect();
    PreparedSql sql = dialect.getIdentitySelectSql("aaa", "bbb", "ccc", "DDD", false, true);
    assertEquals(
        "select currval(pg_catalog.pg_get_serial_sequence('aaa.bbb.ccc', 'DDD'))", sql.getRawSql());
  }

  public void testGetIdentitySelectSql_quoteRequired_idQuoteRequired() throws Exception {
    PostgresDialect dialect = new PostgresDialect();
    PreparedSql sql = dialect.getIdentitySelectSql("aaa", "bbb", "ccc", "DDD", true, true);
    assertEquals(
        "select currval(pg_catalog.pg_get_serial_sequence('\"aaa\".\"bbb\".\"ccc\"', 'DDD'))",
        sql.getRawSql());
  }
}
