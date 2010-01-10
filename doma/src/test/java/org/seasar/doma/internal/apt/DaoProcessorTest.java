/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.apt;

import org.seasar.doma.internal.apt.dao.AnnotateWithDao;
import org.seasar.doma.internal.apt.dao.AnnotationConflictedDao;
import org.seasar.doma.internal.apt.dao.AnnotationNotFoundDao;
import org.seasar.doma.internal.apt.dao.ArrayFactoryDao;
import org.seasar.doma.internal.apt.dao.AutoBatchUpdateDao;
import org.seasar.doma.internal.apt.dao.AutoDeleteDao;
import org.seasar.doma.internal.apt.dao.AutoFunctionDao;
import org.seasar.doma.internal.apt.dao.AutoInsertDao;
import org.seasar.doma.internal.apt.dao.AutoProcedureDao;
import org.seasar.doma.internal.apt.dao.AutoUpdateDao;
import org.seasar.doma.internal.apt.dao.BlobFactoryDao;
import org.seasar.doma.internal.apt.dao.ClobFactoryDao;
import org.seasar.doma.internal.apt.dao.ConfigProxyDao;
import org.seasar.doma.internal.apt.dao.DaoAwareDelegateDao;
import org.seasar.doma.internal.apt.dao.DelegateDao;
import org.seasar.doma.internal.apt.dao.DomainParameterDao;
import org.seasar.doma.internal.apt.dao.ElementOfParamListUnspecifiedDao;
import org.seasar.doma.internal.apt.dao.ElementOfParamListWildcardTypeDao;
import org.seasar.doma.internal.apt.dao.EmbeddedVariableDao;
import org.seasar.doma.internal.apt.dao.EmptySqlFileDao;
import org.seasar.doma.internal.apt.dao.EnumDao;
import org.seasar.doma.internal.apt.dao.ExtendsDao;
import org.seasar.doma.internal.apt.dao.FunctionAbstractEntityListDao;
import org.seasar.doma.internal.apt.dao.IllegalAnnotateWithDao;
import org.seasar.doma.internal.apt.dao.IllegalConstructorDelegateDao;
import org.seasar.doma.internal.apt.dao.IllegalParameterNameDao;
import org.seasar.doma.internal.apt.dao.IllegalParameterTypeDelegateDao;
import org.seasar.doma.internal.apt.dao.IllegalReturnTypeDelegateDao;
import org.seasar.doma.internal.apt.dao.IncludeAndExcludeDao;
import org.seasar.doma.internal.apt.dao.IterateAbstractEntityDao;
import org.seasar.doma.internal.apt.dao.IterationCallbackDao;
import org.seasar.doma.internal.apt.dao.MethodAccessSqlValidationDao;
import org.seasar.doma.internal.apt.dao.MultiParamMethodAccessSqlValidationDao;
import org.seasar.doma.internal.apt.dao.NClobFactoryDao;
import org.seasar.doma.internal.apt.dao.NameUnsafeDaoImpl;
import org.seasar.doma.internal.apt.dao.NoMethodDelegateDao;
import org.seasar.doma.internal.apt.dao.NoTestLiteralDao;
import org.seasar.doma.internal.apt.dao.NotInterfaceDao;
import org.seasar.doma.internal.apt.dao.NotTopLevelDao;
import org.seasar.doma.internal.apt.dao.PrimitiveTypeDao;
import org.seasar.doma.internal.apt.dao.ProcedureAbstractEntityListDao;
import org.seasar.doma.internal.apt.dao.SelectAbstractEntityDao;
import org.seasar.doma.internal.apt.dao.SelectAbstractEntityListDao;
import org.seasar.doma.internal.apt.dao.SqlFileBatchUpdateDao;
import org.seasar.doma.internal.apt.dao.SqlFileInsertDao;
import org.seasar.doma.internal.apt.dao.SqlFileSelectBasicDao;
import org.seasar.doma.internal.apt.dao.SqlFileSelectDomainDao;
import org.seasar.doma.internal.apt.dao.SqlFileSelectEntityDao;
import org.seasar.doma.internal.apt.dao.UnknownBindVariableSqlValidationDao;
import org.seasar.doma.internal.apt.dao.UnknownVariableSqlValidationDao;
import org.seasar.doma.internal.message.Message;

/**
 * @author taedium
 * 
 */
public class DaoProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addOption("-Atest=true");
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
        assertMessageCode(Message.DOMA4005);
    }

    public void testNotInterface() throws Exception {
        Class<?> target = NotInterfaceDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4014);
    }

    public void testNotTopLevel() throws Exception {
        Class<?> target = NotTopLevelDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4017);
    }

    public void testNameUnsafe() throws Exception {
        Class<?> target = NameUnsafeDaoImpl.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
        assertMessageCode(Message.DOMA4026);
    }

    public void testElementOfParamListUnspecified() throws Exception {
        Class<?> target = ElementOfParamListUnspecifiedDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4108);
    }

    public void testElementOfParamListNotDomain() throws Exception {
        Class<?> target = ElementOfParamListWildcardTypeDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4112);
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

    public void testIterationCallback() throws Exception {
        Class<?> target = IterationCallbackDao.class;
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

    public void testExtendsDao() throws Exception {
        Class<?> target = ExtendsDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4045);
    }

    public void testDelegate() throws Exception {
        Class<?> target = DelegateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testDaoAwareDelegate() throws Exception {
        Class<?> target = DaoAwareDelegateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testIllegalConstructorDelegate() throws Exception {
        Class<?> target = IllegalConstructorDelegateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4080);
    }

    public void testNoMethodDelegate() throws Exception {
        Class<?> target = NoMethodDelegateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4081);
    }

    public void testIllegalReturnTypeDelegate() throws Exception {
        Class<?> target = IllegalReturnTypeDelegateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4081);
    }

    public void testIllegalParameterTypeDelegate() throws Exception {
        Class<?> target = IllegalParameterTypeDelegateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4081);
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
        assertMessageCode(Message.DOMA4086);
    }

    public void testUnknownBindVariableSqlValidation() throws Exception {
        Class<?> target = UnknownBindVariableSqlValidationDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4092);
    }

    public void testUnknownVariableSqlValidation() throws Exception {
        Class<?> target = UnknownVariableSqlValidationDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4092);
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
        assertMessageCode(Message.DOMA4020);
    }

    public void testNoTestLiteral() throws Exception {
        Class<?> target = NoTestLiteralDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4069);
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

    public void testConfigProxy() throws Exception {
        Class<?> target = ConfigProxyDao.class;
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

    public void testIllegalAnnotateWith() throws Exception {
        Class<?> target = IllegalAnnotateWithDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4142);
    }

    public void testIllegalParameterName() throws Exception {
        Class<?> target = IllegalParameterNameDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4025);
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
        assertMessageCode(Message.DOMA4154);
    }

    public void testSelectAbstractEntityList() throws Exception {
        Class<?> target = SelectAbstractEntityListDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4155);
    }

    public void testFunctionAbstractEntityList() throws Exception {
        Class<?> target = FunctionAbstractEntityListDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4156);
    }

    public void testProcedureAbstractEntityList() throws Exception {
        Class<?> target = ProcedureAbstractEntityListDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4157);
    }

    public void testIterateAbstractEntity() throws Exception {
        Class<?> target = IterateAbstractEntityDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(Message.DOMA4158);
    }
}
