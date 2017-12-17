package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.lombok.Value;
import org.seasar.doma.internal.apt.processor.HolderProcessor;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class HolderProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addOption("-Adoma.test=true");
    }

    public void testSalary() throws Exception {
        Class<?> target = Salary.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testPrimitiveValue() throws Exception {
        Class<?> target = PrimitiveValueHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testEnum() throws Exception {
        Class<?> target = EnumHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testUnsupportedValueType() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(UnsupportedValueTypeHolder.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4102);
    }

    public void testConstrutorNotFound() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(ConstrutorNotFoundHolder.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4103);
    }

    public void testAccessorNotFound() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(AccessorNotFoundHolder.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4104);
    }

    public void testInner() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(Outer.class);
        compile();
        assertGeneratedSource(Outer.Inner.class);
        assertTrue(getCompiledResult());
    }

    public void testInner_deep() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(Outer_deepInner.class);
        compile();
        assertGeneratedSource(Outer_deepInner.Middle.Inner.class);
        assertTrue(getCompiledResult());
    }

    public void testInner_nonStatic() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(Outer_nonStaticInner.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4275);
    }

    public void testInner_nonPublic() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(Outer_nonPublicInner.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4275);
    }

    public void testInner_illegalName() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(Outer__illegalName.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4277);
    }

    public void testMiddle_nonPublic() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(Outer_nonPublicMiddle.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4275);
    }

    public void testPackagePrivate() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(PackagePrivateHolder.class);
        compile();
        assertTrue(getCompiledResult());
    }

    public void testJobType() throws Exception {
        Class<?> target = JobType.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4184);
    }

    public void testAbstractHolder() throws Exception {
        Class<?> target = AbstractHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4132);
    }

    public void testOfSalary() throws Exception {
        Class<?> target = OfSalary.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOfPrimitiveValue() throws Exception {
        Class<?> target = OfPrimitiveValueHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOfEnum() throws Exception {
        Class<?> target = OfEnumHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOfJobType() throws Exception {
        Class<?> target = OfJobType.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOfPrimitiveValueType() throws Exception {
        Class<?> target = OfPrimitiveValueType.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOfAbstractHolder() throws Exception {
        Class<?> target = OfAbstractHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testGenericHolder() throws Exception {
        Class<?> target = SpecificHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testVersionCheckSuppressed() throws Exception {
        addOption("-Adoma.version.validation=false");
        Class<?> target = VersionCheckSuppressedHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testParametarizedSalary() throws Exception {
        Class<?> target = ParametarizedSalary.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testParametarizedOfSalary() throws Exception {
        Class<?> target = ParametarizedOfSalary.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testIllegalSizeParametarizedOfSalary() throws Exception {
        Class<?> target = IllegalSizeParametarizedOfSalary.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertMessage(Message.DOMA4106);
    }

    public void testIllegalTypeParametarizedOfSalary() throws Exception {
        Class<?> target = IllegalTypeParametarizedOfSalary.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertMessage(Message.DOMA4106);
    }

    public void testNullRejection() throws Exception {
        Class<?> target = NullRejectionHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testIllegalAcceptNullHolder() throws Exception {
        Class<?> target = IllegalAcceptNullHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertMessage(Message.DOMA4251);
    }

    public void testObjectHolder() throws Exception {
        Class<?> target = ObjectHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testInterface() throws Exception {
        Class<?> target = InterfaceHolder.class;
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testInterfaceFactoryOfAttributeMustNotBeNew() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(InterfaceNew.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4268);
    }

    public void testInterfaceInner() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(InterfaceOuter.class);
        compile();
        assertGeneratedSource(InterfaceOuter.Inner.class);
        assertTrue(getCompiledResult());
    }

    public void testAnnotationMustNotBeHolderClass() throws Exception {
        HolderProcessor processor = new HolderProcessor();
        addProcessor(processor);
        addCompilationUnit(AnnotationHolder.class);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4105);
    }

    public void testLombokValue() throws Exception {
        addOption("-Adoma.lombok.Value=" + Value.class.getName());
        HolderProcessor processor = new HolderProcessor();
        Class<?> target = LombokValue.class;
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testLombokValueStaticConstructor() throws Exception {
        addOption("-Adoma.lombok.Value=" + Value.class.getName());
        HolderProcessor processor = new HolderProcessor();
        Class<?> target = LombokValueStaticConstructor.class;
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4428);
    }

    public void testLombokValueNoField() throws Exception {
        addOption("-Adoma.lombok.Value=" + Value.class.getName());
        HolderProcessor processor = new HolderProcessor();
        Class<?> target = LombokValueNoField.class;
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4430);
    }

    public void testLombokValueTwoFields() throws Exception {
        addOption("-Adoma.lombok.Value=" + Value.class.getName());
        HolderProcessor processor = new HolderProcessor();
        Class<?> target = LombokValueTwoFields.class;
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4431);
    }

    public void testLombokValueTypeNotAssignable() throws Exception {
        addOption("-Adoma.lombok.Value=" + Value.class.getName());
        HolderProcessor processor = new HolderProcessor();
        Class<?> target = LombokValueTypeNotAssignable.class;
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4432);
    }

    public void testLombokValueAccessorMethod() throws Exception {
        addOption("-Adoma.lombok.Value=" + Value.class.getName());
        HolderProcessor processor = new HolderProcessor();
        Class<?> target = LombokValueAccessorMethod.class;
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4429);
    }

    public void testLombokValueAccessorMethod_boolean() throws Exception {
        addOption("-Adoma.lombok.Value=" + Value.class.getName());
        HolderProcessor processor = new HolderProcessor();
        Class<?> target = LombokValueAccessorMethod_boolean.class;
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4429);
    }

}
