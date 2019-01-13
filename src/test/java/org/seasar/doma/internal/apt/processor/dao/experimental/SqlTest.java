package org.seasar.doma.internal.apt.processor.dao.experimental;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.processor.DaoProcessor;
import org.seasar.doma.message.Message;

/** Test case for {@link org.seasar.doma.experimental.Sql} */
public class SqlTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addOption("-Adoma.test=true");
  }

  public void testAnnotationConflict() throws Exception {
    Class<?> target = AnnotationConflictDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4444);
  }

  public void testModifySqlFileElementConflict() throws Exception {
    Class<?> target = ModifySqlFileElementConflictDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4445);
  }

  public void testBatchModifySqlFileElementConflict() throws Exception {
    Class<?> target = BatchModifySqlFileElementConflictDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4445);
  }

  public void testDefaultMethodConflict() throws Exception {
    Class<?> target = DefaultMethodConflictDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4446);
  }

  public void testSqlAnnotation() throws Exception {
    Class<?> target = SqlAnnotationDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }
}
