package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.processor.DaoProcessor;
import org.seasar.doma.message.Message;

public class DaoProcessorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addOption("-Adoma.test=true");
  }

  public void testSqlTemplateSelectEntity() throws Exception {
    Class<?> target = SqlTemplateSelectEntityDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSqlTemplateSelectMap() throws Exception {
    Class<?> target = SqlTemplateSelectMapDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSqlTemplateSelectHolder() throws Exception {
    Class<?> target = SqlTemplateSelectHolderDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSqlTemplateSelectBasic() throws Exception {
    Class<?> target = SqlTemplateSelectBasicDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAutoInsert() throws Exception {
    Class<?> target = AutoInsertDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSqlTemplateInsert() throws Exception {
    Class<?> target = SqlTemplateInsertDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSqlTemplateInsertEntity() throws Exception {
    Class<?> target = SqlTemplateInsertEntityDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAutoUpdate() throws Exception {
    Class<?> target = AutoUpdateDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAutoDelete() throws Exception {
    Class<?> target = AutoDeleteDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAnnotationNotFound() throws Exception {
    Class<?> target = AnnotationNotFoundDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4005);
  }

  public void testNotInterface() throws Exception {
    Class<?> target = NotInterfaceDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4014);
  }

  public void testNotTopLevel() throws Exception {
    Class<?> target = NotTopLevelDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4017);
  }

  public void testNameUnsafe() throws Exception {
    Class<?> target = NameUnsafeDaoImpl.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
    assertMessage(Message.DOMA4026);
  }

  public void testElementOfParamListUnspecified() throws Exception {
    Class<?> target = ElementOfParamListUnspecifiedDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4159);
  }

  public void testElementOfParamListNotHolder() throws Exception {
    Class<?> target = ElementOfParamListWildcardTypeDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4160);
  }

  public void testAutoBatchUpdate() throws Exception {
    Class<?> target = AutoBatchUpdateDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSqlTemplateBatchUpdate() throws Exception {
    Class<?> target = SqlTemplateBatchUpdateDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSqlTemplateBatchUpdateEntity() throws Exception {
    Class<?> target = SqlTemplateBatchUpdateEntityDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAutoFunction() throws Exception {
    Class<?> target = AutoFunctionDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAutoProcedure() throws Exception {
    Class<?> target = AutoProcedureDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testArrayFactory() throws Exception {
    Class<?> target = ArrayFactoryDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testBlobFactory() throws Exception {
    Class<?> target = BlobFactoryDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testClobFactory() throws Exception {
    Class<?> target = ClobFactoryDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testNClobFactory() throws Exception {
    Class<?> target = NClobFactoryDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testDaoExtends() throws Exception {
    Class<?> target = DaoExtendsDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testNoConfigDaoExtends() throws Exception {
    Class<?> target = NoConfigDaoExtendsDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testNoConfigDaoExtendsNoConfig() throws Exception {
    Class<?> target = NoConfigDaoExtendsNoConfigDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testNonDaoExtends() throws Exception {
    Class<?> target = NonDaoExtendsDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testIncludeAndExclude() throws Exception {
    Class<?> target = IncludeAndExcludeDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAnnotationConflicted() throws Exception {
    Class<?> target = AnnotationConflictedDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4087);
  }

  public void testUnknownBindVariableSqlValidation() throws Exception {
    Class<?> target = UnknownBindVariableSqlValidationDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4092);
  }

  public void testUnknownVariableSqlValidation() throws Exception {
    Class<?> target = UnknownVariableSqlValidationDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4092);
  }

  public void testMethodAccessSqlValidation() throws Exception {
    Class<?> target = MethodAccessSqlValidationDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  public void testHolderParameter() throws Exception {
    Class<?> target = HolderParameterDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testMultiParamMethodAccessSqlValidation() throws Exception {
    Class<?> target = MultiParamMethodAccessSqlValidationDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  public void testEmbeddedVariable() throws Exception {
    Class<?> target = EmbeddedVariableDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  public void testEmptySqlTemplate() throws Exception {
    Class<?> target = EmptySqlTemplateDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4020);
  }

  public void testNoTestLiteral() throws Exception {
    Class<?> target = NoTestLiteralDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4069);
  }

  public void testEnum() throws Exception {
    Class<?> target = EnumDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAnnotateWith() throws Exception {
    Class<?> target = AnnotateWithDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testConfigAnnotateWith() throws Exception {
    Class<?> target = ConfigAnnotateWithDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testIllegalParameterName() throws Exception {
    Class<?> target = IllegalParameterNameDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4025);
  }

  public void testPrimitiveType() throws Exception {
    Class<?> target = PrimitiveTypeDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSelectAbstractEntity() throws Exception {
    Class<?> target = SelectAbstractEntityDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4154);
  }

  public void testSelectAbstractEntityList() throws Exception {
    Class<?> target = SelectAbstractEntityListDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4155);
  }

  public void testFunctionAbstractEntityList() throws Exception {
    Class<?> target = FunctionAbstractEntityListDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4156);
  }

  public void testProcedureAbstractEntityList() throws Exception {
    Class<?> target = ProcedureAbstractEntityListDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4157);
  }

  public void testEmpDtoParameter() throws Exception {
    Class<?> target = EmpDtoParameterDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testPackagePrivate() throws Exception {
    Class<?> target = PackagePrivateDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  public void testAnnotationConfig() throws Exception {
    Class<?> target = AnnotationConfigDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testNoConfig() throws Exception {
    Class<?> target = NoConfigDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testEnsureResult() throws Exception {
    Class<?> target = EnsureResultDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testScript() throws Exception {
    Class<?> target = ScriptDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testFunction() throws Exception {
    Class<?> target = FunctionDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testProcedure() throws Exception {
    Class<?> target = ProcedureDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSqlValidationSkip() throws Exception {
    addOption("-Adoma.sql.validation=false");
    Class<?> target = SqlValidationSkipDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSqlValidationSkipWhenOptionSpecifiedByConfigFile() throws Exception {
    addOption("-Adoma.config.path=sql.validation.skip.config");
    Class<?> target = SqlValidationSkipDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testParameterizedParam() throws Exception {
    Class<?> target = ParameterizedParamDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testParameterizedReturn() throws Exception {
    Class<?> target = ParameterizedReturnDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testRawTypeReturn() throws Exception {
    Class<?> target = RawTypeReturnDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4206);
  }

  public void testWildcardTypeReturn() throws Exception {
    Class<?> target = WildcardTypeReturnDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4207);
  }

  public void testRawTypeParam() throws Exception {
    Class<?> target = RawTypeParamDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4208);
  }

  public void testWildcardTypeParam() throws Exception {
    Class<?> target = WildcardTypeParamDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4209);
  }

  public void testIterableRawTypeReturn() throws Exception {
    Class<?> target = IterableRawTypeReturnDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4210);
  }

  public void testIterableWildcardTypeReturn() throws Exception {
    Class<?> target = IterableWildcardTypeReturnDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4211);
  }

  public void testIterableRawTypeParam() throws Exception {
    Class<?> target = IterableRawTypeParamDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4212);
  }

  public void testIterableWildcardTypeParam() throws Exception {
    Class<?> target = IterableWildcardTypeParamDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4213);
  }

  public void testReferenceRawTypeParam() throws Exception {
    Class<?> target = ReferenceRawTypeParamDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4218);
  }

  public void testReferenceWildcardTypeParam() throws Exception {
    Class<?> target = ReferenceWildcardTypeParamDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4219);
  }

  public void testImmutableEmp() throws Exception {
    Class<?> target = ImmutableEmpDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testIllegalModifyImmutableEmp() throws Exception {
    Class<?> target = IllegalModifyImmutableEmpDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4222);
  }

  public void testIllegalBatchModifyImmutableEmp() throws Exception {
    Class<?> target = IllegalBatchModifyImmutableEmpDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4223);
  }

  public void testEnsureResultMapping() throws Exception {
    Class<?> target = EnsureResultMappingDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testOptionalParameter() throws Exception {
    Class<?> target = OptionalParameterDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testBasicResult() throws Exception {
    Class<?> target = BasicResultDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testHolderResult() throws Exception {
    Class<?> target = HolderResultDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testParameterizedHolderResult() throws Exception {
    Class<?> target = ParameterizedHolderResultDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testEntityResult() throws Exception {
    Class<?> target = EntityResultDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testMapResult() throws Exception {
    Class<?> target = MapResultDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAutoFunctionOptionalParameter() throws Exception {
    Class<?> target = AutoFunctionOptionalParameterDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testAutoProcedureOptionalParameter() throws Exception {
    Class<?> target = AutoProcedureOptionalParameterDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testStream() throws Exception {
    Class<?> target = StreamDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testStreamOptionalParameter() throws Exception {
    Class<?> target = StreamOptionalParameterDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testStaticMethod() throws Exception {
    Class<?> target = StaticMethodDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testPackageAccessLevel() throws Exception {
    Class<?> target = PackageAccessLevelDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testDefaultMethod() throws Exception {
    Class<?> target = DefaultMethodDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSingletonConfig() throws Exception {
    Class<?> target = SingletonConfigDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testOptionalInt() throws Exception {
    Class<?> target = OptionalIntDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testOptionalLong() throws Exception {
    Class<?> target = OptionalLongDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testOptionalDouble() throws Exception {
    Class<?> target = OptionalDoubleDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testCollector() throws Exception {
    Class<?> target = CollectorDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testCollectorOptionalParameter() throws Exception {
    Class<?> target = CollectorOptionalParameterDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSQLXMLFactory() throws Exception {
    Class<?> target = SQLXMLFactoryDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testOptionalEntityList() throws Exception {
    Class<?> target = OptionalEntityListDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4267);
  }

  public void testOptionalMapList() throws Exception {
    Class<?> target = OptionalMapListDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4267);
  }

  public void testIssue82() throws Exception {
    Class<?> target = Issue82Dao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testResultStream() throws Exception {
    Class<?> target = ResultStreamDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testPlainSingletonConfig() throws Exception {
    Class<?> target = PlainSingletonConfigDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSqlProcessor() throws Exception {
    Class<?> target = SqlProcessorDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testSqlProcessorBiFunction1stArgCheck() throws Exception {
    Class<?> target = SqlProcessorBiFunction1stArgCheckDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4437);
  }

  public void testSqlProcessorBiFunction2ndArgCheck() throws Exception {
    Class<?> target = SqlProcessorBiFunction2ndArgCheckDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4435);
  }

  public void testSqlProcessorNoBiFunction() throws Exception {
    Class<?> target = SqlProcessorNoBiFunctionDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4433);
  }

  public void testSqlProcessorReturnType() throws Exception {
    Class<?> target = SqlProcessorReturnTypeDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4436);
  }

  public void testSqlProcessorMultiBiFunctions() throws Exception {
    Class<?> target = SqlProcessorMultiBiFunctionsDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4434);
  }

  public void testSqlProcessorRawType() throws Exception {
    Class<?> target = SqlProcessorRawTypeDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4438);
  }

  public void testSqlProcessorWildcardType() throws Exception {
    Class<?> target = SqlProcessorWildcardTypeDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4439);
  }

  public void testOnlyDefaultMethodsExtends() throws Exception {
    Class<?> target = OnlyDefaultMethodsExtendsDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertGeneratedSource(target);
    assertTrue(getCompiledResult());
  }

  public void testNotOnlyDefaultMethodsExtends() throws Exception {
    Class<?> target = NotOnlyDefaultMethodsExtendsDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4440);
  }

  public void testMultiDaoExtends() throws Exception {
    Class<?> target = MultiDaoExtendsDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4188);
  }

  public void testPrivateMethod() throws Exception {
    Class<?> target = PrivateMethodDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }

  public void testIllegalSqlAnnotation() throws Exception {
    Class<?> target = IllegalSqlAnnotationDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4441);
  }

  public void testSqlAnnotationOnDefaultMethod() throws Exception {
    Class<?> target = SqlAnnotationOnDefaultMethodDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4442);
  }

  public void testIllegalSqlAnnotationCombination() throws Exception {
    Class<?> target = IllegalSqlAnnotationCombinationDao.class;
    var processor = new DaoProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4443);
  }
}
