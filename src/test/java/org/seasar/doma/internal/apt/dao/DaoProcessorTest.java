/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.DaoProcessor;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class DaoProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addOption("-Adoma.test=true");
    }

    public void testSqlFileSelectEntity() throws Exception {
        Class<?> target = SqlFileSelectEntityDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testSqlFileSelectMap() throws Exception {
        Class<?> target = SqlFileSelectMapDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testSqlFileSelectDomain() throws Exception {
        Class<?> target = SqlFileSelectDomainDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testSqlFileSelectBasic() throws Exception {
        Class<?> target = SqlFileSelectBasicDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAutoInsert() throws Exception {
        Class<?> target = AutoInsertDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testSqlFileInsert() throws Exception {
        Class<?> target = SqlFileInsertDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testSqlFileInsertEntity() throws Exception {
        Class<?> target = SqlFileInsertEntityDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAutoUpdate() throws Exception {
        Class<?> target = AutoUpdateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAutoDelete() throws Exception {
        Class<?> target = AutoDeleteDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAnnotationNotFound() throws Exception {
        Class<?> target = AnnotationNotFoundDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4005);
    }

    public void testNotInterface() throws Exception {
        Class<?> target = NotInterfaceDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4014);
    }

    public void testNotTopLevel() throws Exception {
        Class<?> target = NotTopLevelDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4017);
    }

    public void testNameUnsafe() throws Exception {
        Class<?> target = NameUnsafeDaoImpl.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
        assertMessage(Message.DOMA4026);
    }

    public void testElementOfParamListUnspecified() throws Exception {
        Class<?> target = ElementOfParamListUnspecifiedDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4159);
    }

    public void testElementOfParamListNotDomain() throws Exception {
        Class<?> target = ElementOfParamListWildcardTypeDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4160);
    }

    public void testAutoBatchUpdate() throws Exception {
        Class<?> target = AutoBatchUpdateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testSqlFileBatchUpdate() throws Exception {
        Class<?> target = SqlFileBatchUpdateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testSqlFileBatchUpdateEntity() throws Exception {
        Class<?> target = SqlFileBatchUpdateEntityDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAutoFunction() throws Exception {
        Class<?> target = AutoFunctionDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAutoProcedure() throws Exception {
        Class<?> target = AutoProcedureDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testArrayFactory() throws Exception {
        Class<?> target = ArrayFactoryDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testBlobFactory() throws Exception {
        Class<?> target = BlobFactoryDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testClobFactory() throws Exception {
        Class<?> target = ClobFactoryDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testNClobFactory() throws Exception {
        Class<?> target = NClobFactoryDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testDaoExtends() throws Exception {
        Class<?> target = DaoExtendsDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testNoConfigDaoExtends() throws Exception {
        Class<?> target = NoConfigDaoExtendsDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testNoConfigDaoExtendsNoConfig() throws Exception {
        Class<?> target = NoConfigDaoExtendsNoConfigDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testNonDaoExtends() throws Exception {
        Class<?> target = NonDaoExtendsDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4188);
    }

    public void testIncludeAndExclude() throws Exception {
        Class<?> target = IncludeAndExcludeDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAnnotationConflicted() throws Exception {
        Class<?> target = AnnotationConflictedDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4087);
    }

    public void testUnknownBindVariableSqlValidation() throws Exception {
        Class<?> target = UnknownBindVariableSqlValidationDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4092);
    }

    public void testUnknownVariableSqlValidation() throws Exception {
        Class<?> target = UnknownVariableSqlValidationDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4092);
    }

    public void testMethodAccessSqlValidation() throws Exception {
        Class<?> target = MethodAccessSqlValidationDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
    }

    public void testDomainParameter() throws Exception {
        Class<?> target = DomainParameterDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testMultiParamMethodAccessSqlValidation() throws Exception {
        Class<?> target = MultiParamMethodAccessSqlValidationDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
    }

    public void testEmbeddedVariable() throws Exception {
        Class<?> target = EmbeddedVariableDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
    }

    public void testEmptySqlFile() throws Exception {
        Class<?> target = EmptySqlFileDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4020);
    }

    public void testNoTestLiteral() throws Exception {
        Class<?> target = NoTestLiteralDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4069);
    }

    public void testEnum() throws Exception {
        Class<?> target = EnumDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAnnotateWith() throws Exception {
        Class<?> target = AnnotateWithDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testConfigAnnotateWith() throws Exception {
        Class<?> target = ConfigAnnotateWithDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testIllegalParameterName() throws Exception {
        Class<?> target = IllegalParameterNameDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4025);
    }

    public void testPrimitiveType() throws Exception {
        Class<?> target = PrimitiveTypeDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testSelectAbstractEntity() throws Exception {
        Class<?> target = SelectAbstractEntityDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4154);
    }

    public void testSelectAbstractEntityList() throws Exception {
        Class<?> target = SelectAbstractEntityListDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4155);
    }

    public void testFunctionAbstractEntityList() throws Exception {
        Class<?> target = FunctionAbstractEntityListDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4156);
    }

    public void testProcedureAbstractEntityList() throws Exception {
        Class<?> target = ProcedureAbstractEntityListDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4157);
    }

    public void testEmpDtoParameter() throws Exception {
        Class<?> target = EmpDtoParameterDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testPackagePrivate() throws Exception {
        Class<?> target = PackagePrivateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
    }

    public void testAnnotationConfig() throws Exception {
        Class<?> target = AnnotationConfigDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testNoConfig() throws Exception {
        Class<?> target = NoConfigDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testEnsureResult() throws Exception {
        Class<?> target = EnsureResultDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testScript() throws Exception {
        Class<?> target = ScriptDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testFunction() throws Exception {
        Class<?> target = FunctionDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testProcedure() throws Exception {
        Class<?> target = ProcedureDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

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

    public void testParameterizedParam() throws Exception {
        Class<?> target = ParameterizedParamDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testParameterizedReturn() throws Exception {
        Class<?> target = ParameterizedReturnDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testRawTypeReturn() throws Exception {
        Class<?> target = RawTypeReturnDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4206);
    }

    public void testWildcardTypeReturn() throws Exception {
        Class<?> target = WildcardTypeReturnDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4207);
    }

    public void testRawTypeParam() throws Exception {
        Class<?> target = RawTypeParamDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4208);
    }

    public void testWildcardTypeParam() throws Exception {
        Class<?> target = WildcardTypeParamDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4209);
    }

    public void testIterableRawTypeReturn() throws Exception {
        Class<?> target = IterableRawTypeReturnDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4210);
    }

    public void testIterableWildcardTypeReturn() throws Exception {
        Class<?> target = IterableWildcardTypeReturnDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4211);
    }

    public void testIterableRawTypeParam() throws Exception {
        Class<?> target = IterableRawTypeParamDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4212);
    }

    public void testIterableWildcardTypeParam() throws Exception {
        Class<?> target = IterableWildcardTypeParamDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4213);
    }

    public void testReferenceRawTypeParam() throws Exception {
        Class<?> target = ReferenceRawTypeParamDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4218);
    }

    public void testReferenceWildcardTypeParam() throws Exception {
        Class<?> target = ReferenceWildcardTypeParamDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4219);
    }

    public void testImmutableEmp() throws Exception {
        Class<?> target = ImmutableEmpDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testIllegalModifyImmutableEmp() throws Exception {
        Class<?> target = IllegalModifyImmutableEmpDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4222);
    }

    public void testIllegalBatchModifyImmutableEmp() throws Exception {
        Class<?> target = IllegalBatchModifyImmutableEmpDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4223);
    }

    public void testEnsureResultMapping() throws Exception {
        Class<?> target = EnsureResultMappingDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOptionalParameter() throws Exception {
        Class<?> target = OptionalParameterDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testBasicResult() throws Exception {
        Class<?> target = BasicResultDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testDomainResult() throws Exception {
        Class<?> target = DomainResultDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testParameterizedDomainResult() throws Exception {
        Class<?> target = ParameterizedDomainResultDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testEntityResult() throws Exception {
        Class<?> target = EntityResultDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testMapResult() throws Exception {
        Class<?> target = MapResultDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAutoFunctionOptionalParameter() throws Exception {
        Class<?> target = AutoFunctionOptionalParameterDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testAutoProcedureOptionalParameter() throws Exception {
        Class<?> target = AutoProcedureOptionalParameterDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testStream() throws Exception {
        Class<?> target = StreamDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testStreamOptionalParameter() throws Exception {
        Class<?> target = StreamOptionalParameterDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testStaticMethod() throws Exception {
        Class<?> target = StaticMethodDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testPackageAccessLevel() throws Exception {
        Class<?> target = PackageAccessLevelDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testDefaultMethod() throws Exception {
        Class<?> target = DefaultMethodDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testSingletonConfig() throws Exception {
        Class<?> target = SingletonConfigDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOptionalInt() throws Exception {
        Class<?> target = OptionalIntDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOptionalLong() throws Exception {
        Class<?> target = OptionalLongDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOptionalDouble() throws Exception {
        Class<?> target = OptionalDoubleDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testCollector() throws Exception {
        Class<?> target = CollectorDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testCollectorOptionalParameter() throws Exception {
        Class<?> target = CollectorOptionalParameterDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testSQLXMLFactory() throws Exception {
        Class<?> target = SQLXMLFactoryDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testOptionalEntityList() throws Exception {
        Class<?> target = OptionalEntityListDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4267);
    }

    public void testOptionalMapList() throws Exception {
        Class<?> target = OptionalMapListDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessage(Message.DOMA4267);
    }

}
