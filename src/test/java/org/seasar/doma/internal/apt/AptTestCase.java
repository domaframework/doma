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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Consumer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.seasar.aptina.unit.AptinaTestCase;
import org.seasar.doma.Dao;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.ExternalHolder;
import org.seasar.doma.Holder;
import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.message.Message;

import junit.framework.AssertionFailedError;

/**
 * @author taedium
 * 
 */
public abstract class AptTestCase extends AptinaTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addSourcePath("src/test/java");
        addSourcePath("src/test/resources");
        setCharset("UTF-8");
        setLocale(Locale.JAPAN);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+9"));
    }

    @Override
    protected void tearDown() throws Exception {
        TimeZone.setDefault(null);
        super.tearDown();
    }

    protected String getExpectedContent() throws Exception {
        String path = getClass().getName().replace(".", "/");
        String suffix = "_" + getName().substring("test".length()) + ".txt";
        return ResourceUtil.getResourceAsString(path + suffix);
    }

    protected void assertGeneratedSource(Class<?> originalClass)
            throws Exception {
        String generatedClassName = getGeneratedClassName(originalClass);
        try {
            assertEqualsGeneratedSource(getExpectedContent(),
                    generatedClassName);
        } catch (AssertionFailedError error) {
            System.out.println(getGeneratedSource(generatedClassName));
            throw error;
        }
    }

    protected String getGeneratedClassName(Class<?> originalClass) {
        if (originalClass.isAnnotationPresent(Dao.class)) {
            return originalClass.getName()
                    + Options.Constants.DEFAULT_DAO_SUFFIX;
        }
        if (originalClass.isAnnotationPresent(Entity.class)
                || originalClass.isAnnotationPresent(Embeddable.class)
                || originalClass.isAnnotationPresent(Holder.class)
                || originalClass.isAnnotationPresent(ExternalHolder.class)) {
            return Conventions.toFullMetaName(originalClass.getName());
        }
        throw new AssertionFailedError("annotation not found.");
    }

    protected void assertMessage(Message message) {
        List<Diagnostic<? extends JavaFileObject>> diagnostics = getDiagnostics();
        if (diagnostics.size() == 1) {
            Message m = extractMessage(diagnostics.get(0));
            if (m == null) {
                fail();
            }
            if (message == m) {
                return;
            }
            fail("actual message id: " + m.name());
        }
        fail();
    }

    protected void assertNoMessage() {
        List<Diagnostic<? extends JavaFileObject>> diagnostics = getDiagnostics();
        if (!diagnostics.isEmpty()) {
            fail();
        }
    }

    @Override
    protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
        List<Diagnostic<? extends JavaFileObject>> results = new ArrayList<Diagnostic<? extends JavaFileObject>>();
        for (Diagnostic<? extends JavaFileObject> diagnostic : super.getDiagnostics()) {
            switch (diagnostic.getKind()) {
            case ERROR:
            case WARNING:
            case MANDATORY_WARNING:
                results.add(diagnostic);
                break;
            default:
                // do nothing
            }
        }
        return results;
    }

    protected Message getMessageCode() {
        for (Diagnostic<? extends JavaFileObject> diagnostic : getDiagnostics()) {
            return extractMessage(diagnostic);
        }
        return null;
    }

    protected Message extractMessage(
            Diagnostic<? extends JavaFileObject> diagnostic) {
        String message = diagnostic.getMessage(getLocale());
        int start = message.indexOf('[');
        int end = message.indexOf(']');
        if (start > -1 && end > -1) {
            String code = message.substring(start + 1, end);
            if (code.startsWith("DOMA")) {
                return Enum.valueOf(Message.class, code);
            }
        }
        return null;
    }

    @SupportedAnnotationTypes("*")
    static protected class AptProcessor extends AbstractProcessor {

        private final Consumer<Context> consumer;

        private Context ctx;

        public AptProcessor(Consumer<Context> consumer) {
            this.consumer = consumer;
        }

        @Override
        public synchronized void init(ProcessingEnvironment env) {
            super.init(env);
            this.ctx = new Context(env);
        }

        @Override
        public SourceVersion getSupportedSourceVersion() {
            return SourceVersion.latest();
        }

        @Override
        public boolean process(final Set<? extends TypeElement> annotations,
                final RoundEnvironment roundEnv) {
            if (roundEnv.processingOver()) {
                return true;
            }
            this.consumer.accept(this.ctx);
            return true;
        }
    }

}
