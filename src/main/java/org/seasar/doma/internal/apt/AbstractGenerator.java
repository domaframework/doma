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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Formatter;

import javax.annotation.processing.Generated;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import org.seasar.doma.internal.Artifact;
import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public abstract class AbstractGenerator implements Generator {

    protected static final String INDENT_SPACE = "    ";

    protected final Context ctx;

    protected final TypeElement typeElement;

    protected final String canonicalName;

    protected final String packageName;

    protected final String simpleName;

    protected final String fullpackage;

    protected final String subpackage;

    protected final String prefix;

    protected final String suffix;

    protected final Formatter formatter;

    protected final StringBuilder indentBuffer = new StringBuilder();

    protected AbstractGenerator(Context ctx,
            TypeElement typeElement, String fullpackage, String subpackage,
            String prefix, String suffix) throws IOException {
        assertNotNull(ctx, typeElement, prefix, suffix);
        this.ctx = ctx;
        this.typeElement = typeElement;
        this.fullpackage = fullpackage;
        this.subpackage = subpackage;
        this.prefix = prefix;
        this.suffix = suffix;
        this.canonicalName = createCanonicalName(typeElement, fullpackage,
                subpackage, prefix, suffix);
        this.packageName = ClassUtil.getPackageName(canonicalName);
        this.simpleName = ClassUtil.getSimpleName(canonicalName);
        JavaFileObject file = ctx.getResources()
                .createSourceFile(canonicalName, typeElement);
        formatter = new Formatter(new BufferedWriter(file.openWriter()));
    }

    protected String createCanonicalName(TypeElement typeElement,
            String fullpackage, String subpackage,
            String prefix, String suffix) {
        String qualifiedNamePrefix = getQualifiedNamePrefix(typeElement,
                fullpackage, subpackage);
        String binaryName = Conventions.normalizeBinaryName(
                ctx.getElements().getBinaryName(typeElement).toString());
        String infix = ClassUtil.getSimpleName(binaryName);
        return qualifiedNamePrefix + prefix + infix + suffix;
    }

    protected String getQualifiedNamePrefix(TypeElement typeElement,
            String fullpackage, String subpackage) {
        if (fullpackage != null) {
            return fullpackage + ".";
        }
        String packageName = ctx.getElements().getPackageName(typeElement);
        String base = "";
        if (packageName != null && packageName.length() > 0) {
            base = packageName + ".";
        }
        if (subpackage != null) {
            return base + subpackage + ".";
        }
        return base;
    }

    protected void printGenerated() {
        iprint("@%s(value = { \"%s\", \"%s\" }, date = \"%tFT%<tT.%<tL%<tz\")%n",
                Generated.class.getName(),
                Artifact.getName(),
                ctx.getOptions().getVersion(), ctx.getOptions().getDate());
    }

    protected void printValidateVersionStaticInitializer() {
        if (ctx.getOptions().getVersionValidation()) {
            iprint("static {%n");
            iprint("    %1$s.validateVersion(\"%2$s\");%n",
                    Artifact.class.getName(), ctx.getOptions().getVersion());
            iprint("}%n");
            print("%n");
        }
    }

    protected void iprint(String format, Object... args) {
        formatter.format(indentBuffer.toString());
        throwExceptionIfNecessary();
        formatter.format(format, args);
        throwExceptionIfNecessary();
    }

    protected void print(String format, Object... args) {
        formatter.format(format, args);
        throwExceptionIfNecessary();
    }

    protected void throwExceptionIfNecessary() {
        IOException ioException = formatter.ioException();
        if (ioException != null) {
            formatter.close();
            throw new AptException(Message.DOMA4079, typeElement, ioException,
                    new Object[] { canonicalName, ioException });
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

    @Override
    public void close() {
        if (formatter != null) {
            formatter.close();
        }
    }

}
