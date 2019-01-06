package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Formatter;

public class Printer {

  private static final String INDENT_SPACE = "    ";

  private final StringBuilder indentBuffer = new StringBuilder();

  private final Formatter formatter;

  public Printer(Formatter formatter) {
    assertNotNull(formatter);
    this.formatter = formatter;
  }

  public void iprint(String format, Object... args) {
    formatter.format(indentBuffer.toString());
    throwExceptionIfNecessary();
    formatter.format(format, args);
    throwExceptionIfNecessary();
  }

  public void print(String format, Object... args) {
    formatter.format(format, args);
    throwExceptionIfNecessary();
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
