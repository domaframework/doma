package org.seasar.doma.internal.apt.processor.embeddable;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;
import org.seasar.doma.internal.apt.lombok.Value;
import org.seasar.doma.internal.apt.processor.EmbeddableProcessor;
import org.seasar.doma.message.Message;

public class EmbeddableProcessorTest extends CompilerSupport {

  @BeforeEach
  protected void setUp() throws Exception {
    addSourcePath("src/test/java");
    addOption("-Adoma.test=true");
  }

  @Test
  public void testSimple() throws Exception {
    Class<?> target = Address.class;
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testInheritance() throws Exception {
    Class<?> target = Derived.class;
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAbstract() throws Exception {
    Class<?> target = AbstractEmbeddable.class;
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testNotTopLevel() throws Exception {
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(NotTopLevel.class);
    compile();
    assertGeneratedSource(NotTopLevel.Address.class);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testOuter_nonStatic() throws Exception {
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonStaticInner.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4415);
  }

  @Test
  public void testOuter_nonPublic() throws Exception {
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonPublicInner.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4415);
  }

  @Test
  public void testOuter_nonPublicMiddle() throws Exception {
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer_nonPublicMiddle.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4415);
  }

  @Test
  public void testOuter__illegalName() throws Exception {
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(Outer__illegalName.class);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4417);
  }

  @Test
  public void testLombokValue() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    Class<?> target = LombokValue.class;
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testLombokValueStaticConstructor() throws Exception {
    addOption("-Adoma.lombok.Value=" + Value.class.getName());
    Class<?> target = LombokValueStaticConstructor.class;
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4423);
  }

  @Test
  public void testLombokAllArgsConstructor() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructor.class;
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testLombokAllArgsConstructorStaticName() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorStaticName.class;
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4424);
  }

  @Test
  public void testLombokAllArgsConstructorAccess_private() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorAccess_private.class;
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4425);
  }

  @Test
  public void testLombokAllArgsConstructorAccess_none() throws Exception {
    addOption("-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName());
    Class<?> target = LombokAllArgsConstructorAccess_none.class;
    EmbeddableProcessor processor = new EmbeddableProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4427);
  }
}
