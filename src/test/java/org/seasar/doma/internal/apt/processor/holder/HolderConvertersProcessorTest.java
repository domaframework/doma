package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.processor.HolderConvertersProcessor;
import org.seasar.doma.message.Message;

public class HolderConvertersProcessorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addOption("-Adoma.test=true");
  }

  public void testDay() throws Exception {
    HolderConvertersProcessor processor = new HolderConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(DayConvertersProvider.class);
    compile();
    assertTrue(getCompiledResult());
  }

  public void testEmpty() throws Exception {
    HolderConvertersProcessor processor = new HolderConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(EmptyConvertersProvider.class);
    compile();
    assertTrue(getCompiledResult());
  }

  public void testExternalHolderNotSpecified() throws Exception {
    HolderConvertersProcessor processor = new HolderConvertersProcessor();
    addProcessor(processor);
    addCompilationUnit(ExternalHolderNotSpecifiedProvider.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4196);
  }
}
