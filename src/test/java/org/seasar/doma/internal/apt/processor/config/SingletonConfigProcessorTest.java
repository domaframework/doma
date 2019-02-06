package org.seasar.doma.internal.apt.processor.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.processor.SingletonConfigProcessor;
import org.seasar.doma.message.Message;

public class SingletonConfigProcessorTest extends CompilerSupport {

  @BeforeEach
  protected void setUp() throws Exception {
    addOption("-Adoma.test=true");
  }

  @Test
  public void testNoConfig() throws Exception {
    Class<?> target = NoConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4253);
  }

  @Test
  public void testMethodNotFoundConfig() throws Exception {
    Class<?> target = MethodNotFoundConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4254);
  }

  @Test
  public void testPublicConstructorConfig() throws Exception {
    Class<?> target = PublicConstructorConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4256);
  }

  @Test
  public void testValidConfig() throws Exception {
    Class<?> target = ValidConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }
}
