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

import org.seasar.doma.internal.apt.dao.AbstractImplementedByDao;
import org.seasar.doma.internal.apt.dao.AnnotationNotFoundDao;
import org.seasar.doma.internal.apt.dao.ArrayFactoryDao;
import org.seasar.doma.internal.apt.dao.AutoBatchUpdateDao;
import org.seasar.doma.internal.apt.dao.AutoDeleteDao;
import org.seasar.doma.internal.apt.dao.AutoFunctionDao;
import org.seasar.doma.internal.apt.dao.AutoInsertDao;
import org.seasar.doma.internal.apt.dao.AutoProcedureDao;
import org.seasar.doma.internal.apt.dao.AutoUpdateDao;
import org.seasar.doma.internal.apt.dao.ElementOfParamListNotDomainDao;
import org.seasar.doma.internal.apt.dao.ElementOfParamListUnspecifiedDao;
import org.seasar.doma.internal.apt.dao.GenericDaoEx;
import org.seasar.doma.internal.apt.dao.ImplementedByDao;
import org.seasar.doma.internal.apt.dao.InterfaceNotImplementedDao;
import org.seasar.doma.internal.apt.dao.IterationCallbackDao;
import org.seasar.doma.internal.apt.dao.NameUnsafeDao_;
import org.seasar.doma.internal.apt.dao.NotInterfaceDao;
import org.seasar.doma.internal.apt.dao.NotTopLevelDao;
import org.seasar.doma.internal.apt.dao.SqlFileBatchUpdateDao;
import org.seasar.doma.internal.apt.dao.SqlFileInsertDao;
import org.seasar.doma.internal.apt.dao.SqlFileSelectDomainDao;
import org.seasar.doma.internal.apt.dao.SqlFileSelectEntityDao;
import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.internal.apt.entity.Emp_;
import org.seasar.doma.message.MessageCode;

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

    public void testGenericDao() throws Exception {
        Class<?> target = GenericDaoEx.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        addCompilationUnit(Emp.class);
        addCompilationUnit(Emp_.class);
        compile();
        assertGeneratedSource(target);
        assertTrue(getCompiledResult());
    }

    public void testImplementedBy() throws Exception {
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(ImplementedByDao.class);
        addCompilationUnit(AbstractImplementedByDao.class);
        compile();
        assertGeneratedSource(ImplementedByDao.class);
        assertTrue(getCompiledResult());
    }

    public void testAnnotationNotFound() throws Exception {
        Class<?> target = AnnotationNotFoundDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4005);
    }

    public void testNotInterface() throws Exception {
        Class<?> target = NotInterfaceDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4014);
    }

    public void testNotTopLevel() throws Exception {
        Class<?> target = NotTopLevelDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4017);
    }

    // public void testIllegalDaoE4019_select() throws Exception {
    // Class<?> target = IllegalDaoE4019_select.class;
    // DaoProcessor processor = new DaoProcessor();
    // addProcessor(processor);
    // addCompilationUnit(target);
    // compile();
    // assertFalse(getCompiledResult());
    // assertMessageCode(MessageCode.E4019);
    // }
    //
    // public void testIllegalDaoE4019_insert() throws Exception {
    // Class<?> target = IllegalDaoE4019_insert.class;
    // DaoProcessor processor = new DaoProcessor();
    // addProcessor(processor);
    // addCompilationUnit(target);
    // compile();
    // assertFalse(getCompiledResult());
    // assertMessageCode(MessageCode.E4019);
    // }
    //
    // public void testIllegalDaoE4019_update() throws Exception {
    // Class<?> target = IllegalDaoE4019_update.class;
    // DaoProcessor processor = new DaoProcessor();
    // addProcessor(processor);
    // addCompilationUnit(target);
    // compile();
    // assertFalse(getCompiledResult());
    // assertMessageCode(MessageCode.E4019);
    // }
    //
    // public void testIllegalDaoE4019_delete() throws Exception {
    // Class<?> target = IllegalDaoE4019_delete.class;
    // DaoProcessor processor = new DaoProcessor();
    // addProcessor(processor);
    // addCompilationUnit(target);
    // compile();
    // assertFalse(getCompiledResult());
    // assertMessageCode(MessageCode.E4019);
    // }

    public void testInterfaceNotImplemented() throws Exception {
        Class<?> target = InterfaceNotImplementedDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4020);
    }

    public void testNameUnsafe() throws Exception {
        Class<?> target = NameUnsafeDao_.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4026);
    }

    public void testElementOfParamListUnspecified() throws Exception {
        Class<?> target = ElementOfParamListUnspecifiedDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4027);
    }

    public void testElementOfParamListNotDomain() throws Exception {
        Class<?> target = ElementOfParamListNotDomainDao.class;
        DaoProcessor processor = new DaoProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4028);
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
}
