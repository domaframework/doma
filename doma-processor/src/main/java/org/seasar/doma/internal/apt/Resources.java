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
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public class Resources {

  private final Filer filer;

  private final Reporter reporter;

  private final String resourcesDir;

  private final boolean canAcceptDirectoryPath;

  private final JavaFileManager.Location location;

  private final JavaFileManager.Location fallbackLocation = StandardLocation.CLASS_OUTPUT;

  private final boolean isDebug;

  Resources(
      Filer filer,
      Reporter reporter,
      String resourcesDir,
      boolean canAcceptDirectoryPath,
      JavaFileManager.Location location,
      boolean isDebug) {
    assertNotNull(filer, reporter);
    this.filer = filer;
    this.reporter = reporter;
    this.resourcesDir = resourcesDir;
    this.canAcceptDirectoryPath = canAcceptDirectoryPath;
    this.location = location;
    this.isDebug = isDebug;
  }

  public JavaFileObject createSourceFile(CharSequence name, Element... originatingElements)
      throws IOException {
    return filer.createSourceFile(name, originatingElements);
  }

  /**
   * Indicates whether directory paths can be accepted in the {@code #getResource} method.
   *
   * @return {@code true} if directory paths can be accepted; {@code false} otherwise
   */
  public boolean canAcceptDirectoryPath() {
    return canAcceptDirectoryPath;
  }

  /**
   * Attempts to retrieve a resource file based on the specified relative path.
   *
   * @param relativePath the relative path to the desired resource; must not be null
   * @return a {@code FileObject} representing the resource located at the given relative path
   * @throws IOException if an I/O error occurs during resource retrieval
   * @throws AssertionError if the {@code relativePath} parameter is null
   */
  public FileObject getResource(String relativePath) throws IOException {
    assertNotNull(relativePath);

    // Prefer the directory specified by the annotation processor option.
    if (resourcesDir != null) {
      Path path = Paths.get(resourcesDir, relativePath);
      return new FileObjectImpl(path);
    }

    try {
      return filer.getResource(location, "", relativePath);
    } catch (Exception e) {
      if (location != fallbackLocation) {
        if (isDebug) {
          var message =
              String.format(
                  "Fall back from %s to %s: %s, exception: %s",
                  location, fallbackLocation, relativePath, e);
          reporter.debug(message);
        }
        // If a file cannot be found in the default location, use the fallbackLocation.
        return filer.getResource(fallbackLocation, "", relativePath);
      }
      throw e;
    }
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
