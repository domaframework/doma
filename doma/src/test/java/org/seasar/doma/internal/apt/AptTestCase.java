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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import junit.framework.AssertionFailedError;

import org.seasar.aptina.unit.AptinaTestCase;
import org.seasar.doma.Dao;
import org.seasar.doma.Domain;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.message.DomaMessageCode;
import org.seasar.doma.internal.util.ResourceUtil;

/**
 * @author taedium
 * 
 */
public abstract class AptTestCase extends AptinaTestCase {

    protected Locale locale = Locale.JAPAN;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addSourcePath("src/test/java");
        addSourcePath("src/test/resources");
        setCharset("UTF-8");
        setLocale(locale);
    }

    @Override
    protected void tearDown() throws Exception {
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
        if (originalClass.isAnnotationPresent(Entity.class)) {
            return originalClass.getPackage().getName() + "."
                    + Constants.DEFAULT_ENTITY_PREFIX
                    + originalClass.getSimpleName();
        }
        if (originalClass.isAnnotationPresent(Domain.class)) {
            return originalClass.getPackage().getName() + "."
                    + Constants.DEFAULT_DOMAIN_PREFIX
                    + originalClass.getSimpleName();
        }
        throw new AssertionFailedError("annotation not found.");
    }

    protected void assertMessageCode(DomaMessageCode messageCode) {
        List<Diagnostic<? extends JavaFileObject>> diagnostics = getDiagnostics();
        if (diagnostics.size() == 1) {
            if (messageCode == extractMessageCode(diagnostics.get(0))) {
                return;
            }
        }
        fail();
    }

    @Override
    protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
        List<Diagnostic<? extends JavaFileObject>> results = new ArrayList<Diagnostic<? extends JavaFileObject>>();
        for (Diagnostic<? extends JavaFileObject> diagnostic : super
                .getDiagnostics()) {
            switch (diagnostic.getKind()) {
            case ERROR:
            case WARNING:
            case MANDATORY_WARNING:
                results.add(diagnostic);
                break;
            }
        }
        return results;
    }

    protected DomaMessageCode getMessageCode() {
        for (Diagnostic<? extends JavaFileObject> diagnostic : getDiagnostics()) {
            return extractMessageCode(diagnostic);
        }
        return null;
    }

    protected DomaMessageCode extractMessageCode(
            Diagnostic<? extends JavaFileObject> diagnostic) {
        String message = diagnostic.getMessage(locale);
        int start = message.indexOf('[');
        int end = message.indexOf(']');
        if (start > 0 && end > 0) {
            String code = message.substring(start + 1, end);
            if (code.startsWith("DOMA")) {
                return Enum.valueOf(DomaMessageCode.class, code);
            }
        }
        return null;
    }

}
