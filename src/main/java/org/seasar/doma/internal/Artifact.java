package org.seasar.doma.internal;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.Message;

public final class Artifact {

  private static final String NAME = "Doma";

  private static final String VERSION = "2.21.1-SNAPSHOT";

  public static String getName() {
    return NAME;
  }

  public static String getVersion() {
    return VERSION;
  }

  public static void validateVersion(String generationtimeVersion) {
    if (!VERSION.equals(generationtimeVersion)) {
      throw new DomaException(Message.DOMA0003, VERSION, generationtimeVersion);
    }
  }
}
