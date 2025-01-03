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
package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.tools.FileObject;
import org.junit.jupiter.api.Test;

public class ResourcesTest {

  @Test
  public void testFileObjectImpl_toUri() {
    Path path = Paths.get("aaa", "bbb");
    FileObject fileObject = new Resources.FileObjectImpl(path);
    assertNotNull(fileObject.toUri());
  }

  @Test
  public void testFileObjectImpl_getName() {
    Path path = Paths.get("aaa", "bbb");
    FileObject fileObject = new Resources.FileObjectImpl(path);
    assertNotNull(fileObject.getName());
  }

  @Test
  public void testFileObjectImpl_openInputStream() throws Exception {
    File file = File.createTempFile("aaa", null);
    try {
      FileObject fileObject = new Resources.FileObjectImpl(file.toPath());
      try (InputStream is = fileObject.openInputStream()) {
        //noinspection ResultOfMethodCallIgnored
        is.read();
      }
    } finally {
      //noinspection ResultOfMethodCallIgnored
      file.delete();
    }
  }
}
