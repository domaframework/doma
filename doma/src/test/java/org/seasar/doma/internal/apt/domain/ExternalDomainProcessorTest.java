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
package org.seasar.doma.internal.apt.domain;

import junit.framework.AssertionFailedError;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.ExternalDomainProcessor;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class ExternalDomainProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addOption("-Atest=true");
    }

    public void testNotDomainConverter() throws Exception {
        ExternalDomainProcessor processor = new ExternalDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(NotDomainConverter.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4191);
    }

    public void testAbstruct() throws Exception {
        ExternalDomainProcessor processor = new ExternalDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(AbstractDomainConverter.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4192);
    }

    public void testConstrutorNotFound() throws Exception {
        ExternalDomainProcessor processor = new ExternalDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(ConstrutorNotFoundDomainConverter.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4193);
    }

    public void testNotPersistent() throws Exception {
        ExternalDomainProcessor processor = new ExternalDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(NotPersistentValueObjectConverter.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4194);
    }

    public void testEnumDomain() throws Exception {
        ExternalDomainProcessor processor = new ExternalDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(EnumDomainValueObjectConverter.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4195);
    }

    public void testNestingValueObjectConverter() throws Exception {
        ExternalDomainProcessor processor = new ExternalDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(NestingValueObjectConverter.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4199);
    }

    public void testValueObjectConverter() throws Exception {
        Class<?> target = ValueObjectConverter.class;
        ExternalDomainProcessor processor = new ExternalDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();

        String generatedClassName = "__.org.seasar.doma.internal.apt.domain._"
                + ValueObject.class.getSimpleName();
        try {
            assertEqualsGeneratedSource(getExpectedContent(),
                    generatedClassName);
        } catch (AssertionFailedError error) {
            System.out.println(getGeneratedSource(generatedClassName));
            throw error;
        }
        assertTrue(getCompiledResult());
    }

    public void testParameterizedValueObjectConverter() throws Exception {
        Class<?> target = ParameterizedValueObjectConverter.class;
        ExternalDomainProcessor processor = new ExternalDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        String generatedClassName = "__.org.seasar.doma.internal.apt.domain._"
                + ParameterizedValueObject.class.getSimpleName();
        try {
            assertEqualsGeneratedSource(getExpectedContent(),
                    generatedClassName);
        } catch (AssertionFailedError error) {
            System.out.println(getGeneratedSource(generatedClassName));
            throw error;
        }
        assertTrue(getCompiledResult());
    }

    public void testIllegalParameterizedValueObjectConverter() throws Exception {
        Class<?> target = IllegalParameterizedValueObjectConverter.class;
        ExternalDomainProcessor processor = new ExternalDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4203);
    }

}
