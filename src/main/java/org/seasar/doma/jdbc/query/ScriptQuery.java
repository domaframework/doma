package org.seasar.doma.jdbc.query;

import java.io.IOException;
import java.io.Reader;
import org.seasar.doma.jdbc.SqlLogType;

/** An object used for building an SQL script. */
public interface ScriptQuery extends Query {

  Reader openReader() throws IOException;

  String getSqlFilePath();

  String getBlockDelimiter();

  boolean getHaltOnError();

  SqlLogType getSqlLogType();
}
