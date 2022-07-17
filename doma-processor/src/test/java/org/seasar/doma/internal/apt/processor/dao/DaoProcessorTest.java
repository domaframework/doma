package org.seasar.doma.internal.apt.processor.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.GeneratedClassNameParameterResolver;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.cdi.ApplicationScoped;
import org.seasar.doma.internal.apt.processor.DaoProcessor;
import org.seasar.doma.message.Message;

class DaoProcessorTest extends CompilerSupport {

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.test=true");
  }

  @TestTemplate
  @ExtendWith(SuccessInvocationContextProvider.class)
  void success(Class<?> clazz, URL expectedResourceUrl, String generatedClassName, String[] options)
      throws Exception {
    addOption(options);
    addProcessor(new DaoProcessor());
    addCompilationUnit(clazz);
    compile();
    assertEqualsGeneratedSourceWithResource(expectedResourceUrl, generatedClassName);
    assertTrue(getCompiledResult());
  }

  static class SuccessInvocationContextProvider implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
      return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
        ExtensionContext context) {
      return Stream.of(
          invocationContext(VarArgsDao.class),
          invocationContext(NameUnsafeDaoImpl.class),
          invocationContext(MethodAccessSqlValidationDao.class),
          invocationContext(MultiParamMethodAccessSqlValidationDao.class),
          invocationContext(Issue214Dao.class),
          invocationContext(SqlValidationSkipDao.class, "-Adoma.sql.validation=false"),
          invocationContext(
              SqlValidationSkipWithConfigFileDao.class,
              "-Adoma.config.path=sql.validation.skip.config"),
          invocationContext(SqlFileSelectEntityDao.class),
          invocationContext(SqlFileSelectMapDao.class),
          invocationContext(SqlFileSelectDomainDao.class),
          invocationContext(SqlFileSelectBasicDao.class),
          invocationContext(AutoInsertDao.class),
          invocationContext(SqlFileInsertDao.class),
          invocationContext(SqlFileInsertEntityDao.class),
          invocationContext(AutoUpdateDao.class),
          invocationContext(AutoDeleteDao.class),
          invocationContext(AutoBatchUpdateDao.class),
          invocationContext(SqlFileBatchUpdateDao.class),
          invocationContext(SqlFileBatchUpdateEntityDao.class),
          invocationContext(AutoFunctionDao.class),
          invocationContext(AutoProcedureDao.class),
          invocationContext(ArrayFactoryDao.class),
          invocationContext(BlobFactoryDao.class),
          invocationContext(ClobFactoryDao.class),
          invocationContext(NClobFactoryDao.class),
          invocationContext(DaoExtendsDao.class),
          invocationContext(NoConfigDaoExtendsDao.class),
          invocationContext(NoConfigDaoExtendsNoConfigDao.class),
          invocationContext(NonDaoExtendsDao.class),
          invocationContext(IncludeAndExcludeDao.class),
          invocationContext(DomainParameterDao.class),
          invocationContext(EmbeddedVariableDao.class),
          invocationContext(EnumDao.class),
          invocationContext(AnnotateWithDao.class),
          invocationContext(ConfigAnnotateWithDao.class),
          invocationContext(PrimitiveTypeDao.class),
          invocationContext(EmpDtoParameterDao.class),
          invocationContext(PackagePrivateDao.class),
          invocationContext(AnnotationConfigDao.class),
          invocationContext(NoConfigDao.class),
          invocationContext(EnsureResultDao.class),
          invocationContext(ScriptDao.class),
          invocationContext(FunctionDao.class),
          invocationContext(ProcedureDao.class),
          invocationContext(ParameterizedParamDao.class),
          invocationContext(ParameterizedReturnDao.class),
          invocationContext(ImmutableEmpDao.class),
          invocationContext(EnsureResultMappingDao.class),
          invocationContext(OptionalParameterDao.class),
          invocationContext(BasicResultDao.class),
          invocationContext(DomainResultDao.class),
          invocationContext(ParameterizedDomainResultDao.class),
          invocationContext(EntityResultDao.class),
          invocationContext(MapResultDao.class),
          invocationContext(AutoFunctionOptionalParameterDao.class),
          invocationContext(AutoProcedureOptionalParameterDao.class),
          invocationContext(StreamDao.class),
          invocationContext(StreamOptionalParameterDao.class),
          invocationContext(StaticMethodDao.class),
          invocationContext(PackageAccessLevelDao.class),
          invocationContext(DefaultMethodDao.class),
          invocationContext(VirtualDefaultMethodDao.class),
          invocationContext(SingletonConfigDao.class),
          invocationContext(OptionalIntDao.class),
          invocationContext(OptionalLongDao.class),
          invocationContext(OptionalDoubleDao.class),
          invocationContext(CollectorDao.class),
          invocationContext(CollectorOptionalParameterDao.class),
          invocationContext(SQLXMLFactoryDao.class),
          invocationContext(Issue82Dao.class),
          invocationContext(ResultStreamDao.class),
          invocationContext(PlainSingletonConfigDao.class),
          invocationContext(SqlProcessorDao.class),
          invocationContext(OnlyDefaultMethodsExtendsDao.class),
          invocationContext(
              ApplicationScopedDao.class,
              "-Adoma.cdi.ApplicationScoped=" + ApplicationScoped.class.getCanonicalName()),
          invocationContext(MultipleAnnotateWithDao.class),
          invocationContext(IgnoreGeneratedKeysDao.class));
    }

    private TestTemplateInvocationContext invocationContext(Class<?> clazz, String... options) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return clazz.getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(clazz),
              new ResourceParameterResolver(clazz),
              new GeneratedClassNameParameterResolver(clazz),
              new SimpleParameterResolver(options));
        }
      };
    }
  }

  @TestTemplate
  @ExtendWith(ErrorInvocationContextProvider.class)
  void error(Class<?> clazz, Message message) throws Exception {
    addProcessor(new DaoProcessor());
    addCompilationUnit(clazz);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(message);
  }

  static class ErrorInvocationContextProvider implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
      return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
        ExtensionContext context) {
      return Stream.of(
          invocationContext(AnnotationNotFoundDao.class, Message.DOMA4005),
          invocationContext(NotInterfaceDao.class, Message.DOMA4014),
          invocationContext(EmptySqlFileDao.class, Message.DOMA4020),
          invocationContext(NotTopLevelDao.class, Message.DOMA4017),
          invocationContext(ElementOfParamListUnspecifiedDao.class, Message.DOMA4159),
          invocationContext(ElementOfParamListWildcardTypeDao.class, Message.DOMA4160),
          invocationContext(AnnotationConflictedDao.class, Message.DOMA4087),
          invocationContext(UnknownBindVariableSqlValidationDao.class, Message.DOMA4092),
          invocationContext(UnknownVariableSqlValidationDao.class, Message.DOMA4092),
          invocationContext(EmptySqlFileDao.class, Message.DOMA4020),
          invocationContext(NoTestLiteralDao.class, Message.DOMA4069),
          invocationContext(IllegalParameterNameDao.class, Message.DOMA4025),
          invocationContext(SelectAbstractEntityDao.class, Message.DOMA4154),
          invocationContext(SelectAbstractEntityListDao.class, Message.DOMA4155),
          invocationContext(FunctionAbstractEntityListDao.class, Message.DOMA4156),
          invocationContext(ProcedureAbstractEntityListDao.class, Message.DOMA4157),
          invocationContext(RawTypeReturnDao.class, Message.DOMA4206),
          invocationContext(WildcardTypeReturnDao.class, Message.DOMA4207),
          invocationContext(RawTypeParamDao.class, Message.DOMA4208),
          invocationContext(WildcardTypeParamDao.class, Message.DOMA4209),
          invocationContext(IterableRawTypeReturnDao.class, Message.DOMA4210),
          invocationContext(IterableWildcardTypeReturnDao.class, Message.DOMA4211),
          invocationContext(IterableRawTypeParamDao.class, Message.DOMA4212),
          invocationContext(IterableWildcardTypeParamDao.class, Message.DOMA4213),
          invocationContext(ReferenceRawTypeParamDao.class, Message.DOMA4218),
          invocationContext(ReferenceWildcardTypeParamDao.class, Message.DOMA4219),
          invocationContext(IllegalModifyImmutableEmpDao.class, Message.DOMA4222),
          invocationContext(IllegalBatchModifyImmutableEmpDao.class, Message.DOMA4223),
          invocationContext(OptionalEntityListDao.class, Message.DOMA4267),
          invocationContext(OptionalMapListDao.class, Message.DOMA4267),
          invocationContext(SqlProcessorBiFunction1stArgCheckDao.class, Message.DOMA4437),
          invocationContext(SqlProcessorBiFunction2ndArgCheckDao.class, Message.DOMA4435),
          invocationContext(SqlProcessorNoBiFunctionDao.class, Message.DOMA4433),
          invocationContext(SqlProcessorMultiBiFunctionsDao.class, Message.DOMA4434),
          invocationContext(SqlProcessorReturnTypeDao.class, Message.DOMA4436),
          invocationContext(SqlProcessorRawTypeDao.class, Message.DOMA4438),
          invocationContext(SqlProcessorWildcardTypeDao.class, Message.DOMA4439),
          invocationContext(NotOnlyDefaultMethodsExtendsDao.class, Message.DOMA4440),
          invocationContext(MultiDaoExtendsDao.class, Message.DOMA4188));
    }

    private TestTemplateInvocationContext invocationContext(Class<?> clazz, Message message) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return clazz.getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(clazz), new SimpleParameterResolver(message));
        }
      };
    }
  }
}
