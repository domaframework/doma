package org.seasar.doma.jdbc.query;

import java.io.Reader;
import java.net.URL;
import java.util.function.Supplier;
import org.seasar.doma.jdbc.SqlLogType;

public interface ScriptQuery extends Query {

  URL getScriptFileUrl();

  Supplier<Reader> getReaderSupplier();

  String getScriptFilePath();

  String getBlockDelimiter();

  boolean getHaltOnError();

  SqlLogType getSqlLogType();
}
