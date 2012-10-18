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

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.DomainConvertersProcessor;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class DomainConvertersProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addOption("-Atest=true");
    }

    public void testDay() throws Exception {
        DomainConvertersProcessor processor = new DomainConvertersProcessor();
        addProcessor(processor);
        addCompilationUnit(DayConvertersProvider.class);
        compile();
        assertTrue(getCompiledResult());
    }

    public void testEmpty() throws Exception {
        DomainConvertersProcessor processor = new DomainConvertersProcessor();
        addProcessor(processor);
        addCompilationUnit(EmptyConvertersProvider.class);
        compile();
        assertTrue(getCompiledResult());
    }

    public void testExternalDomainNotSpecified() throws Exception {
        DomainConvertersProcessor processor = new DomainConvertersProcessor();
        addProcessor(processor);
        addCompilationUnit(ExternalDomainNotSpecifiedProvider.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4196);
    }

}
