package org.seasar.doma.internal.apt.processor.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;
import org.seasar.doma.internal.apt.lombok.Value;
import org.seasar.doma.internal.apt.processor.EntityProcessor;
import org.seasar.doma.message.Message;

public class EntityProcessorTest extends CompilerSupport {

  @BeforeEach
  protected void setUp() throws Exception {
    addSourcePath("src/test/java");
    addOption(
        "-Adoma.test=true",
        "-Adoma.domain.converters=org.seasar.doma.internal.apt.processor.entity.DomainConvertersProvider");
  }

  @Test
  public void testEmp() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Emp.class);
    compile();
    assertGeneratedSource(Emp.class);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testPrivatePropertyEntity() throws Exception {
    Class<?> target = PrivatePropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testPrivateOriginalStatesEntity() throws Exception {
    Class<?> target = PrivateOriginalStatesEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testVersionDuplicated() throws Exception {
    Class<?> target = VersionDuplicatedEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4024);
  }

  @Test
  public void testNotTopLevel() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(NotTopLevelEntity.class);
    compile();
    assertGeneratedSource(NotTopLevelEntity.Hoge.class);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testNotTopLevelImmutable() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(NotTopLevelImmutableEntity.class);
    compile();
    assertGeneratedSource(NotTopLevelImmutableEntity.Hoge.class);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testOuter_nonStatic() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonStaticInner.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4315);
  }

  @Test
  public void testOuter_nonPublic() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonPublicInner.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4315);
  }

  @Test
  public void testOuter_nonPublicMiddle() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonPublicMiddle.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4315);
  }

  @Test
  public void testOuter__illegalName() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer__illegalName.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4317);
  }

  @Test
  public void testUnsupportedProperty() throws Exception {
    Class<?> target = UnsupportedPropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4096);
  }

  @Test
  public void testPrimitiveProperty() throws Exception {
    Class<?> target = PrimitivePropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testExtends() throws Exception {
    Class<?> target = ChildEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testExtendsListenerInherited() throws Exception {
    Class<?> target = Child2InheritingEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testExtendsListenerNoInherited() throws Exception {
    Class<?> target = Child2NoInheritingEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testExtendsListenerIllegalInherited() throws Exception {
    Class<?> target = Child3InheritingEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4230);
  }

  @Test
  public void testExtendsWithOriginalStates() throws Exception {
    Class<?> target = OriginalStatesChildEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testPropertyNameReserved() throws Exception {
    Class<?> target = PropertyNameReservedEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4025);
  }

  @Test
  public void testTransientProperty() throws Exception {
    Class<?> target = TransientPropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testVersionNotNumber() throws Exception {
    Class<?> target = VersionNotNumberEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4093);
  }

  @Test
  public void testListenerArgumentTypeIllegal() throws Exception {
    Class<?> target = ListenerArgumentTypeIllegalEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4038);
  }

  @Test
  public void testAnnotationConflicted() throws Exception {
    Class<?> target = AnnotationConflictedEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4086);
  }

  @Test
  public void testDomainProperty() throws Exception {
    Class<?> target = DomainPropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testEnumProperty() throws Exception {
    Class<?> target = EnumPropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAbstract() throws Exception {
    Class<?> target = AbstractEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testCommonListener() throws Exception {
    Class<?> target = CommonChild.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  public void testPackagePrivate() throws Exception {
    Class<?> target = PackagePrivateEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAbstractEntityListener() throws Exception {
    Class<?> target = AbstractEntityListenerEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4166);
  }

  @Test
  public void testNoDefaultConstructorEntityListener() throws Exception {
    Class<?> target = NoDefaultConstructorEntityListenerEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4167);
  }

  @Test
  public void testAbstractSequenceIdGenerator() throws Exception {
    Class<?> target = AbstractSequenceIdGeneratorEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4170);
  }

  @Test
  public void testNoDefaultConstructorSequenceIdGenerator() throws Exception {
    Class<?> target = NoDefaultConstructorSequenceIdGeneratorEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4171);
  }

  @Test
  public void testAbstractTableIdGenerator() throws Exception {
    Class<?> target = AbstractTableIdGeneratorEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4168);
  }

  @Test
  public void testNoDefaultConstructorTableIdGenerator() throws Exception {
    Class<?> target = NoDefaultConstructorTableIdGeneratorEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4169);
  }

  @Test
  public void testGeneratedValueNotNumber() throws Exception {
    Class<?> target = GeneratedValueNotNumberEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4095);
  }

  @Test
  public void testGeneratedValueWithoutId() throws Exception {
    Class<?> target = GeneratedValueWithoutIdEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4033);
  }

  @Test
  public void testGeneratedValueWithCompositeId() throws Exception {
    Class<?> target = GeneratedValueWithCompositeIdEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4036);
  }

  @Test
  public void testSequenceGeneratorWithoutGeneratedValue() throws Exception {
    Class<?> target = SequenceGeneratorWithoutGeneratedValueEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4030);
  }

  @Test
  public void testTableGeneratorWithoutGeneratedValue() throws Exception {
    Class<?> target = TableGeneratorWithoutGeneratedValueEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4031);
  }

  @Test
  public void testDept() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Dept.class);
    compile();
    assertGeneratedSource(Dept.class);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testIllegalIdProperty() throws Exception {
    Class<?> target = IllegalIdPropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4095);
  }

  @Test
  public void testIllegalVersionProperty() throws Exception {
    Class<?> target = IllegalVersionPropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4093);
  }

  @Test
  public void testParameterizedProperty() throws Exception {
    Class<?> target = ParameterizedPropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testRawtypeProperty() throws Exception {
    Class<?> target = RawtypePropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4204);
  }

  @Test
  public void testWildcardProperty() throws Exception {
    Class<?> target = WildcardPropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4205);
  }

  @Test
  public void testImmutableEntity() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(ImmutableEntity.class);
    compile();
    assertGeneratedSource(ImmutableEntity.class);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testIllegalOriginalStatesImmutableEntity() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(IllegalOriginalStatesImmutableEntity.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4224);
  }

  @Test
  public void testFinalMissingImmutableEntity() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(FinalMissingImmutableEntity.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4225);
  }

  @Test
  public void testImmutableChildEntity() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(ImmutableChildEntity.class);
    compile();
    assertGeneratedSource(ImmutableChildEntity.class);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testIllegalMutableChildEntity() throws Exception {
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(IllegalMutableChildEntity.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4226);
  }

  @Test
  public void testNamingType1() throws Exception {
    Class<?> target = NamingType1Entity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testNamingType2() throws Exception {
    Class<?> target = NamingType2Entity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testNamingType3() throws Exception {
    Class<?> target = NamingType3Entity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testGenericListener1() throws Exception {
    Class<?> target = GenericListener1Entity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testGenericListener2() throws Exception {
    Class<?> target = GenericListener2Entity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4227);
  }

  @Test
  public void testGenericListener3() throws Exception {
    Class<?> target = GenericListener3Entity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testGenericListener4() throws Exception {
    Class<?> target = GenericListener4Entity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4229);
  }

  @Test
  public void testGenericListener5() throws Exception {
    Class<?> target = GenericListener5Entity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4166);
  }

  @Test
  public void testGenericListener6() throws Exception {
    Class<?> target = GenericListener6Entity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testGenericListener8() throws Exception {
    Class<?> target = GenericListener8Entity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4228);
  }

  @Test
  public void testOptional() throws Exception {
    Class<?> target = OptionalEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testRawtypeOptional() throws Exception {
    Class<?> target = RawtypeOptionalEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4232);
  }

  @Test
  public void testWildcardOptional() throws Exception {
    Class<?> target = WildcardOptionalEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4233);
  }

  @Test
  public void testQuote() throws Exception {
    Class<?> target = QuoteEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testOptionalInt() throws Exception {
    Class<?> target = OptionalIntEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testOptionalLong() throws Exception {
    Class<?> target = OptionalLongEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testOptionalDouble() throws Exception {
    Class<?> target = OptionalDoubleEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testBytesProperty() throws Exception {
    Class<?> target = BytesPropertyEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testEmbeddedProperty() throws Exception {
    Class<?> target = User.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testEmbeddedPropertyForImmutableEntity() throws Exception {
    Class<?> target = ImmutableUser.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testLombokValue() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    Class<?> target = LombokValue.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testLombokValueNotImmutable() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    Class<?> target = LombokValueNotImmutable.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4418);
  }

  @Test
  public void testLombokValueStaticConstructor() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    Class<?> target = LombokValueStaticConstructor.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4419);
  }

  @Test
  public void testLombokAllArgsConstructor() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructor.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testLombokAllArgsConstructorNotImmutable() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorNotImmutable.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4420);
  }

  @Test
  public void testLombokAllArgsConstructorStaticName() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorStaticName.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4421);
  }

  @Test
  public void testLombokAllArgsConstructorAccess_private() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorAccess_private.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4422);
  }

  @Test
  public void testLombokAllArgsConstructorAccess_none() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorAccess_none.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4426);
  }

  @Test
  public void testTenantId() throws Exception {
    Class<?> target = TenantIdEntity.class;
    EntityProcessor processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }
}
