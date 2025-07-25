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
package org.seasar.doma.internal.apt.generator;

import static java.util.stream.Collectors.joining;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Formatter;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.RoundContext;

public class Printer {

  private static final String INDENT_SPACE = "    ";

  private final StringBuilder indentBuffer = new StringBuilder();

  private final RoundContext ctx;

  private final Formatter formatter;

  public Printer(RoundContext ctx, Formatter formatter) {
    assertNotNull(ctx, formatter);
    this.ctx = ctx;
    this.formatter = formatter;
  }

  public void iprint(String format, Object... args) {
    formatter.format(indentBuffer.toString());
    throwExceptionIfNecessary();
    convertArgs(args);
    formatter.format(format, args);
    throwExceptionIfNecessary();
  }

  public void print(String format, Object... args) {
    convertArgs(args);
    formatter.format(format, args);
    throwExceptionIfNecessary();
  }

  private void convertArgs(Object... args) {
    for (int i = 0; i < args.length; i++) {
      args[i] = toCharSequence(args[i]);
    }
  }

  @SuppressWarnings("rawtypes")
  private CharSequence toCharSequence(Object arg) {
    if (arg instanceof CharSequence) {
      return (CharSequence) arg;
    } else if (arg instanceof Class) {
      return ((Class) arg).getName().replace('$', '.');
    } else if (arg instanceof TypeMirror) {
      return ctx.getMoreTypes().getTypeName((TypeMirror) arg);
    } else if (arg instanceof TypeElement) {
      return ((TypeElement) arg).getQualifiedName();
    } else if (arg instanceof Element) {
      return ((Element) arg).getSimpleName();
    } else if (arg instanceof Collection) {
      return ((Collection<?>) arg).stream().map(this::toCharSequence).collect(joining(", "));
    } else if (arg instanceof Code) {
      Formatter f = new Formatter();
      Printer p = new Printer(ctx, f);
      ((Code) arg).print(p);
      return f.toString();
    }
    if (arg != null) {
      return arg.toString();
    }
    return null;
  }

  private void throwExceptionIfNecessary() {
    IOException e = formatter.ioException();
    if (e != null) {
      throw new UncheckedIOException(e);
    }
  }

  public void indent() {
    indentBuffer.append(INDENT_SPACE);
  }

  public void unindent() {
    if (indentBuffer.length() >= INDENT_SPACE.length()) {
      indentBuffer.setLength(indentBuffer.length() - INDENT_SPACE.length());
    }
  }
}
