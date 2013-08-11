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
import org.seasar.doma.internal.apt.DomainProcessor;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class DomainProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addOption("-Atest=true");
    }

    public void testSalary() throws Exception {
        Class<?> target = Salary.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testPrimitiveValue() throws Exception {
        Class<?> target = PrimitiveValueDomain.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testEnum() throws Exception {
        Class<?> target = EnumDomain.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testUnsupportedValueType() throws Exception {
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(UnsupportedValueTypeDomain.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4102);
    }

    public void testConstrutorNotFound() throws Exception {
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(ConstrutorNotFoundDomain.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4103);
    }

    public void testAccessorNotFound() throws Exception {
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(AccessorNotFoundDomain.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4104);
    }

    public void testInner() throws Exception {
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(Outer.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4179);
    }

    public void testPackagePrivate() throws Exception {
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(PackagePrivateDomain.class);
        compile();
        assertTrue(getCompiledResult());
    }

    public void testJobType() throws Exception {
        Class<?> target = JobType.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4184);
    }

    public void testAbstractDomain() throws Exception {
        Class<?> target = AbstractDomain.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4132);
    }

    public void testOfSalary() throws Exception {
        Class<?> target = OfSalary.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOfPrimitiveValue() throws Exception {
        Class<?> target = OfPrimitiveValueDomain.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOfEnum() throws Exception {
        Class<?> target = OfEnumDomain.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOfJobType() throws Exception {
        Class<?> target = OfJobType.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOfPrimitiveValueType() throws Exception {
        Class<?> target = OfPrimitiveValueType.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOfAbstractDomain() throws Exception {
        Class<?> target = OfAbstractDomain.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testGenericDomain() throws Exception {
        Class<?> target = SpecificDomain.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testVersionCheckSuppressed() throws Exception {
        addOption("-Aversion.validation=false");
        Class<?> target = VersionCheckSuppressedDomain.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testParametarizedSalary() throws Exception {
        Class<?> target = ParametarizedSalary.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testParametarizedOfSalary() throws Exception {
        Class<?> target = ParametarizedOfSalary.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testIllegalSizeParametarizedOfSalary() throws Exception {
        Class<?> target = IllegalSizeParametarizedOfSalary.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertMessage(Message.DOMA4106);
    }

    public void testIllegalTypeParametarizedOfSalary() throws Exception {
        Class<?> target = IllegalTypeParametarizedOfSalary.class;
        DomainProcessor processor = new DomainProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertMessage(Message.DOMA4106);
    }

}
