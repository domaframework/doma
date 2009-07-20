/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Formatter;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import org.seasar.doma.internal.ProductInfo;
import org.seasar.doma.internal.util.ClasseUtil;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public abstract class AbstractGenerator implements Generator {

    protected static final String INDENT_SPACE = "    ";

    protected final ProcessingEnvironment env;

    protected final TypeElement typeElement;

    protected final String qualifiedName;

    protected final String packageName;

    protected final String simpleName;

    protected final Formatter formatter;

    protected final StringBuilder indentBuffer = new StringBuilder();

    public AbstractGenerator(ProcessingEnvironment env, TypeElement typeElement)
            throws IOException {
        assertNotNull(env, typeElement);
        this.env = env;
        this.typeElement = typeElement;
        this.qualifiedName = typeElement.getQualifiedName()
                + Options.getSuffix(env);
        this.packageName = ClasseUtil.getPackageName(qualifiedName);
        this.simpleName = ClasseUtil.getSimpleName(qualifiedName);
        Filer filer = env.getFiler();
        JavaFileObject file = filer
                .createSourceFile(qualifiedName, typeElement);
        formatter = new Formatter(new BufferedWriter(file.openWriter()));
    }

    protected void printGenerated() {
        print("@%s(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")%n", Generated.class
                .getName(), ProductInfo.getName(), ProductInfo.getVersion(), Options
                .getDate(env));
    }

    protected void print(String format, Object... args) {
        formatter.format(indentBuffer.toString());
        throwExceptionIfNecessary();
        formatter.format(format, args);
        throwExceptionIfNecessary();
    }

    protected void put(String format, Object... args) {
        formatter.format(format, args);
        throwExceptionIfNecessary();
    }

    protected void throwExceptionIfNecessary() {
        IOException ioException = formatter.ioException();
        if (ioException != null) {
            formatter.close();
            throw new AptException(MessageCode.DOMA4079, env, typeElement,
                    ioException, qualifiedName, ioException);
        }
    }

    protected void indent() {
        indentBuffer.append(INDENT_SPACE);
    }

    protected void unindent() {
        if (indentBuffer.length() >= INDENT_SPACE.length()) {
            indentBuffer.setLength(indentBuffer.length()
                    - INDENT_SPACE.length());
        }
    }

    public void close() {
        if (formatter != null) {
            formatter.close();
        }
    }
}
