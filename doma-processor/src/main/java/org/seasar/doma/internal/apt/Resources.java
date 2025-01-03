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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public class Resources {

  private final Filer filer;

  private final String resourcesDir;

  Resources(Filer filer, String resourcesDir) {
    assertNotNull(filer);
    this.filer = filer;
    this.resourcesDir = resourcesDir;
  }

  public JavaFileObject createSourceFile(CharSequence name, Element... originatingElements)
      throws IOException {
    return filer.createSourceFile(name, originatingElements);
  }

  public FileObject getResource(String relativePath) throws IOException {
    assertNotNull(relativePath);
    if (resourcesDir != null) {
      Path path = Paths.get(resourcesDir, relativePath);
      return new FileObjectImpl(path);
    }
    return filer.getResource(StandardLocation.CLASS_OUTPUT, "", relativePath);
  }

  protected static class FileObjectImpl implements FileObject {

    private final Path path;

    FileObjectImpl(Path path) {
      this.path = path;
    }

    @Override
    public URI toUri() {
      return path.toUri();
    }

    @Override
    public String getName() {
      return path.toString();
    }

    @Override
    public InputStream openInputStream() throws IOException {
      return Files.newInputStream(path);
    }

    @Override
    public OutputStream openOutputStream() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Writer openWriter() {
      throw new UnsupportedOperationException();
    }

    @Override
    public long getLastModified() {
      return 0L;
    }

    @Override
    public boolean delete() {
      return false;
    }
  }
}
