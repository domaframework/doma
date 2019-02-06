package org.seasar.doma.internal.apt.processor.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.processor.DaoProcessor;
import org.seasar.doma.message.Message;

public class DaoProcessorTest extends CompilerSupport {

  @BeforeEach
  protected void setUp() throws Exception {
    addOption("-Adoma.test=true");
  }

  @Test
  public void testSqlFileSelectEntity() throws Exception {
    Class<?> target = SqlFileSelectEntityDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSqlFileSelectMap() throws Exception {
    Class<?> target = SqlFileSelectMapDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSqlFileSelectDomain() throws Exception {
    Class<?> target = SqlFileSelectDomainDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSqlFileSelectBasic() throws Exception {
    Class<?> target = SqlFileSelectBasicDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAutoInsert() throws Exception {
    Class<?> target = AutoInsertDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSqlFileInsert() throws Exception {
    Class<?> target = SqlFileInsertDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSqlFileInsertEntity() throws Exception {
    Class<?> target = SqlFileInsertEntityDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAutoUpdate() throws Exception {
    Class<?> target = AutoUpdateDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAutoDelete() throws Exception {
    Class<?> target = AutoDeleteDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAnnotationNotFound() throws Exception {
    Class<?> target = AnnotationNotFoundDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4005);
  }

  @Test
  public void testNotInterface() throws Exception {
    Class<?> target = NotInterfaceDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4014);
  }

  @Test
  public void testNotTopLevel() throws Exception {
    Class<?> target = NotTopLevelDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4017);
  }

  @Test
  public void testNameUnsafe() throws Exception {
    Class<?> target = NameUnsafeDaoImpl.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
    assertMessage(Message.DOMA4026);
  }

  @Test
  public void testElementOfParamListUnspecified() throws Exception {
    Class<?> target = ElementOfParamListUnspecifiedDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4159);
  }

  @Test
  public void testElementOfParamListNotDomain() throws Exception {
    Class<?> target = ElementOfParamListWildcardTypeDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4160);
  }

  @Test
  public void testAutoBatchUpdate() throws Exception {
    Class<?> target = AutoBatchUpdateDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSqlFileBatchUpdate() throws Exception {
    Class<?> target = SqlFileBatchUpdateDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSqlFileBatchUpdateEntity() throws Exception {
    Class<?> target = SqlFileBatchUpdateEntityDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAutoFunction() throws Exception {
    Class<?> target = AutoFunctionDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAutoProcedure() throws Exception {
    Class<?> target = AutoProcedureDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testArrayFactory() throws Exception {
    Class<?> target = ArrayFactoryDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testBlobFactory() throws Exception {
    Class<?> target = BlobFactoryDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testClobFactory() throws Exception {
    Class<?> target = ClobFactoryDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testNClobFactory() throws Exception {
    Class<?> target = NClobFactoryDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testDaoExtends() throws Exception {
    Class<?> target = DaoExtendsDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testNoConfigDaoExtends() throws Exception {
    Class<?> target = NoConfigDaoExtendsDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testNoConfigDaoExtendsNoConfig() throws Exception {
    Class<?> target = NoConfigDaoExtendsNoConfigDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testNonDaoExtends() throws Exception {
    Class<?> target = NonDaoExtendsDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testIncludeAndExclude() throws Exception {
    Class<?> target = IncludeAndExcludeDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAnnotationConflicted() throws Exception {
    Class<?> target = AnnotationConflictedDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4087);
  }

  @Test
  public void testUnknownBindVariableSqlValidation() throws Exception {
    Class<?> target = UnknownBindVariableSqlValidationDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4092);
  }

  @Test
  public void testUnknownVariableSqlValidation() throws Exception {
    Class<?> target = UnknownVariableSqlValidationDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4092);
  }

  @Test
  public void testMethodAccessSqlValidation() throws Exception {
    Class<?> target = MethodAccessSqlValidationDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  public void testDomainParameter() throws Exception {
    Class<?> target = DomainParameterDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testMultiParamMethodAccessSqlValidation() throws Exception {
    Class<?> target = MultiParamMethodAccessSqlValidationDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  public void testEmbeddedVariable() throws Exception {
    Class<?> target = EmbeddedVariableDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  public void testEmptySqlFile() throws Exception {
    Class<?> target = EmptySqlFileDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4020);
  }

  @Test
  public void testNoTestLiteral() throws Exception {
    Class<?> target = NoTestLiteralDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4069);
  }

  @Test
  public void testEnum() throws Exception {
    Class<?> target = EnumDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAnnotateWith() throws Exception {
    Class<?> target = AnnotateWithDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testConfigAnnotateWith() throws Exception {
    Class<?> target = ConfigAnnotateWithDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testIllegalParameterName() throws Exception {
    Class<?> target = IllegalParameterNameDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4025);
  }

  @Test
  public void testPrimitiveType() throws Exception {
    Class<?> target = PrimitiveTypeDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSelectAbstractEntity() throws Exception {
    Class<?> target = SelectAbstractEntityDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4154);
  }

  @Test
  public void testSelectAbstractEntityList() throws Exception {
    Class<?> target = SelectAbstractEntityListDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4155);
  }

  @Test
  public void testFunctionAbstractEntityList() throws Exception {
    Class<?> target = FunctionAbstractEntityListDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4156);
  }

  @Test
  public void testProcedureAbstractEntityList() throws Exception {
    Class<?> target = ProcedureAbstractEntityListDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4157);
  }

  @Test
  public void testEmpDtoParameter() throws Exception {
    Class<?> target = EmpDtoParameterDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testPackagePrivate() throws Exception {
    Class<?> target = PackagePrivateDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAnnotationConfig() throws Exception {
    Class<?> target = AnnotationConfigDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testNoConfig() throws Exception {
    Class<?> target = NoConfigDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testEnsureResult() throws Exception {
    Class<?> target = EnsureResultDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testScript() throws Exception {
    Class<?> target = ScriptDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testFunction() throws Exception {
    Class<?> target = FunctionDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testProcedure() throws Exception {
    Class<?> target = ProcedureDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSqlValidationSkip() throws Exception {
    addOption("-Adoma.sql.validation=false");
    Class<?> target = SqlValidationSkipDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSqlValidationSkipWhenOptionSpecifiedByConfigFile() throws Exception {
    addOption("-Adoma.config.path=sql.validation.skip.config");
    Class<?> target = SqlValidationSkipDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testParameterizedParam() throws Exception {
    Class<?> target = ParameterizedParamDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testParameterizedReturn() throws Exception {
    Class<?> target = ParameterizedReturnDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testRawTypeReturn() throws Exception {
    Class<?> target = RawTypeReturnDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4206);
  }

  @Test
  public void testWildcardTypeReturn() throws Exception {
    Class<?> target = WildcardTypeReturnDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4207);
  }

  @Test
  public void testRawTypeParam() throws Exception {
    Class<?> target = RawTypeParamDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4208);
  }

  @Test
  public void testWildcardTypeParam() throws Exception {
    Class<?> target = WildcardTypeParamDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4209);
  }

  @Test
  public void testIterableRawTypeReturn() throws Exception {
    Class<?> target = IterableRawTypeReturnDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4210);
  }

  @Test
  public void testIterableWildcardTypeReturn() throws Exception {
    Class<?> target = IterableWildcardTypeReturnDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4211);
  }

  @Test
  public void testIterableRawTypeParam() throws Exception {
    Class<?> target = IterableRawTypeParamDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4212);
  }

  @Test
  public void testIterableWildcardTypeParam() throws Exception {
    Class<?> target = IterableWildcardTypeParamDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4213);
  }

  @Test
  public void testReferenceRawTypeParam() throws Exception {
    Class<?> target = ReferenceRawTypeParamDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4218);
  }

  @Test
  public void testReferenceWildcardTypeParam() throws Exception {
    Class<?> target = ReferenceWildcardTypeParamDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4219);
  }

  @Test
  public void testImmutableEmp() throws Exception {
    Class<?> target = ImmutableEmpDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testIllegalModifyImmutableEmp() throws Exception {
    Class<?> target = IllegalModifyImmutableEmpDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4222);
  }

  @Test
  public void testIllegalBatchModifyImmutableEmp() throws Exception {
    Class<?> target = IllegalBatchModifyImmutableEmpDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4223);
  }

  @Test
  public void testEnsureResultMapping() throws Exception {
    Class<?> target = EnsureResultMappingDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testOptionalParameter() throws Exception {
    Class<?> target = OptionalParameterDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testBasicResult() throws Exception {
    Class<?> target = BasicResultDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testDomainResult() throws Exception {
    Class<?> target = DomainResultDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testParameterizedDomainResult() throws Exception {
    Class<?> target = ParameterizedDomainResultDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testEntityResult() throws Exception {
    Class<?> target = EntityResultDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testMapResult() throws Exception {
    Class<?> target = MapResultDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAutoFunctionOptionalParameter() throws Exception {
    Class<?> target = AutoFunctionOptionalParameterDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testAutoProcedureOptionalParameter() throws Exception {
    Class<?> target = AutoProcedureOptionalParameterDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testStream() throws Exception {
    Class<?> target = StreamDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testStreamOptionalParameter() throws Exception {
    Class<?> target = StreamOptionalParameterDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testStaticMethod() throws Exception {
    Class<?> target = StaticMethodDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testPackageAccessLevel() throws Exception {
    Class<?> target = PackageAccessLevelDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testDefaultMethod() throws Exception {
    Class<?> target = DefaultMethodDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSingletonConfig() throws Exception {
    Class<?> target = SingletonConfigDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testOptionalInt() throws Exception {
    Class<?> target = OptionalIntDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testOptionalLong() throws Exception {
    Class<?> target = OptionalLongDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testOptionalDouble() throws Exception {
    Class<?> target = OptionalDoubleDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testCollector() throws Exception {
    Class<?> target = CollectorDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testCollectorOptionalParameter() throws Exception {
    Class<?> target = CollectorOptionalParameterDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSQLXMLFactory() throws Exception {
    Class<?> target = SQLXMLFactoryDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testOptionalEntityList() throws Exception {
    Class<?> target = OptionalEntityListDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4267);
  }

  @Test
  public void testOptionalMapList() throws Exception {
    Class<?> target = OptionalMapListDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4267);
  }

  @Test
  public void testIssue82() throws Exception {
    Class<?> target = Issue82Dao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testResultStream() throws Exception {
    Class<?> target = ResultStreamDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testPlainSingletonConfig() throws Exception {
    Class<?> target = PlainSingletonConfigDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSqlProcessor() throws Exception {
    Class<?> target = SqlProcessorDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testSqlProcessorBiFunction1stArgCheck() throws Exception {
    Class<?> target = SqlProcessorBiFunction1stArgCheckDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4437);
  }

  @Test
  public void testSqlProcessorBiFunction2ndArgCheck() throws Exception {
    Class<?> target = SqlProcessorBiFunction2ndArgCheckDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4435);
  }

  @Test
  public void testSqlProcessorNoBiFunction() throws Exception {
    Class<?> target = SqlProcessorNoBiFunctionDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4433);
  }

  @Test
  public void testSqlProcessorReturnType() throws Exception {
    Class<?> target = SqlProcessorReturnTypeDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4436);
  }

  @Test
  public void testSqlProcessorMultiBiFunctions() throws Exception {
    Class<?> target = SqlProcessorMultiBiFunctionsDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4434);
  }

  @Test
  public void testSqlProcessorRawType() throws Exception {
    Class<?> target = SqlProcessorRawTypeDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4438);
  }

  @Test
  public void testSqlProcessorWildcardType() throws Exception {
    Class<?> target = SqlProcessorWildcardTypeDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4439);
  }

  @Test
  public void testOnlyDefaultMethodsExtends() throws Exception {
    Class<?> target = OnlyDefaultMethodsExtendsDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  @Test
  public void testNotOnlyDefaultMethodsExtends() throws Exception {
    Class<?> target = NotOnlyDefaultMethodsExtendsDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4440);
  }

  @Test
  public void testMultiDaoExtends() throws Exception {
    Class<?> target = MultiDaoExtendsDao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4188);
  }

  @Test
  public void testIssue214() throws Exception {
    Class<?> target = Issue214Dao.class;
    DaoProcessor processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }
}
