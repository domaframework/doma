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
package org.seasar.doma.internal.apt.embeddable;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.EmbeddableProcessor;

/**
 * @author taedium
 * 
 */
public class EmbeddableProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addSourcePath("src/test/java");
        addOption("-Adoma.test=true");
    }

    public void testSimple() throws Exception {
        Class<?> target = Address.class;
        EmbeddableProcessor processor = new EmbeddableProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testInheritance() throws Exception {
        Class<?> target = Derived.class;
        EmbeddableProcessor processor = new EmbeddableProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAbstract() throws Exception {
        Class<?> target = AbstractEmbeddable.class;
        EmbeddableProcessor processor = new EmbeddableProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

}
