package org.seasar.doma.internal.apt.processor.embeddable;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;
import org.seasar.doma.internal.apt.lombok.Value;
import org.seasar.doma.internal.apt.processor.EmbeddableProcessor;
import org.seasar.doma.message.Message;

public class EmbeddableProcessorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addSourcePath("src/test/java");
    addOption("-Adoma.test=true");
  }

  public void testSimple() throws Exception {
    Class<?> target = Address.class;
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testInheritance() throws Exception {
    Class<?> target = Derived.class;
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAbstract() throws Exception {
    Class<?> target = AbstractEmbeddable.class;
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testNotTopLevel() throws Exception {
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(NotTopLevel.class);
    compile();
    assertGeneratedSource(NotTopLevel.Address.class);
    assertTrue(getCompiledResult());
  }

  public void testOuter_nonStatic() throws Exception {
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonStaticInner.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4415);
  }

  public void testOuter_nonPublic() throws Exception {
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonPublicInner.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4415);
  }

  public void testOuter_nonPublicMiddle() throws Exception {
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonPublicMiddle.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4415);
  }

  public void testOuter__illegalName() throws Exception {
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer__illegalName.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4417);
  }

  public void testLombokValue() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    Class<?> target = LombokValue.class;
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testLombokValueStaticConstructor() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    Class<?> target = LombokValueStaticConstructor.class;
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4423);
  }

  public void testLombokAllArgsConstructor() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructor.class;
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testLombokAllArgsConstructorStaticName() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorStaticName.class;
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4424);
  }

  public void testLombokAllArgsConstructorAccess_private() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorAccess_private.class;
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4425);
  }

  public void testLombokAllArgsConstructorAccess_none() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorAccess_none.class;
    var processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4427);
  }
}
