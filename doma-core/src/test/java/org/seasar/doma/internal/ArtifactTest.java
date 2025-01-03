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
