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
package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.EntityProcessor;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class EntityProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addSourcePath("src/test/java");
        addOption(
                "-Atest=true",
                "-Adomain.converters=org.seasar.doma.internal.apt.entity.DomainConvertersProvider");
    }

    public void testEmp() throws Exception {
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(Emp.class);
        compile();
        assertGeneratedSource(Emp.class);
        assertTrue(getCompiledResult());
    }

    public void testPrivatePropertyEntity() throws Exception {
        Class<?> target = PrivatePropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testPrivateOriginalStatesEntity() throws Exception {
        Class<?> target = PrivateOriginalStatesEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testVersionDuplicated() throws Exception {
        Class<?> target = VersionDuplicatedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4024);
    }

    public void testNotTopLevel() throws Exception {
        Class<?> target = NotTopLevelEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4018);
    }

    public void testUnsupportedProperty() throws Exception {
        Class<?> target = UnsupportedPropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4096);
    }

    public void testPrimitiveProperty() throws Exception {
        Class<?> target = PrimitivePropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testExtends() throws Exception {
        Class<?> target = ChildEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testExtendsListenerInherited() throws Exception {
        Class<?> target = Child2InheritingEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testExtendsListenerNoInherited() throws Exception {
        Class<?> target = Child2NoInheritingEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testExtendsListenerIllegalInherited() throws Exception {
        Class<?> target = Child3InheritingEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4230);
    }

    public void testExtendsWithOriginalStates() throws Exception {
        Class<?> target = OriginalStatesChildEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testPropertyNameReserved() throws Exception {
        Class<?> target = PropertyNameReservedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4025);
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
        assertMessage(Message.DOMA4093);
    }

    public void testListenerArgumentTypeIllegal() throws Exception {
        Class<?> target = ListenerArgumentTypeIllegalEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4038);
    }

    public void testAnnotationConflicted() throws Exception {
        Class<?> target = AnnotationConflictedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4086);
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

    public void testPackagePrivate() throws Exception {
        Class<?> target = PackagePrivateEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
    }

    public void testAbstractEntityListener() throws Exception {
        Class<?> target = AbstractEntityListenerEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4166);
    }

    public void testNoDefaultConstructorEntityListener() throws Exception {
        Class<?> target = NoDefaultConstructorEntityListenerEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4167);
    }

    public void testAbstractSequenceIdGenerator() throws Exception {
        Class<?> target = AbstractSequenceIdGeneratorEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4170);
    }

    public void testNoDefaultConstructorSequenceIdGenerator() throws Exception {
        Class<?> target = NoDefaultConstructorSequenceIdGeneratorEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4171);
    }

    public void testAbstractTableIdGenerator() throws Exception {
        Class<?> target = AbstractTableIdGeneratorEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4168);
    }

    public void testNoDefaultConstructorTableIdGenerator() throws Exception {
        Class<?> target = NoDefaultConstructorTableIdGeneratorEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4169);
    }

    public void testGeneratedValueNotNumber() throws Exception {
        Class<?> target = GeneratedValueNotNumberEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4095);
    }

    public void testGeneratedValueWithoutId() throws Exception {
        Class<?> target = GeneratedValueWithoutIdEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4033);
    }

    public void testGeneratedValueWithCompositeId() throws Exception {
        Class<?> target = GeneratedValueWithCompositeIdEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4036);
    }

    public void testSequenceGeneratorWithoutGeneratedValue() throws Exception {
        Class<?> target = SequenceGeneratorWithoutGeneratedValueEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4030);
    }

    public void testTableGeneratorWithoutGeneratedValue() throws Exception {
        Class<?> target = TableGeneratorWithoutGeneratedValueEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4031);
    }

    public void testDept() throws Exception {
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(Dept.class);
        compile();
        assertGeneratedSource(Dept.class);
        assertTrue(getCompiledResult());
    }

    public void testIllegalIdProperty() throws Exception {
        Class<?> target = IllegalIdPropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4095);
    }

    public void testIllegalVersionProperty() throws Exception {
        Class<?> target = IllegalVersionPropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4093);
    }

    public void testRawtypeProperty() throws Exception {
        Class<?> target = RawtypePropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4204);
    }

    public void testWildcardProperty() throws Exception {
        Class<?> target = WildcardPropertyEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4205);
    }

    public void testImmutableEntity() throws Exception {
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(ImmutableEntity.class);
        compile();
        assertGeneratedSource(ImmutableEntity.class);
        assertTrue(getCompiledResult());
    }

    public void testIllegalOriginalStatesImmutableEntity() throws Exception {
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(IllegalOriginalStatesImmutableEntity.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4224);
    }

    public void testFinalMissingImmutableEntity() throws Exception {
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(FinalMissingImmutableEntity.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4225);
    }

    public void testImmutableChildEntity() throws Exception {
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(ImmutableChildEntity.class);
        compile();
        assertGeneratedSource(ImmutableChildEntity.class);
        assertTrue(getCompiledResult());
    }

    public void testIllegalMutableChildEntity() throws Exception {
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(IllegalMutableChildEntity.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4226);
    }

    public void testNamingType1() throws Exception {
        Class<?> target = NamingType1Entity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testNamingType2() throws Exception {
        Class<?> target = NamingType2Entity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testNamingType3() throws Exception {
        Class<?> target = NamingType3Entity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testGenericListener1() throws Exception {
        Class<?> target = GenericListener1Entity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testGenericListener2() throws Exception {
        Class<?> target = GenericListener2Entity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4227);
    }

    public void testGenericListener3() throws Exception {
        Class<?> target = GenericListener3Entity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testGenericListener4() throws Exception {
        Class<?> target = GenericListener4Entity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4229);
    }

    public void testGenericListener5() throws Exception {
        Class<?> target = GenericListener5Entity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4166);
    }

    public void testGenericListener6() throws Exception {
        Class<?> target = GenericListener6Entity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testGenericListener8() throws Exception {
        Class<?> target = GenericListener8Entity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4228);
    }

}
