package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;

public class StaticScriptQuery extends AbstractQuery implements ScriptQuery {

  protected String scriptFilePath;

  protected String blockDelimiter;

  protected boolean haltOnError;

  protected URL scriptFileUrl;

  protected SqlLogType sqlLogType;

  protected String sql;

  public void setBlockDelimiter(String blockDelimiter) {
    this.blockDelimiter = blockDelimiter;
  }

  public void setHaltOnError(boolean haltOnError) {
    this.haltOnError = haltOnError;
  }

  public void setSqlLogType(SqlLogType sqlLogType) {
    this.sqlLogType = sqlLogType;
  }

  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(blockDelimiter);

    var sqlAnnotation = method.getAnnotation(org.seasar.doma.Sql.class);
    if (sqlAnnotation == null || sqlAnnotation.useFile()) {
      scriptFilePath =
          ScriptFileUtil.buildPath(method.getDeclaringClass().getName(), method.getName());
      var dbmsSpecificPath =
          ScriptFileUtil.convertToDbmsSpecificPath(scriptFilePath, config.getDialect());
      scriptFileUrl = ResourceUtil.getResource(dbmsSpecificPath);
      if (scriptFileUrl != null) {
        scriptFilePath = dbmsSpecificPath;
      } else {
        scriptFileUrl = ResourceUtil.getResource(scriptFilePath);
        if (scriptFileUrl == null) {
          throw new ScriptFileNotFoundException(scriptFilePath);
        }
      }
    } else {
      sql = sqlAnnotation.value();
    }

    if (blockDelimiter.isEmpty()) {
      blockDelimiter = config.getDialect().getScriptBlockDelimiter();
    }
  }

  @Override
  public void complete() {}

  @Override
  public int getQueryTimeout() {
    return -1;
  }

  @Override
  public Sql<?> getSql() {
    assertUnreachable();
    return null;
  }

  @Override
  public String getSqlFilePath() {
    return scriptFilePath;
  }

  @Override
  public Reader openReader() throws IOException {
    if (scriptFileUrl != null) {
      return new InputStreamReader(scriptFileUrl.openStream(), StandardCharsets.UTF_8);
    }
    return new StringReader(sql);
  }

  @Override
  public String getBlockDelimiter() {
    return blockDelimiter;
  }

  @Override
  public boolean getHaltOnError() {
    return haltOnError;
  }

  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }
}
