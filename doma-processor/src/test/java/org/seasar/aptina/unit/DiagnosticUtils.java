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

import static org.seasar.aptina.unit.AssertionUtils.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * @author koichik
 */
class DiagnosticUtils {

  private DiagnosticUtils() {}

  public static List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
      final List<Diagnostic<? extends JavaFileObject>> diagnostics, final Class<?> clazz) {
    assertNotNull("clazz", clazz);
    return getDiagnostics(diagnostics, clazz.getName());
  }

  public static List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
      final List<Diagnostic<? extends JavaFileObject>> diagnostics, final String className) {
    assertNotNull("className", className);
    final String name = className.replace('.', '/') + ".java";
    final List<Diagnostic<? extends JavaFileObject>> result = new ArrayList<>();
    for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
      final JavaFileObject source = diagnostic.getSource();
      if (source != null && source.toUri().toString().endsWith(name)) {
        result.add(diagnostic);
      }
    }
    return result;
  }

  public static List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
      final List<Diagnostic<? extends JavaFileObject>> diagnostics,
      final javax.tools.Diagnostic.Kind kind) {
    assertNotNull("kind", kind);
    final List<Diagnostic<? extends JavaFileObject>> result = new ArrayList<>();
    for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
      if (diagnostic.getKind().equals(kind)) {
        result.add(diagnostic);
      }
    }
    return result;
  }

  public static List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
      final List<Diagnostic<? extends JavaFileObject>> diagnostics,
      final Class<?> clazz,
      final javax.tools.Diagnostic.Kind kind) {
    assertNotNull("clazz", clazz);
    assertNotNull("kind", kind);
    return getDiagnostics(diagnostics, clazz.getName(), kind);
  }

  public static List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
      final List<Diagnostic<? extends JavaFileObject>> diagnostics,
      final String className,
      final javax.tools.Diagnostic.Kind kind) {
    assertNotNull("className", className);
    assertNotNull("kind", kind);
    final String name = className.replace('.', '/') + ".java";
    final List<Diagnostic<? extends JavaFileObject>> result = new ArrayList<>();
    for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
      final JavaFileObject source = diagnostic.getSource();
      if (source != null
          && source.toUri().toString().endsWith(name)
          && diagnostic.getKind().equals(kind)) {
        result.add(diagnostic);
      }
    }
    return result;
  }
}
