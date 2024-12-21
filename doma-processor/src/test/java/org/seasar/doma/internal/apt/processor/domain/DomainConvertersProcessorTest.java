package org.seasar.doma.internal.apt.processor.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.processor.DomainConvertersProcessor;
import org.seasar.doma.message.Message;

class DomainConvertersProcessorTest extends CompilerSupport {

  @TempDir Path sourceOutput;
  @TempDir Path classOutput;

  @BeforeEach
  void beforeEach() {
    setSourceOutput(sourceOutput);
    setClassOutput(classOutput);
    addOption("-Adoma.test=true");
  }

  @Test
  void testDay() throws Exception {
    DomainConvertersProcessor processor = new DomainConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(DayConvertersProvider.class);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testEmpty() throws Exception {
    DomainConvertersProcessor processor = new DomainConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(EmptyConvertersProvider.class);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testExternalDomainNotSpecified() throws Exception {
    DomainConvertersProcessor processor = new DomainConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(ExternalDomainNotSpecifiedProvider.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4196);
  }
}
