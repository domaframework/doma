package org.seasar.doma.internal.apt.processor.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.processor.ExternalDomainProcessor;
import org.seasar.doma.internal.apt.processor.domain.NestingValueObjectConverter.NestingValueObject;
import org.seasar.doma.message.Message;

public class ExternalDomainProcessorTest extends CompilerSupport {

  @BeforeEach
  protected void setUp() throws Exception {
    addOption("-Adoma.test=true");
  }

  @Test
  public void testNotDomainConverter() throws Exception {
    ExternalDomainProcessor processor = new ExternalDomainProcessor();
    addProcessor(processor);
    addCompilationUnit(NotDomainConverter.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4191);
  }

  @Test
  public void testAbstruct() throws Exception {
    ExternalDomainProcessor processor = new ExternalDomainProcessor();
    addProcessor(processor);
    addCompilationUnit(AbstractDomainConverter.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4192);
  }

  @Test
  public void testConstrutorNotFound() throws Exception {
    ExternalDomainProcessor processor = new ExternalDomainProcessor();
    addProcessor(processor);
    addCompilationUnit(ConstrutorNotFoundDomainConverter.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4193);
  }

  @Test
  public void testNotPersistent() throws Exception {
    ExternalDomainProcessor processor = new ExternalDomainProcessor();
    addProcessor(processor);
    addCompilationUnit(NotPersistentValueObjectConverter.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4194);
  }

  @Test
  public void testNestingValueObjectConverter() throws Exception {
    ExternalDomainProcessor processor = new ExternalDomainProcessor();
    addProcessor(processor);
    addCompilationUnit(NestingValueObjectConverter.class);
    compile();

    String generatedClassName =
        "__.org.seasar.doma.internal.apt.processor.domain._"
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

  @Test
  public void testValueObjectConverter() throws Exception {
    Class<?> target = ValueObjectConverter.class;
    ExternalDomainProcessor processor = new ExternalDomainProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();

    String generatedClassName =
        "__.org.seasar.doma.internal.apt.processor.domain._" + ValueObject.class.getSimpleName();
    try {
      assertEqualsGeneratedSource(getExpectedContent(), generatedClassName);
    } catch (AssertionFailedError error) {
      System.out.println(getGeneratedSource(generatedClassName));
      throw error;
    }
    assertTrue(getCompiledResult());
  }

  @Test
  public void testParameterizedValueObjectConverter() throws Exception {
    Class<?> target = ParameterizedValueObjectConverter.class;
    ExternalDomainProcessor processor = new ExternalDomainProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    String generatedClassName =
        "__.org.seasar.doma.internal.apt.processor.domain._"
            + ParameterizedValueObject.class.getSimpleName();
    try {
      assertEqualsGeneratedSource(getExpectedContent(), generatedClassName);
    } catch (AssertionFailedError error) {
      System.out.println(getGeneratedSource(generatedClassName));
      throw error;
    }
    assertTrue(getCompiledResult());
  }

  @Test
  public void testIllegalParameterizedValueObjectConverter() throws Exception {
    Class<?> target = IllegalParameterizedValueObjectConverter.class;
    ExternalDomainProcessor processor = new ExternalDomainProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4203);
  }

  @Test
  public void testBytesConversion() throws Exception {
    ExternalDomainProcessor processor = new ExternalDomainProcessor();
    addProcessor(processor);
    addCompilationUnit(UUIDConverter.class);
    compile();
    assertTrue(getCompiledResult());
  }
}
