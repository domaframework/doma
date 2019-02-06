package org.seasar.doma.internal.apt.processor.dao.experimental;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.processor.DaoProcessor;
import org.seasar.doma.message.Message;

/** Test case for {@link org.seasar.doma.experimental.Sql} */
public class SqlTest extends CompilerSupport {

  @BeforeEach
  protected void setUp() throws Exception {
    addOption("-Adoma.test=true");
  }

  @Test
  public void testAnnotationConflict() throws Exception {
    Class<?> target = AnnotationConflictDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4444);
  }

  @Test
  public void testModifySqlFileElementConflict() throws Exception {
    Class<?> target = ModifySqlFileElementConflictDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4445);
  }

  @Test
  public void testBatchModifySqlFileElementConflict() throws Exception {
    Class<?> target = BatchModifySqlFileElementConflictDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4445);
  }

  @Test
  public void testDefaultMethodConflict() throws Exception {
    Class<?> target = DefaultMethodConflictDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4446);
  }

  @Test
  public void testSqlAnnotation() throws Exception {
    Class<?> target = SqlAnnotationDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }
}
