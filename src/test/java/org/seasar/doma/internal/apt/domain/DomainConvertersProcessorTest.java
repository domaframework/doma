package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.DomainConvertersProcessor;
import org.seasar.doma.message.Message;

public class DomainConvertersProcessorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addOption("-Adoma.test=true");
  }

  public void testDay() throws Exception {
    DomainConvertersProcessor processor = new DomainConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(DayConvertersProvider.class);
    compile();
    assertTrue(getCompiledResult());
  }

  public void testEmpty() throws Exception {
    DomainConvertersProcessor processor = new DomainConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(EmptyConvertersProvider.class);
    compile();
    assertTrue(getCompiledResult());
  }

  public void testExternalDomainNotSpecified() throws Exception {
    DomainConvertersProcessor processor = new DomainConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(ExternalDomainNotSpecifiedProvider.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4196);
  }
}
