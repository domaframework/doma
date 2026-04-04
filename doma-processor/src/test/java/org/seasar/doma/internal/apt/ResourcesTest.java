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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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

  @Test
  public void testGetResource_multipleDirectories(@TempDir Path dir1, @TempDir Path dir2)
      throws IOException {
    // Place a file only in the second directory (simulating doma.resources.dir.test)
    Path subDir = dir2.resolve("META-INF").resolve("test");
    Files.createDirectories(subDir);
    Path sqlFile = subDir.resolve("query.sql");
    Files.writeString(sqlFile, "select 1");

    // Configure two separate resource directories via doma.resources.dir and
    // doma.resources.dir.test
    Resources resources =
        new Resources(
            new NoopFiler(),
            new Reporter(new NoopMessager()),
            dir1.toAbsolutePath().toString(),
            dir2.toAbsolutePath().toString(),
            true,
            StandardLocation.CLASS_OUTPUT,
            false);

    FileObject result = resources.getResource("META-INF/test/query.sql");
    assertTrue(Files.exists(Path.of(result.toUri())), "Should find file in test directory");
  }

  @Test
  public void testGetResource_firstDirectoryWins(@TempDir Path dir1, @TempDir Path dir2)
      throws IOException {
    // Place a file in both directories
    for (Path dir : new Path[] {dir1, dir2}) {
      Path subDir = dir.resolve("META-INF");
      Files.createDirectories(subDir);
      Files.writeString(subDir.resolve("test.sql"), "select from " + dir.getFileName());
    }

    Resources resources =
        new Resources(
            new NoopFiler(),
            new Reporter(new NoopMessager()),
            dir1.toAbsolutePath().toString(),
            dir2.toAbsolutePath().toString(),
            true,
            StandardLocation.CLASS_OUTPUT,
            false);

    FileObject result = resources.getResource("META-INF/test.sql");
    // Should resolve to dir1 (first match wins)
    assertTrue(
        result.toUri().getPath().contains(dir1.getFileName().toString()),
        "Should return file from first matching directory");
  }

  /** Minimal Messager implementation for unit tests. */
  private static class NoopMessager implements javax.annotation.processing.Messager {

    @Override
    public void printMessage(javax.tools.Diagnostic.Kind kind, CharSequence msg) {}

    @Override
    public void printMessage(
        javax.tools.Diagnostic.Kind kind, CharSequence msg, javax.lang.model.element.Element e) {}

    @Override
    public void printMessage(
        javax.tools.Diagnostic.Kind kind,
        CharSequence msg,
        javax.lang.model.element.Element e,
        javax.lang.model.element.AnnotationMirror a) {}

    @Override
    public void printMessage(
        javax.tools.Diagnostic.Kind kind,
        CharSequence msg,
        javax.lang.model.element.Element e,
        javax.lang.model.element.AnnotationMirror a,
        javax.lang.model.element.AnnotationValue v) {}
  }

  /** Minimal Filer implementation for unit tests that don't need Filer functionality. */
  private static class NoopFiler implements javax.annotation.processing.Filer {

    @Override
    public javax.tools.JavaFileObject createSourceFile(
        CharSequence n, javax.lang.model.element.Element... e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public javax.tools.JavaFileObject createClassFile(
        CharSequence n, javax.lang.model.element.Element... e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public javax.tools.FileObject createResource(
        javax.tools.JavaFileManager.Location l,
        CharSequence m,
        CharSequence r,
        javax.lang.model.element.Element... e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public javax.tools.FileObject getResource(
        javax.tools.JavaFileManager.Location l, CharSequence m, CharSequence r) {
      throw new UnsupportedOperationException();
    }
  }
}
