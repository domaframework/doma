package org.seasar.doma.internal;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.Message;

/** @author taedium */
public final class Artifact {

  private static final String NAME = "Doma";

  private static final String VERSION = "3.0-beta-1-SNAPSHOT";

  public static String getName() {
    return NAME;
  }

  public static String getVersion() {
    return VERSION;
  }

  public static void validateVersion(String generationTimeVersion) {
    if (!VERSION.equals(generationTimeVersion)) {
      throw new DomaException(Message.DOMA0003, VERSION, generationTimeVersion);
    }
  }
}
