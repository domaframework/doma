package org.seasar.doma.jdbc.query;

import java.net.URL;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * @since 1.7.0
 */
public interface ScriptQuery extends Query {

  URL getScriptFileUrl();

  String getScriptFilePath();

  String getBlockDelimiter();

  boolean getHaltOnError();

  SqlLogType getSqlLogType();
}
