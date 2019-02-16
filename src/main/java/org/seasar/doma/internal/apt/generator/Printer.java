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
import org.seasar.doma.internal.apt.Context;

public class Printer {

  private static final String INDENT_SPACE = "    ";

  private final StringBuilder indentBuffer = new StringBuilder();

  private final Context ctx;

  private final Formatter formatter;

  public Printer(Context ctx, Formatter formatter) {
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

  private CharSequence toCharSequence(Object arg) {
    if (arg instanceof CharSequence) {
      return (CharSequence) arg;
    } else if (arg instanceof Class) {
      return ((Class) arg).getName();
    } else if (arg instanceof TypeMirror) {
      return ctx.getTypes().getTypeName((TypeMirror) arg);
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
