package org.seasar.doma.internal;

import junit.framework.TestCase;
import org.seasar.doma.DomaException;

/** @author taedium */
public class ArtifactTest extends TestCase {

  public void testGetName() throws Exception {
    assertEquals("Doma", Artifact.getName());
  }

  public void testGetVersion() throws Exception {
    assertNotNull(Artifact.getVersion());
  }

  public void testValidateVersion() throws Exception {
    Artifact.validateVersion(Artifact.getVersion());
  }

  public void testValidateVersion_conflicted() throws Exception {
    try {
      Artifact.validateVersion("hoge");
      fail();
    } catch (DomaException expected) {
      System.out.println(expected.getMessage());
    }
  }
}
