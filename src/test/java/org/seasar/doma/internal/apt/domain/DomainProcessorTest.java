package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.DomainProcessor;
import org.seasar.doma.internal.apt.lombok.Value;
import org.seasar.doma.message.Message;

public class DomainProcessorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addOption("-Adoma.test=true");
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
    assertGeneratedSource(Outer.Inner.class);
    assertTrue(getCompiledResult());
  }

  public void testInner_deep() throws Exception {
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_deepInner.class);
    compile();
    assertGeneratedSource(Outer_deepInner.Middle.Inner.class);
    assertTrue(getCompiledResult());
  }

  public void testInner_nonStatic() throws Exception {
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonStaticInner.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4275);
  }

  public void testInner_nonPublic() throws Exception {
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonPublicInner.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4275);
  }

  public void testInner_illegalName() throws Exception {
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer__illegalName.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4277);
  }

  public void testMiddle_nonPublic() throws Exception {
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonPublicMiddle.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4275);
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
    addOption("-Adoma.version.validation=false");
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

  public void testNullRejection() throws Exception {
    Class<?> target = NullRejectionDomain.class;
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testIllegalAcceptNullDomain() throws Exception {
    Class<?> target = IllegalAcceptNullDomain.class;
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertMessage(Message.DOMA4251);
  }

  public void testObjectDomain() throws Exception {
    Class<?> target = ObjectDomain.class;
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testInterface() throws Exception {
    Class<?> target = InterfaceDomain.class;
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testInterfaceFactoryOfAttributeMustNotBeNew() throws Exception {
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(InterfaceNew.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4268);
  }

  public void testInterfaceInner() throws Exception {
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(InterfaceOuter.class);
    compile();
    assertGeneratedSource(InterfaceOuter.Inner.class);
    assertTrue(getCompiledResult());
  }

  public void testAnnotationMustNotBeDomainClass() throws Exception {
    DomainProcessor processor = new DomainProcessor();
    addProcessor(processor);
    addCompilationUnit(AnnotationDomain.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4105);
  }

  public void testLombokValue() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    DomainProcessor processor = new DomainProcessor();
    Class<?> target = LombokValue.class;
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testLombokValueStaticConstructor() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    DomainProcessor processor = new DomainProcessor();
    Class<?> target = LombokValueStaticConstructor.class;
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4428);
  }

  public void testLombokValueNoField() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    DomainProcessor processor = new DomainProcessor();
    Class<?> target = LombokValueNoField.class;
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4430);
  }

  public void testLombokValueTwoFields() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    DomainProcessor processor = new DomainProcessor();
    Class<?> target = LombokValueTwoFields.class;
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4431);
  }

  public void testLombokValueTypeNotAssignable() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    DomainProcessor processor = new DomainProcessor();
    Class<?> target = LombokValueTypeNotAssignable.class;
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4432);
  }

  public void testLombokValueAccessorMethod() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    DomainProcessor processor = new DomainProcessor();
    Class<?> target = LombokValueAccessorMethod.class;
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4429);
  }

  public void testLombokValueAccessorMethod_boolean() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    DomainProcessor processor = new DomainProcessor();
    Class<?> target = LombokValueAccessorMethod_boolean.class;
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4429);
  }
}
