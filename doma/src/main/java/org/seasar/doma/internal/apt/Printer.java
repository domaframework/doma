package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.Assertions.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Formatter;

/**
 * @author taedium
 * 
 */
public class Printer {

    protected static final String INDENT_SPACE = "    ";

    protected final Formatter formatter;

    protected StringBuilder indent = new StringBuilder();

    public Printer(Writer writer) throws IOException {
        assertNotNull(writer);
        formatter = new Formatter(new BufferedWriter(writer));
    }

    public void p(String format, Object... args) {
        if (format.equals("%n")) {
            formatter.format(format, args);
        } else {
            formatter.format(indent + format, args);
        }
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
