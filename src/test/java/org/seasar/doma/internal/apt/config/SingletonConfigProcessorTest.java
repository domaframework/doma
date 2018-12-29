package org.seasar.doma.internal.apt.config;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.SingletonConfigProcessor;
import org.seasar.doma.message.Message;

public class SingletonConfigProcessorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addOption("-Adoma.test=true");
  }

  public void testNoConfig() throws Exception {
    Class<?> target = NoConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4253);
  }

  public void testMethodNotFoundConfig() throws Exception {
    Class<?> target = MethodNotFoundConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4254);
  }

  public void testPublicConstructorConfig() throws Exception {
    Class<?> target = PublicConstructorConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4256);
  }

  public void testValidConfig() throws Exception {
    Class<?> target = ValidConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }
}
