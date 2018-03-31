package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.net.URL;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlFileScriptQuery extends AbstractQuery implements ScriptQuery {

  protected String scriptFilePath;

  protected String blockDelimiter;

  protected boolean haltOnError;

  protected URL scriptFileUrl;

  protected SqlLogType sqlLogType;

  public void setScriptFilePath(String scriptFilePath) {
    this.scriptFilePath = scriptFilePath;
  }

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
    assertNotNull(scriptFilePath, blockDelimiter);
    assertTrue(scriptFilePath.startsWith(Constants.SCRIPT_PATH_PREFIX));
    assertTrue(scriptFilePath.endsWith(Constants.SCRIPT_PATH_SUFFIX));

    String dbmsSpecificPath =
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
  public String getScriptFilePath() {
    return scriptFilePath;
  }

  @Override
  public URL getScriptFileUrl() {
    return scriptFileUrl;
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
