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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Formatter;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import org.seasar.doma.internal.Artifact;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.message.Message;

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

    protected final String fullpackage;

    protected final String subpackage;

    protected final String prefix;

    protected final String suffix;

    protected final Formatter formatter;

    protected final StringBuilder indentBuffer = new StringBuilder();

    protected AbstractGenerator(ProcessingEnvironment env,
            TypeElement typeElement, String fullpackage, String subpackage,
            String prefix, String suffix) throws IOException {
        assertNotNull(env, typeElement, prefix, suffix);
        this.env = env;
        this.typeElement = typeElement;
        this.fullpackage = fullpackage;
        this.subpackage = subpackage;
        this.prefix = prefix;
        this.suffix = suffix;
        this.qualifiedName = createQualifiedName(env, typeElement, fullpackage,
                subpackage, prefix, suffix);
        this.packageName = ClassUtil.getPackageName(qualifiedName);
        this.simpleName = ClassUtil.getSimpleName(qualifiedName);
        Filer filer = env.getFiler();
        JavaFileObject file = filer
                .createSourceFile(qualifiedName, typeElement);
        formatter = new Formatter(new BufferedWriter(file.openWriter()));
    }

    protected String createQualifiedName(ProcessingEnvironment env,
            TypeElement typeElement, String fullpackage, String subpackage,
            String prefix, String suffix) {
        String qualifiedNamePrefix = getQualifiedNamePrefix(env, typeElement,
                fullpackage, subpackage);
        return qualifiedNamePrefix + prefix
                + ElementUtil.getPackageExcludedBinaryName(typeElement, env)
                + suffix;
    }

    protected String getQualifiedNamePrefix(ProcessingEnvironment env,
            TypeElement typeElement, String fullpackage, String subpackage) {
        if (fullpackage != null) {
            return fullpackage + ".";
        }
        String packageName = ElementUtil.getPackageName(typeElement, env);
        String base = packageName + ".";
        if (subpackage != null) {
            return base + subpackage + ".";
        }
        return base;
    }

    protected void printGenerated() {
        iprint("@%s(value = { \"%s\", \"%s\" }, date = \"%tFT%<tT.%<tL%<tz\")%n",
                Generated.class.getName(), Artifact.getName(),
                Options.getVersion(env), Options.getDate(env));
    }

    protected void printValidateVersionStaticInitializer() {
        if (Options.getVersionValidation(env)) {
            iprint("static {%n");
            iprint("    %1$s.validateVersion(\"%2$s\");%n",
                    Artifact.class.getName(), Options.getVersion(env));
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
            throw new AptException(Message.DOMA4079, env, typeElement,
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

    @Override
    public void close() {
        if (formatter != null) {
            formatter.close();
        }
    }

}
