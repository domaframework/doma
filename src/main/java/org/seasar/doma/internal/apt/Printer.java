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
package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Formatter;

/**
 * @author taedium
 * 
 */
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
