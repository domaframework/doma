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
package org.seasar.doma.internal.apt.holder;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.ExternalHolderProcessor;
import org.seasar.doma.internal.apt.holder.NestingValueObjectConverter.NestingValueObject;
import org.seasar.doma.message.Message;

import junit.framework.AssertionFailedError;

/**
 * @author taedium
 * 
 */
public class ExternalHolderProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addOption("-Adoma.test=true");
    }

    public void testNotHolderConverter() throws Exception {
        ExternalHolderProcessor processor = new ExternalHolderProcessor();
        addProcessor(processor);
        addCompilationUnit(NotHolderConverter.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4191);
    }

    public void testAbstruct() throws Exception {
        ExternalHolderProcessor processor = new ExternalHolderProcessor();
        addProcessor(processor);
        addCompilationUnit(AbstractHolderConverter.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4192);
    }

    public void testConstrutorNotFound() throws Exception {
        ExternalHolderProcessor processor = new ExternalHolderProcessor();
        addProcessor(processor);
        addCompilationUnit(ConstrutorNotFoundHolderConverter.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4193);
    }

    public void testNotPersistent() throws Exception {
        ExternalHolderProcessor processor = new ExternalHolderProcessor();
        addProcessor(processor);
        addCompilationUnit(NotPersistentValueObjectConverter.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4194);
    }

    public void testNestingValueObjectConverter() throws Exception {
        ExternalHolderProcessor processor = new ExternalHolderProcessor();
        addProcessor(processor);
        addCompilationUnit(NestingValueObjectConverter.class);
        compile();

        String generatedClassName = "__.org.seasar.doma.internal.apt.holder._"
                + NestingValueObjectConverter.class.getSimpleName() + "__"
                + NestingValueObject.class.getSimpleName();
        try {
            assertEqualsGeneratedSource(getExpectedContent(),
                    generatedClassName);
        } catch (AssertionFailedError error) {
            System.out.println(getGeneratedSource(generatedClassName));
            throw error;
        }
        assertTrue(getCompiledResult());
    }

    public void testValueObjectConverter() throws Exception {
        Class<?> target = ValueObjectConverter.class;
        ExternalHolderProcessor processor = new ExternalHolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();

        String generatedClassName = "__.org.seasar.doma.internal.apt.holder._"
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
        ExternalHolderProcessor processor = new ExternalHolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        String generatedClassName = "__.org.seasar.doma.internal.apt.holder._"
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
        ExternalHolderProcessor processor = new ExternalHolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4203);
    }

    public void testBytesConversion() throws Exception {
        ExternalHolderProcessor processor = new ExternalHolderProcessor();
        addProcessor(processor);
        addCompilationUnit(UUIDConverter.class);
        compile();
        assertTrue(getCompiledResult());
    }

}
