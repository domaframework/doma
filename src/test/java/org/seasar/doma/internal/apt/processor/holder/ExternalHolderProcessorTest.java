package org.seasar.doma.internal.apt.processor.holder;

import junit.framework.AssertionFailedError;
import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.processor.ExternalHolderProcessor;
import org.seasar.doma.internal.apt.processor.holder.NestingValueObjectConverter.NestingValueObject;
import org.seasar.doma.message.Message;

/** @author taedium */
public class ExternalHolderProcessorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addOption("-Adoma.test=true");
  }

  public void testNotHolderConverter() throws Exception {
    ExternalHolderProcessor processor = new ExternalHolderProcessor();
    addProcessor(processor);
    addCompilationUnit(NotHolderConverter.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4191);
  }

  public void testAbstruct() throws Exception {
    ExternalHolderProcessor processor = new ExternalHolderProcessor();
    addProcessor(processor);
    addCompilationUnit(AbstractHolderConverter.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4192);
  }

  public void testConstrutorNotFound() throws Exception {
    ExternalHolderProcessor processor = new ExternalHolderProcessor();
    addProcessor(processor);
    addCompilationUnit(ConstrutorNotFoundHolderConverter.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4193);
  }

  public void testNotPersistent() throws Exception {
    ExternalHolderProcessor processor = new ExternalHolderProcessor();
    addProcessor(processor);
    addCompilationUnit(NotPersistentValueObjectConverter.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4194);
  }

  public void testNestingValueObjectConverter() throws Exception {
    ExternalHolderProcessor processor = new ExternalHolderProcessor();
    addProcessor(processor);
    addCompilationUnit(NestingValueObjectConverter.class);
    compile();

    String generatedClassName =
        "__.org.seasar.doma.internal.apt.processor.holder._"
            + NestingValueObjectConverter.class.getSimpleName()
            + "__"
            + NestingValueObject.class.getSimpleName();
    try {
      assertEqualsGeneratedSource(getExpectedContent(), generatedClassName);
    } catch (AssertionFailedError error) {
      System.out.println(getGeneratedSource(generatedClassName));
      throw error;
    }
    assertTrue(getCompiledResult());
  }

  public void testValueObjectConverter() throws Exception {
    Class<?> target = ValueObjectConverter.class;
    ExternalHolderProcessor processor = new ExternalHolderProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();

    String generatedClassName =
        "__.org.seasar.doma.internal.apt.processor.holder._" + ValueObject.class.getSimpleName();
    try {
      assertEqualsGeneratedSource(getExpectedContent(), generatedClassName);
    } catch (AssertionFailedError error) {
      System.out.println(getGeneratedSource(generatedClassName));
      throw error;
    }
    assertTrue(getCompiledResult());
  }

  public void testParameterizedValueObjectConverter() throws Exception {
    Class<?> target = ParameterizedValueObjectConverter.class;
    ExternalHolderProcessor processor = new ExternalHolderProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    String generatedClassName =
        "__.org.seasar.doma.internal.apt.processor.holder._"
            + ParameterizedValueObject.class.getSimpleName();
    try {
      assertEqualsGeneratedSource(getExpectedContent(), generatedClassName);
    } catch (AssertionFailedError error) {
      System.out.println(getGeneratedSource(generatedClassName));
      throw error;
    }
    assertTrue(getCompiledResult());
  }

  public void testIllegalParameterizedValueObjectConverter() throws Exception {
    Class<?> target = IllegalParameterizedValueObjectConverter.class;
    ExternalHolderProcessor processor = new ExternalHolderProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4203);
  }

  public void testBytesConversion() throws Exception {
    ExternalHolderProcessor processor = new ExternalHolderProcessor();
    addProcessor(processor);
    addCompilationUnit(UUIDConverter.class);
    compile();
    assertTrue(getCompiledResult());
  }
}
