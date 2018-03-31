package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;
import org.seasar.doma.internal.apt.lombok.Value;
import org.seasar.doma.internal.apt.processor.EntityProcessor;
import org.seasar.doma.message.Message;

public class EntityProcessorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addSourcePath("src/test/java");
    addOption(
        "-Adoma.test=true",
        "-Adoma.holder.converters=org.seasar.doma.internal.apt.processor.entity.HolderConvertersProvider");
  }

  public void testEmp() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Emp.class);
    compile();
    assertGeneratedSource(Emp.class);
    assertTrue(getCompiledResult());
  }

  public void testPrivatePropertyEntity() throws Exception {
    Class<?> target = PrivatePropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testPrivateOriginalStatesEntity() throws Exception {
    Class<?> target = PrivateOriginalStatesEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testVersionDuplicated() throws Exception {
    Class<?> target = VersionDuplicatedEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4024);
  }

  public void testNotTopLevel() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(NotTopLevelEntity.class);
    compile();
    assertGeneratedSource(NotTopLevelEntity.Hoge.class);
    assertTrue(getCompiledResult());
  }

  public void testNotTopLevelImmutable() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(NotTopLevelImmutableEntity.class);
    compile();
    assertGeneratedSource(NotTopLevelImmutableEntity.Hoge.class);
    assertTrue(getCompiledResult());
  }

  public void testOuter_nonStatic() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonStaticInner.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4315);
  }

  public void testOuter_nonPublic() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonPublicInner.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4315);
  }

  public void testOuter_nonPublicMiddle() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonPublicMiddle.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4315);
  }

  public void testOuter__illegalName() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer__illegalName.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4317);
  }

  public void testUnsupportedProperty() throws Exception {
    Class<?> target = UnsupportedPropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4096);
  }

  public void testPrimitiveProperty() throws Exception {
    Class<?> target = PrimitivePropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testExtends() throws Exception {
    Class<?> target = ChildEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testExtendsListenerInherited() throws Exception {
    Class<?> target = Child2InheritingEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testExtendsListenerNoInherited() throws Exception {
    Class<?> target = Child2NoInheritingEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testExtendsListenerIllegalInherited() throws Exception {
    Class<?> target = Child3InheritingEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4230);
  }

  public void testExtendsWithOriginalStates() throws Exception {
    Class<?> target = OriginalStatesChildEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testPropertyNameReserved() throws Exception {
    Class<?> target = PropertyNameReservedEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4025);
  }

  public void testTransientProperty() throws Exception {
    Class<?> target = TransientPropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testVersionNotNumber() throws Exception {
    Class<?> target = VersionNotNumberEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4093);
  }

  public void testListenerArgumentTypeIllegal() throws Exception {
    Class<?> target = ListenerArgumentTypeIllegalEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4038);
  }

  public void testAnnotationConflicted() throws Exception {
    Class<?> target = AnnotationConflictedEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4086);
  }

  public void testHolderProperty() throws Exception {
    Class<?> target = HolderPropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testEnumProperty() throws Exception {
    Class<?> target = EnumPropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAbstract() throws Exception {
    Class<?> target = AbstractEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testCommonListener() throws Exception {
    Class<?> target = CommonChild.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  public void testPackagePrivate() throws Exception {
    Class<?> target = PackagePrivateEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  public void testAbstractEntityListener() throws Exception {
    Class<?> target = AbstractEntityListenerEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4166);
  }

  public void testNoDefaultConstructorEntityListener() throws Exception {
    Class<?> target = NoDefaultConstructorEntityListenerEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4167);
  }

  public void testAbstractSequenceIdGenerator() throws Exception {
    Class<?> target = AbstractSequenceIdGeneratorEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4170);
  }

  public void testNoDefaultConstructorSequenceIdGenerator() throws Exception {
    Class<?> target = NoDefaultConstructorSequenceIdGeneratorEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4171);
  }

  public void testAbstractTableIdGenerator() throws Exception {
    Class<?> target = AbstractTableIdGeneratorEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4168);
  }

  public void testNoDefaultConstructorTableIdGenerator() throws Exception {
    Class<?> target = NoDefaultConstructorTableIdGeneratorEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4169);
  }

  public void testGeneratedValueNotNumber() throws Exception {
    Class<?> target = GeneratedValueNotNumberEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4095);
  }

  public void testGeneratedValueWithoutId() throws Exception {
    Class<?> target = GeneratedValueWithoutIdEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4033);
  }

  public void testGeneratedValueWithCompositeId() throws Exception {
    Class<?> target = GeneratedValueWithCompositeIdEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4036);
  }

  public void testSequenceGeneratorWithoutGeneratedValue() throws Exception {
    Class<?> target = SequenceGeneratorWithoutGeneratedValueEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4030);
  }

  public void testTableGeneratorWithoutGeneratedValue() throws Exception {
    Class<?> target = TableGeneratorWithoutGeneratedValueEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4031);
  }

  public void testDept() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(Dept.class);
    compile();
    assertGeneratedSource(Dept.class);
    assertTrue(getCompiledResult());
  }

  public void testIllegalIdProperty() throws Exception {
    Class<?> target = IllegalIdPropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4095);
  }

  public void testIllegalVersionProperty() throws Exception {
    Class<?> target = IllegalVersionPropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4093);
  }

  public void testParameterizedProperty() throws Exception {
    Class<?> target = ParameterizedPropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testRawtypeProperty() throws Exception {
    Class<?> target = RawtypePropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4204);
  }

  public void testWildcardProperty() throws Exception {
    Class<?> target = WildcardPropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4205);
  }

  public void testImmutableEntity() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(ImmutableEntity.class);
    compile();
    assertGeneratedSource(ImmutableEntity.class);
    assertTrue(getCompiledResult());
  }

  public void testIllegalOriginalStatesImmutableEntity() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(IllegalOriginalStatesImmutableEntity.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4224);
  }

  public void testFinalMissingImmutableEntity() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(FinalMissingImmutableEntity.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4225);
  }

  public void testImmutableChildEntity() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(ImmutableChildEntity.class);
    compile();
    assertGeneratedSource(ImmutableChildEntity.class);
    assertTrue(getCompiledResult());
  }

  public void testIllegalMutableChildEntity() throws Exception {
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(IllegalMutableChildEntity.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4226);
  }

  public void testNamingType1() throws Exception {
    Class<?> target = NamingType1Entity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testNamingType2() throws Exception {
    Class<?> target = NamingType2Entity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testNamingType3() throws Exception {
    Class<?> target = NamingType3Entity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testGenericListener1() throws Exception {
    Class<?> target = GenericListener1Entity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testGenericListener2() throws Exception {
    Class<?> target = GenericListener2Entity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4227);
  }

  public void testGenericListener3() throws Exception {
    Class<?> target = GenericListener3Entity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testGenericListener4() throws Exception {
    Class<?> target = GenericListener4Entity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4229);
  }

  public void testGenericListener5() throws Exception {
    Class<?> target = GenericListener5Entity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4166);
  }

  public void testGenericListener6() throws Exception {
    Class<?> target = GenericListener6Entity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testGenericListener8() throws Exception {
    Class<?> target = GenericListener8Entity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4228);
  }

  public void testOptional() throws Exception {
    Class<?> target = OptionalEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testRawtypeOptional() throws Exception {
    Class<?> target = RawtypeOptionalEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4232);
  }

  public void testWildcardOptional() throws Exception {
    Class<?> target = WildcardOptionalEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4233);
  }

  public void testQuote() throws Exception {
    Class<?> target = QuoteEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testOptionalInt() throws Exception {
    Class<?> target = OptionalIntEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testOptionalLong() throws Exception {
    Class<?> target = OptionalLongEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testOptionalDouble() throws Exception {
    Class<?> target = OptionalDoubleEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testBytesProperty() throws Exception {
    Class<?> target = BytesPropertyEntity.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testEmbeddedProperty() throws Exception {
    Class<?> target = User.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testEmbeddedPropertyForImmutableEntity() throws Exception {
    Class<?> target = ImmutableUser.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testLombokValue() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    Class<?> target = LombokValue.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testLombokValueNotImmutable() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    Class<?> target = LombokValueNotImmutable.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4418);
  }

  public void testLombokValueStaticConstructor() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    Class<?> target = LombokValueStaticConstructor.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4419);
  }

  public void testLombokAllArgsConstructor() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructor.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testLombokAllArgsConstructorNotImmutable() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorNotImmutable.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4420);
  }

  public void testLombokAllArgsConstructorStaticName() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorStaticName.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4421);
  }

  public void testLombokAllArgsConstructorAccess_private() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorAccess_private.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4422);
  }

  public void testLombokAllArgsConstructorAccess_none() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorAccess_none.class;
    var processor = new EntityProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4426);
  }
}
