package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.jdbc.SelectForUpdateType;

public class MssqlForUpdateTransformer extends Mssql2008ForUpdateTransformer {

  public MssqlForUpdateTransformer(
      SelectForUpdateType forUpdateType, int waitSeconds, String... aliases) {
    super(forUpdateType, waitSeconds, aliases);
  }
}
