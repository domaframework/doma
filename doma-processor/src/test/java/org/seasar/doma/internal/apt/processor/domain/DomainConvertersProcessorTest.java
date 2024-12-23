package org.seasar.doma.internal.apt.processor.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.processor.DomainConvertersProcessor;
import org.seasar.doma.message.Message;

class DomainConvertersProcessorTest extends CompilerSupport {

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.test=true");
    addProcessor(new DomainConvertersProcessor());
  }

  @Test
  void testDay() throws Exception {
    addCompilationUnit(DayConvertersProvider.class);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testEmpty() throws Exception {
    addCompilationUnit(EmptyConvertersProvider.class);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testExternalDomainNotSpecified() throws Exception {
    addCompilationUnit(ExternalDomainNotSpecifiedProvider.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4196);
  }
}
