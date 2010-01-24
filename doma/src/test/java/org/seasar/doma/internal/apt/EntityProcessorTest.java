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

import org.seasar.doma.internal.apt.entity.AbstractEntity;
import org.seasar.doma.internal.apt.entity.AnnotationConflictedEntity;
import org.seasar.doma.internal.apt.entity.ChildEntity;
import org.seasar.doma.internal.apt.entity.CommonChild;
import org.seasar.doma.internal.apt.entity.DomainPropertyEntity;
import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.internal.apt.entity.EnumPropertyEntity;
import org.seasar.doma.internal.apt.entity.ListenerArgumentTypeIllegalEntity;
import org.seasar.doma.internal.apt.entity.NotTopLevelEntity;
import org.seasar.doma.internal.apt.entity.PrimitivePropertyEntity;
import org.seasar.doma.internal.apt.entity.PrivatePropertyEntity;
import org.seasar.doma.internal.apt.entity.PropertyNameReservedEntity;
import org.seasar.doma.internal.apt.entity.TransientPropertyEntity;
import org.seasar.doma.internal.apt.entity.UnsupportedPropertyEntity;
import org.seasar.doma.internal.apt.entity.VersionDuplicatedEntity;
import org.seasar.doma.internal.apt.entity.VersionNotNumberEntity;
import org.seasar.doma.internal.message.Message;

/**
 * @author taedium
 * 
 */
public class EntityProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addSourcePath("src/test/java");
        addOption("-Atest=true");
    }

    public void testEmp() throws Exception {
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(Emp.class);
        compile();
        assertGeneratedSource(Emp.class);
        assertTrue(getCompiledResult());
    }

    public void testPrivateEntity() throws Exception {
        Class<?> target = PrivatePropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4094);
    }

    public void testPrivatePropertyEntity() throws Exception {
        Class<?> target = PrivatePropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4094);
    }

    public void testVersionDuplicated() throws Exception {
        Class<?> target = VersionDuplicatedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4024);
    }

    public void testNotTopLevel() throws Exception {
        Class<?> target = NotTopLevelEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4018);
    }

    public void testUnsupportedProperty() throws Exception {
        Class<?> target = UnsupportedPropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4096);
    }

    public void testPrimitiveProperty() throws Exception {
        Class<?> target = PrimitivePropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
    }

    public void testExtends() throws Exception {
        Class<?> target = ChildEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
    }

    public void testPropertyNameReserved() throws Exception {
        Class<?> target = PropertyNameReservedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4025);
    }

    public void testTransientProperty() throws Exception {
        Class<?> target = TransientPropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testVersionNotNumber() throws Exception {
        Class<?> target = VersionNotNumberEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4093);
    }

    public void testListenerArgumentTypeIllegal() throws Exception {
        Class<?> target = ListenerArgumentTypeIllegalEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4038);
    }

    public void testAnnotationConflicted() throws Exception {
        Class<?> target = AnnotationConflictedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4086);
    }

    public void testDomainProperty() throws Exception {
        Class<?> target = DomainPropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testEnumProperty() throws Exception {
        Class<?> target = EnumPropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAbstract() throws Exception {
        Class<?> target = AbstractEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testCommonListener() throws Exception {
        Class<?> target = CommonChild.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
    }
}
