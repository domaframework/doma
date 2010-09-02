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
package org.seasar.doma.internal.apt.enumdomain;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.EnumDomainProcessor;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class EnumDomainProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addOption("-Atest=true");
    }

    public void testJobType() throws Exception {
        Class<?> target = JobType.class;
        EnumDomainProcessor processor = new EnumDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testPrimitiveValue() throws Exception {
        Class<?> target = PrimitiveValue.class;
        EnumDomainProcessor processor = new EnumDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testInner() throws Exception {
        Class<?> target = Outer.class;
        EnumDomainProcessor processor = new EnumDomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4180);
    }

}
