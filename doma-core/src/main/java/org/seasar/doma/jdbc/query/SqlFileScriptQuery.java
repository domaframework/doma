/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.function.Supplier;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.jdbc.ScriptFileLoader;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * A query that executes a SQL script file.
 *
 * <p>This class extends {@link AbstractQuery} to provide functionality for executing SQL script
 * files. It handles script file loading, block delimiter detection, and script execution.
 */
public class SqlFileScriptQuery extends AbstractQuery implements ScriptQuery {

  /** The path to the script file. */
  protected String scriptFilePath;

  /** The delimiter that separates SQL statements in the script. */
  protected String blockDelimiter;

  /** Whether to halt execution when an error occurs. */
  protected boolean haltOnError;

  /** The URL of the script file. */
  protected URL scriptFileUrl;

  /** The SQL annotation if the script is defined inline. */
  protected org.seasar.doma.Sql sqlAnnotation;

  /** The SQL log type for this script query. */
  protected SqlLogType sqlLogType;

  /**
   * Sets the path to the script file.
   *
   * @param scriptFilePath the script file path
   */
  public void setScriptFilePath(String scriptFilePath) {
    this.scriptFilePath = scriptFilePath;
  }

  /**
   * Sets the delimiter that separates SQL statements in the script.
   *
   * @param blockDelimiter the block delimiter
   */
  public void setBlockDelimiter(String blockDelimiter) {
    this.blockDelimiter = blockDelimiter;
  }

  /**
   * Sets whether to halt execution when an error occurs.
   *
   * @param haltOnError whether to halt on error
   */
  public void setHaltOnError(boolean haltOnError) {
    this.haltOnError = haltOnError;
  }

  /**
   * Sets the SQL log type for this script query.
   *
   * @param sqlLogType the SQL log type
   */
  public void setSqlLogType(SqlLogType sqlLogType) {
    this.sqlLogType = sqlLogType;
  }

  /** {@inheritDoc} */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(scriptFilePath, blockDelimiter);
    sqlAnnotation = method.getAnnotation(org.seasar.doma.Sql.class);
    if (sqlAnnotation == null) {
      assertTrue(scriptFilePath.startsWith(Constants.SCRIPT_PATH_PREFIX));
      assertTrue(scriptFilePath.endsWith(Constants.SCRIPT_PATH_SUFFIX));
      ScriptFileLoader loader = config.getScriptFileLoader();

      String dbmsSpecificPath =
          ScriptFileUtil.convertToDbmsSpecificPath(scriptFilePath, config.getDialect());
      scriptFileUrl = loader.loadAsURL(dbmsSpecificPath);
      if (scriptFileUrl != null) {
        scriptFilePath = dbmsSpecificPath;
      } else {
        scriptFileUrl = loader.loadAsURL(scriptFilePath);
        if (scriptFileUrl == null) {
          throw new ScriptFileNotFoundException(scriptFilePath);
        }
      }
    }
    if (blockDelimiter.isEmpty()) {
      blockDelimiter = config.getDialect().getScriptBlockDelimiter();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void complete() {}

  /** {@inheritDoc} */
  @Override
  public int getQueryTimeout() {
    return -1;
  }

  /** {@inheritDoc} */
  @Override
  public Sql<?> getSql() {
    assertUnreachable();
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public String getScriptFilePath() {
    return scriptFilePath;
  }

  /** {@inheritDoc} */
  @Override
  public URL getScriptFileUrl() {
    return scriptFileUrl;
  }

  /**
   * Returns a supplier that provides a reader for the script content.
   *
   * @return the reader supplier
   */
  public Supplier<Reader> getReaderSupplier() {
    if (sqlAnnotation == null) {
      return () -> {
        try {
          InputStream inputStream = scriptFileUrl.openStream();
          return new InputStreamReader(inputStream, Constants.UTF_8);
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      };
    }
    return () -> new StringReader(sqlAnnotation.value());
  }

  /** {@inheritDoc} */
  @Override
  public String getBlockDelimiter() {
    return blockDelimiter;
  }

  /** {@inheritDoc} */
  @Override
  public boolean getHaltOnError() {
    return haltOnError;
  }

  /**
   * Returns the SQL log type for this script query.
   *
   * @return the SQL log type
   */
  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }
}
