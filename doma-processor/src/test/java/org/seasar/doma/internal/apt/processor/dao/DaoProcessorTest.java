/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.seasar.doma.internal.apt.DomaProcessor;
import org.seasar.doma.internal.apt.GeneratedClassNameParameterResolver;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.cdi.ApplicationScoped;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.internal.apt.processor.entity.Emp2;
import org.seasar.doma.internal.apt.processor.entity.ImmutableEmp;
import org.seasar.doma.internal.apt.processor.entity.ImmutableEmp2;
import org.seasar.doma.internal.apt.processor.entity.ImmutableUser;
import org.seasar.doma.internal.apt.processor.entity.ParentEntity;
import org.seasar.doma.internal.apt.processor.entity.UserAddress;
import org.seasar.doma.message.Message;

class DaoProcessorTest extends CompilerSupport {

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.test=true");

    addProcessor(new DomaProcessor());

    // Add the dependent entities
    addCompilationUnit(Emp.class);
    addCompilationUnit(Emp2.class);
    addCompilationUnit(ImmutableEmp.class);
    addCompilationUnit(ImmutableEmp2.class);
    addCompilationUnit(ImmutableUser.class);
    addCompilationUnit(Issue214Entity.class);
    addCompilationUnit(ParentEntity.class);

    // Add the dependent embeddables
    addCompilationUnit(UserAddress.class);

    // Add the dependent domains
    addCompilationUnit(PhoneNumber.class);
    addCompilationUnit(JobType.class);
    addCompilationUnit(Age.class);
    addCompilationUnit(Height.class);
  }

  @TestTemplate
  @ExtendWith(SuccessInvocationContextProvider.class)
  void success(
      Class<?>[] classes, URL expectedResourceUrl, String generatedClassName, String[] options)
      throws Exception {
    addOption(options);
    addCompilationUnit(classes);
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
          invocationContext(new Class[] {DaoExtendsDao.class, EmpDao.class}),
          invocationContext(new Class[] {NoConfigDaoExtendsDao.class, NoConfigEmpDao.class}),
          invocationContext(
              new Class[] {NoConfigDaoExtendsNoConfigDao.class, NoConfigEmpDao.class}),
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
          invocationContext(OptionalIntDao.class),
          invocationContext(OptionalLongDao.class),
          invocationContext(OptionalDoubleDao.class),
          invocationContext(CollectorDao.class),
          invocationContext(CollectorOptionalParameterDao.class),
          invocationContext(SQLXMLFactoryDao.class),
          invocationContext(Issue82Dao.class),
          invocationContext(ResultStreamDao.class),
          invocationContext(SqlProcessorDao.class),
          invocationContext(OnlyDefaultMethodsExtendsDao.class),
          invocationContext(
              ApplicationScopedDao.class,
              "-Adoma.cdi.ApplicationScoped=" + ApplicationScoped.class.getCanonicalName()),
          invocationContext(MultipleAnnotateWithDao.class),
          invocationContext(IgnoreGeneratedKeysDao.class),
          invocationContext(AutoMultiInsertDao.class));
    }

    private TestTemplateInvocationContext invocationContext(Class<?> clazz, String... options) {
      return invocationContext(new Class[] {clazz}, options);
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?>[] classArray, String... options) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return classArray[0].getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(classArray),
              new ResourceParameterResolver(classArray[0]),
              new GeneratedClassNameParameterResolver(classArray[0]),
              new SimpleParameterResolver(options));
        }
      };
    }
  }

  @TestTemplate
  @ExtendWith(ErrorInvocationContextProvider.class)
  void error(Class<?> clazz, Message message) throws Exception {
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
          invocationContext(MultiDaoExtendsDao.class, Message.DOMA4188),
          invocationContext(MultiInsertIllegalParameterTypeDao.class, Message.DOMA4042),
          invocationContext(MultiInsertIllegalParameterSizeDao.class, Message.DOMA4002),
          invocationContext(MultiInsertIllegalTypeArgumentDao.class, Message.DOMA4043),
          invocationContext(MultiInsertIllegalReturnTypeDao.class, Message.DOMA4001),
          invocationContext(
              MultiInsertIllegalReturnTypeForImmutableEntityDao.class, Message.DOMA4461),
          invocationContext(InsertIllegalDuplicateKeyDao.class, Message.DOMA4462),
          invocationContext(BatchInsertIllegalDuplicateKeyDao.class, Message.DOMA4462),
          invocationContext(MultiInsertIllegalDuplicateKeyDao.class, Message.DOMA4462));
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
