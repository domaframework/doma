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

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

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
import org.seasar.doma.internal.apt.dao.DelegateDao;
import org.seasar.doma.internal.apt.dao.ElementOfParamListNotDomainDao;
import org.seasar.doma.internal.apt.dao.ElementOfParamListUnspecifiedDao;
import org.seasar.doma.internal.apt.dao.ExtendsDao;
import org.seasar.doma.internal.apt.dao.IllegalConstructorDelegateDao;
import org.seasar.doma.internal.apt.dao.IllegalMethodDelegateDao;
import org.seasar.doma.internal.apt.dao.IncludeAndExcludeDao;
import org.seasar.doma.internal.apt.dao.IterationCallbackDao;
import org.seasar.doma.internal.apt.dao.NClobFactoryDao;
import org.seasar.doma.internal.apt.dao.NameUnsafeDao_;
import org.seasar.doma.internal.apt.dao.NotInterfaceDao;
import org.seasar.doma.internal.apt.dao.NotTopLevelDao;
import org.seasar.doma.internal.apt.dao.SqlFileBatchUpdateDao;
import org.seasar.doma.internal.apt.dao.SqlFileInsertDao;
import org.seasar.doma.internal.apt.dao.SqlFileSelectDomainDao;
import org.seasar.doma.internal.apt.dao.SqlFileSelectEntityDao;
import org.seasar.doma.internal.apt.dao.SqlValidationDao;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class DaoProcessorTest extends AptTestCase {

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
        assertMessageCode(DomaMessageCode.DOMA4005);
    }

    public void testNotInterface() throws Exception {
        Class<?> target = NotInterfaceDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4014);
    }

    public void testNotTopLevel() throws Exception {
        Class<?> target = NotTopLevelDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4017);
    }

    public void testNameUnsafe() throws Exception {
        Class<?> target = NameUnsafeDao_.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4026);
    }

    public void testElementOfParamListUnspecified() throws Exception {
        Class<?> target = ElementOfParamListUnspecifiedDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4027);
    }

    public void testElementOfParamListNotDomain() throws Exception {
        Class<?> target = ElementOfParamListNotDomainDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4028);
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
        assertMessageCode(DomaMessageCode.DOMA4045);
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

    public void testIllegalConstructorDelegate() throws Exception {
        Class<?> target = IllegalConstructorDelegateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4080);
    }

    public void testIllegalMethodDelegate() throws Exception {
        Class<?> target = IllegalMethodDelegateDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(DomaMessageCode.DOMA4081);
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
        assertMessageCode(DomaMessageCode.DOMA4086);
    }

    public void testSqlValidationDao() throws Exception {
        Class<?> target = SqlValidationDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        for (Diagnostic<? extends JavaFileObject> diagnostic : getDiagnostics()) {
            assertEquals(DomaMessageCode.DOMA4092,
                    extractMessageCode(diagnostic));
        }
    }
}
