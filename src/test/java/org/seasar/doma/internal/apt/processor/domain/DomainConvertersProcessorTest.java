package org.seasar.doma.internal.apt.processor.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.processor.DomainConvertersProcessor;
import org.seasar.doma.message.Message;

public class DomainConvertersProcessorTest extends CompilerSupport {

  @BeforeEach
  protected void setUp() throws Exception {
    addOption("-Adoma.test=true");
  }

  @Test
  public void testDay() throws Exception {
    DomainConvertersProcessor processor = new DomainConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(DayConvertersProvider.class);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  public void testEmpty() throws Exception {
    DomainConvertersProcessor processor = new DomainConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(EmptyConvertersProvider.class);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  public void testExternalDomainNotSpecified() throws Exception {
    DomainConvertersProcessor processor = new DomainConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(ExternalDomainNotSpecifiedProvider.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4196);
  }
}
