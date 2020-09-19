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
package org.seasar.aptina.unit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

/** @author koichik */
class TestingJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

  final Map<String, InMemoryJavaFileObject> fileObjects = new HashMap<>();

  final Charset charset;

  public TestingJavaFileManager(final StandardJavaFileManager fileManager, final Charset charset) {
    super(fileManager);
    this.charset = charset;
  }

  @Override
  public FileObject getFileForInput(
      final Location location, final String packageName, final String relativeName)
      throws IOException {
    if (relativeName.endsWith(".java")) {
      return getJavaFileForInput(location, packageName + "." + relativeName, Kind.SOURCE);
    }
    if (relativeName.endsWith(".class")) {
      return getJavaFileForInput(location, packageName + "." + relativeName, Kind.CLASS);
    }
    final String key = createKey(packageName, relativeName);
    if (fileObjects.containsKey(key)) {
      return fileObjects.get(key);
    }
    return super.getFileForInput(location, packageName, relativeName);
  }

  @Override
  public FileObject getFileForOutput(
      final Location location,
      final String packageName,
      final String relativeName,
      final FileObject sibling)
      throws IOException {
    if (relativeName.endsWith(".java")) {
      return getJavaFileForOutput(location, packageName + "." + relativeName, Kind.SOURCE, sibling);
    }
    if (relativeName.endsWith(".class")) {
      return getJavaFileForOutput(location, packageName + "." + relativeName, Kind.CLASS, sibling);
    }
    final String key = createKey(packageName, relativeName);
    if (fileObjects.containsKey(key)) {
      return fileObjects.get(key);
    }

    byte[] content = null;
    URI uri = null;
    try {
      FileObject originalFileObject = null;
      if (location == StandardLocation.CLASS_OUTPUT) {
        // Because resources are not copied to the CLASS_OUTPUT in the Aptina Unit environment, we
        // read
        // them from SOURCE_PATH first
        originalFileObject =
            super.getFileForInput(StandardLocation.SOURCE_PATH, packageName, relativeName);
      }
      if (originalFileObject == null) {
        originalFileObject = super.getFileForOutput(location, packageName, relativeName, sibling);
      }
      uri = originalFileObject.toUri();
      content = IOUtils.readBytes(originalFileObject.openInputStream());
    } catch (final FileNotFoundException ignore) {
    }
    final InMemoryJavaFileObject fileObject =
        new InMemoryJavaFileObject(
            uri != null ? uri : toURI(location, packageName, relativeName),
            Kind.OTHER,
            charset,
            content);
    fileObjects.put(key, fileObject);
    return fileObject;
  }

  @Override
  public JavaFileObject getJavaFileForInput(
      final Location location, final String className, final Kind kind) throws IOException {
    final String key = createKey(className, kind);
    if (fileObjects.containsKey(key)) {
      return fileObjects.get(key);
    }
    return super.getJavaFileForInput(location, className, kind);
  }

  @Override
  public JavaFileObject getJavaFileForOutput(
      final Location location, final String className, final Kind kind, final FileObject sibling)
      throws IOException {
    final String key = createKey(className, kind);
    if (fileObjects.containsKey(key)) {
      return fileObjects.get(key);
    }

    byte[] content = null;
    URI uri = null;
    try {
      final JavaFileObject originalFileObject =
          super.getJavaFileForOutput(location, className, kind, sibling);
      uri = originalFileObject.toUri();
      content = IOUtils.readBytes(originalFileObject.openInputStream());
    } catch (final FileNotFoundException ignore) {
    }
    final InMemoryJavaFileObject fileObject =
        new InMemoryJavaFileObject(
            uri != null ? uri : toURI(location, className), kind, charset, content);
    fileObjects.put(key, fileObject);
    return fileObject;
  }

  @Override
  public boolean isSameFile(final FileObject lhs, final FileObject rhs) {
    if (lhs instanceof InMemoryJavaFileObject) {
      if (rhs instanceof InMemoryJavaFileObject) {
        return lhs == rhs;
      }
      return false;
    }
    return super.isSameFile(lhs, rhs);
  }

  public JavaFileObject getGeneratedJavaFile(
      final Location location, final String className, final Kind kind) {
    final String key = kind.name() + "::" + className;
    if (fileObjects.containsKey(key)) {
      return fileObjects.get(key);
    }
    return null;
  }

  URI toURI(final Location location, final String packageName, final String relativeName) {
    try {
      return new URI(location.getName() + "/" + packageName.replace('.', '/') + "/" + relativeName);
    } catch (final URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  URI toURI(final Location location, final String className) {
    try {
      return new URI(location.getName() + "/" + className.replace('.', '/') + ".java");
    } catch (final URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  String createKey(final String packageName, final String relativeName) {
    if (packageName == null || packageName.isEmpty()) {
      return relativeName;
    }
    return packageName.replace('.', '/') + "/" + relativeName;
  }

  String createKey(final String className, final Kind kind) {
    return className.replace('.', '/') + kind.extension;
  }
}
