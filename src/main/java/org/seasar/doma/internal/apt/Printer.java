package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Formatter;

/** @author taedium */
public class Printer {

  protected static final String INDENT_SPACE = "    ";

  protected StringBuilder indent = new StringBuilder();

  protected final Formatter formatter;

  public Printer(Appendable appendable) {
    assertNotNull(appendable);
    formatter = new Formatter(appendable);
  }

  public void p(String format, Object... args) {
    formatter.format(indent + format, args);
  }

  public void pp(String format, Object... args) {
    formatter.format(format, args);
  }

  public void indent() {
    indent.append(INDENT_SPACE);
  }

  public void unindent() {
    if (indent.length() >= INDENT_SPACE.length()) {
      indent.setLength(indent.length() - INDENT_SPACE.length());
    }
  }

  public void close() {
    formatter.close();
  }
}
