package org.seasar.doma.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaException;

public class ArtifactTest {

  @Test
  public void testGetName() {
    assertEquals("Doma", Artifact.getName());
  }

  @Test
  public void testGetVersion() {
    assertNotNull(Artifact.getVersion());
  }

  @Test
  public void testValidateVersion() {
    Artifact.validateVersion(Artifact.getVersion());
  }

  @Test
  public void testValidateVersion_conflicted() {
    try {
      Artifact.validateVersion("hoge");
      fail();
    } catch (DomaException expected) {
      System.out.println(expected.getMessage());
      assertFalse(expected.getMessage().contains("{"));
    }
  }
}
