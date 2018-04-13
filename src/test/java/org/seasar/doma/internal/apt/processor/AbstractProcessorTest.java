package org.seasar.doma.internal.apt.processor;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.message.Message;

public class AbstractProcessorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    disableAssertionForCompilation();
    addSourcePath("src/main/java");
    addSourcePath("src/test/java");
  }

  @Override
  protected void tearDown() throws Exception {
    enableAssertionForCompilation();
    super.tearDown();
  }

  public void testRuntimeException() throws Exception {
    Class<?> target = Person.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor(
            __ -> {
              throw new NullPointerException("hoge");
            }));
    try {
      compile();
      fail();
    } catch (Exception ignored) {
    }
    assertMessage(Message.DOMA4016);
  }

  public void testAssertionError() throws Exception {
    Class<?> target = Person.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor(
            __ -> {
              throw new AssertionError("hoge");
            }));
    try {
      compile();
      fail();
    } catch (Exception ignored) {
    }
    assertMessage(Message.DOMA4016);
  }
}
