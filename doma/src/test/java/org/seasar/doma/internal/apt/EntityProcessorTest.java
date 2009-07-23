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

import org.seasar.doma.internal.apt.entity.ChildEntity;
import org.seasar.doma.internal.apt.entity.ElementOfReturnListNotDomainEntity;
import org.seasar.doma.internal.apt.entity.ElementOfReturnListUnspecifiedEntity;
import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.internal.apt.entity.ListenerArgumentTypeIllegalEntity;
import org.seasar.doma.internal.apt.entity.NameUnsafeEntity_;
import org.seasar.doma.internal.apt.entity.NotTopLevelEntity;
import org.seasar.doma.internal.apt.entity.ParamSizeNotZeroEntity;
import org.seasar.doma.internal.apt.entity.PropertyNameReservedEntity;
import org.seasar.doma.internal.apt.entity.ReturnListNotTransientEntity;
import org.seasar.doma.internal.apt.entity.ReturnListTransientEntity;
import org.seasar.doma.internal.apt.entity.ReturnTypeNotConcreteDomainEntity;
import org.seasar.doma.internal.apt.entity.ReturnTypeNotDomainEntity;
import org.seasar.doma.internal.apt.entity.VersionDuplicatedEntity;
import org.seasar.doma.internal.apt.entity.VersionNotNumberEntity;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class EntityProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addSourcePath("src/test/java");
    }

    public void testEmp() throws Exception {
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(Emp.class.getName());
        compile();
        assertGeneratedSource(Emp.class);
        assertTrue(getCompiledResult());
    }

    public void testParamSizeNotZero() throws Exception {
        Class<?> target = ParamSizeNotZeroEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4023);
    }

    public void testVersionDuplicated() throws Exception {
        Class<?> target = VersionDuplicatedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4024);
    }

    public void testNotTopLevel() throws Exception {
        Class<?> target = NotTopLevelEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4018);
    }

    public void testReturnTypeNotDomain() throws Exception {
        Class<?> target = ReturnTypeNotDomainEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4022);
    }

    public void testReturnTypeNotConcreteDomain() throws Exception {
        Class<?> target = ReturnTypeNotConcreteDomainEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4022);
    }

    public void testExtends() throws Exception {
        Class<?> target = ChildEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(ChildEntity.class);
    }

    public void testPropertyNameReserved() throws Exception {
        Class<?> target = PropertyNameReservedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4025);
    }

    public void testNameUnsafe() throws Exception {
        Class<?> target = NameUnsafeEntity_.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4026);
    }

    public void testElementOfReturnListUnspecified() throws Exception {
        Class<?> target = ElementOfReturnListUnspecifiedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4029);
    }

    public void testElementOfReturnListNotDomain() throws Exception {
        Class<?> target = ElementOfReturnListNotDomainEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4030);
    }

    public void testReturnListNotTransient() throws Exception {
        Class<?> target = ReturnListNotTransientEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4031);
    }

    public void testReturnListTransient() throws Exception {
        Class<?> target = ReturnListTransientEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(ReturnListTransientEntity.class);
        assertTrue(getCompiledResult());
    }

    public void testVersionNotNumber() throws Exception {
        Class<?> target = VersionNotNumberEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4032);
    }

    public void testListenerArgumentTypeIllegal() throws Exception {
        Class<?> target = ListenerArgumentTypeIllegalEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4038);
    }
}
