package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.jdbc.SelectForUpdateType;

/** @author taedium */
public class H2ForUpdateTransformer extends H212126ForUpdateTransformer {

  public H2ForUpdateTransformer(
      SelectForUpdateType forUpdateType, int waitSeconds, String... aliases) {
    super(forUpdateType, waitSeconds, aliases);
  }
}
