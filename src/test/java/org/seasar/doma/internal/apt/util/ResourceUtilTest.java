/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.tools.FileObject;
import junit.framework.TestCase;

/** @author nakamura */
public class ResourceUtilTest extends TestCase {

  public void testFileObjectImpl_toUri() throws Exception {
    Path path = Paths.get("aaa", "bbb");
    FileObject fileObject = new ResourceUtil.FileObjectImpl(path);
    assertNotNull(fileObject.toUri());
  }

  public void testFileObjectImpl_getName() throws Exception {
    Path path = Paths.get("aaa", "bbb");
    FileObject fileObject = new ResourceUtil.FileObjectImpl(path);
    assertNotNull(fileObject.getName());
  }

  public void testFileObjectImpl_openInputStream() throws Exception {
    File file = File.createTempFile("aaa", null);
    try {
      FileObject fileObject = new ResourceUtil.FileObjectImpl(file.toPath());
      try (InputStream is = fileObject.openInputStream()) {
        is.read();
      }
    } finally {
      file.delete();
    }
  }
}
