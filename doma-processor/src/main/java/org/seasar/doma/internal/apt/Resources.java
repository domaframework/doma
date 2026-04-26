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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public class Resources {

  private final Filer filer;

  private final Reporter reporter;

  private final String[] resourcesDirs;

  private final boolean canAcceptDirectoryPath;

  private final JavaFileManager.Location location;

  private final JavaFileManager.Location fallbackLocation = StandardLocation.CLASS_OUTPUT;

  private final boolean isDebug;

  Resources(
      Filer filer,
      Reporter reporter,
      String resourcesDir,
      List<String> additionalResourcesDirs,
      boolean canAcceptDirectoryPath,
      JavaFileManager.Location location,
      boolean isDebug) {
    assertNotNull(filer, reporter);
    this.filer = filer;
    this.reporter = reporter;
    List<String> dirs = new ArrayList<>();
    if (resourcesDir != null && !resourcesDir.isEmpty()) {
      dirs.add(resourcesDir);
    }
    if (additionalResourcesDirs != null) {
      dirs.addAll(additionalResourcesDirs);
    }
    this.resourcesDirs = dirs.isEmpty() ? null : dirs.toArray(new String[0]);
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
   * <p>When {@code doma.resources.dir} is set, it is searched first. If {@code doma.resources.dirs}
   * is also set, each directory listed there is searched in order as a fallback. The first existing
   * match is returned. If no match is found in any directory, a {@code FileObject} pointing to the
   * first directory is returned so that error messages contain a meaningful path.
   *
   * @param relativePath the relative path to the desired resource; must not be null
   * @return a {@code FileObject} representing the resource located at the given relative path
   * @throws IOException if an I/O error occurs during resource retrieval
   * @throws AssertionError if the {@code relativePath} parameter is null
   */
  public FileObject getResource(String relativePath) throws IOException {
    assertNotNull(relativePath);

    // Prefer the directories specified by the annotation processor option.
    if (resourcesDirs != null) {
      for (String dir : resourcesDirs) {
        Path path = Paths.get(dir, relativePath);
        if (Files.exists(path)) {
          return new FileObjectImpl(path);
        }
      }
      // None found — return the first directory so error reporting shows a meaningful path.
      return new FileObjectImpl(Paths.get(resourcesDirs[0], relativePath));
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
