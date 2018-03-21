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
package org.seasar.doma.jdbc.query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.TenantIdPropertyType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

import example.entity.Emp;
import example.entity._Emp;
import junit.framework.TestCase;

/**
 * @author nakamura-to
 * 
 */
public class SqlFileUpdateQueryTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testPopulate() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setVersion(100);

        SqlFileUpdateQuery query = new SqlFileUpdateQuery();
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setSqlFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileUpdateQueryTest/testPopulate.sql");
        query.setConfig(runtimeConfig);
        query.setEntityAndEntityType("emp", emp, _Emp.getSingletonInternal());
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.addParameter("emp", Emp.class, emp);
        query.prepare();

        UpdateQuery updateQuery = query;
        PreparedSql sql = updateQuery.getSql();
        assertEquals(
                "update aaa set NAME = ?, SALARY = ?, VERSION = ? + 1 where id = ?",
                sql.getRawSql());
        assertEquals(
                "update aaa set NAME = 'aaa', SALARY = null, VERSION = 100 + 1 where id = 10",
                sql.getFormattedSql());
        List<? extends InParameter<?>> parameters = sql.getParameters();
        assertEquals(4, parameters.size());
        assertEquals("aaa", parameters.get(0).getWrapper().get());
        assertEquals(null, parameters.get(1).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(2).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(3).getWrapper().get());
        assertTrue(query.isExecutable());
    }

    public void testPopulate_states() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setVersion(100);
        emp.originalStates = new Emp();

        SqlFileUpdateQuery query = new SqlFileUpdateQuery();
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setSqlFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileUpdateQueryTest/testPopulate.sql");
        query.setConfig(runtimeConfig);
        query.setEntityAndEntityType("emp", emp, _Emp.getSingletonInternal());
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.addParameter("emp", Emp.class, emp);
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?",
                sql.getRawSql());

        List<InParameter<?>> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals("aaa", parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
        assertTrue(query.isExecutable());
    }

    public void testPopulate_excludeNull() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("hoge");
        emp.setVersion(100);

        SqlFileUpdateQuery query = new SqlFileUpdateQuery();
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setSqlFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileUpdateQueryTest/testPopulate.sql");
        query.setConfig(runtimeConfig);
        query.setEntityAndEntityType("emp", emp, _Emp.getSingletonInternal());
        query.setNullExcluded(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.addParameter("emp", Emp.class, emp);
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?",
                sql.getRawSql());
        List<InParameter<?>> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals("hoge", parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
        assertTrue(query.isExecutable());
    }

    public void testPopulate_excludeNull_updateNullableInPreUpdate()
            throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setVersion(100);

        SqlFileUpdateQuery query = new SqlFileUpdateQuery();
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setSqlFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileUpdateQueryTest/testPopulate.sql");
        query.setConfig(runtimeConfig);
        query.setEntityAndEntityType("emp", emp,
                new PreUpdate(_Emp.getSingletonInternal()));
        query.setNullExcluded(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.addParameter("emp", Emp.class, emp);
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?",
                sql.getRawSql());
        List<InParameter<?>> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals("hoge", parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
        assertTrue(query.isExecutable());
    }

    public void testPopulate_ignoreVersion() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setVersion(100);
        emp.originalStates = new Emp();

        SqlFileUpdateQuery query = new SqlFileUpdateQuery();
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setSqlFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileUpdateQueryTest/testPopulate.sql");
        query.setConfig(runtimeConfig);
        query.setEntityAndEntityType("emp", emp, _Emp.getSingletonInternal());
        query.setVersionIgnored(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.addParameter("emp", Emp.class, emp);
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals("update aaa set NAME = ?, VERSION = ? where id = ?",
                sql.getRawSql());
        List<InParameter<?>> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals("aaa", parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
        assertTrue(query.isExecutable());
    }

    public void testPopulate_include() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setSalary(new BigDecimal(200));
        emp.setVersion(100);

        SqlFileUpdateQuery query = new SqlFileUpdateQuery();
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setSqlFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileUpdateQueryTest/testPopulate.sql");
        query.setConfig(runtimeConfig);
        query.setEntityAndEntityType("emp", emp, _Emp.getSingletonInternal());
        query.setIncludedPropertyNames("name");
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.addParameter("emp", Emp.class, emp);
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?",
                sql.getRawSql());
        List<InParameter<?>> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals("aaa", parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
        assertTrue(query.isExecutable());
    }

    public void testPopulate_exclude() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setSalary(new BigDecimal(200));
        emp.setVersion(100);

        SqlFileUpdateQuery query = new SqlFileUpdateQuery();
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setSqlFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileUpdateQueryTest/testPopulate.sql");
        query.setConfig(runtimeConfig);
        query.setEntityAndEntityType("emp", emp, _Emp.getSingletonInternal());
        query.setExcludedPropertyNames("name");
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.addParameter("emp", Emp.class, emp);
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals("update aaa set SALARY = ?, VERSION = ? + 1 where id = ?",
                sql.getRawSql());
        List<InParameter<?>> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals(new BigDecimal(200), parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertTrue(query.isExecutable());
    }

    public void testPopulate_IsExecutable() throws Exception {
        Emp emp = new Emp();
        emp.originalStates = new Emp();

        SqlFileUpdateQuery query = new SqlFileUpdateQuery();
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setSqlFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileUpdateQueryTest/testPopulate.sql");
        query.setConfig(runtimeConfig);
        query.setEntityAndEntityType("emp", emp, _Emp.getSingletonInternal());
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.addParameter("emp", Emp.class, emp);
        query.prepare();

        assertFalse(query.isExecutable());
    }

    public void testNonEntity() throws Exception {
        SqlFileUpdateQuery query = new SqlFileUpdateQuery();
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setSqlFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileUpdateQueryTest/testNonEntity.sql");
        query.setConfig(runtimeConfig);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.addParameter("id", Integer.class, Integer.valueOf(10));
        query.addParameter("name", String.class, "aaa");
        query.addParameter("version", Integer.class, Integer.valueOf(100));
        query.prepare();

        UpdateQuery updateQuery = query;
        PreparedSql sql = updateQuery.getSql();
        assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?",
                sql.getRawSql());
        assertEquals(
                "update aaa set NAME = 'aaa', VERSION = 100 + 1 where id = 10",
                sql.getFormattedSql());
        List<? extends InParameter<?>> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals("aaa", parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
        assertTrue(query.isExecutable());
    }

    public void testOriginalStates_unchanged() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setSalary(BigDecimal.ZERO);
        emp.setVersion(100);
        emp.originalStates = new Emp();
        emp.originalStates.setId(emp.getId());
        emp.originalStates.setName(emp.getName());
        emp.originalStates.setSalary(emp.getSalary());
        emp.originalStates.setVersion(emp.getVersion());

        SqlFileUpdateQuery query = new SqlFileUpdateQuery();
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setSqlFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileUpdateQueryTest/testOriginalStates.sql");
        query.setConfig(runtimeConfig);
        query.setEntityAndEntityType("emp", emp, _Emp.getSingletonInternal());
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.addParameter("emp", Emp.class, emp);
        query.prepare();

        UpdateQuery updateQuery = query;
        PreparedSql sql = updateQuery.getSql();
        assertEquals("update aaa set VERSION = ? + 1 where id = ?",
                sql.getRawSql());
        assertEquals("update aaa set VERSION = 100 + 1 where id = 10",
                sql.getFormattedSql());
        List<? extends InParameter<?>> parameters = sql.getParameters();
        assertEquals(2, parameters.size());
        assertEquals(new Integer(100), parameters.get(0).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(1).getWrapper().get());
        assertFalse(query.isExecutable());
    }

    public static class PreUpdate implements EntityType<Emp> {

        protected final _Emp emp;

        public String getQualifiedTableName() {
            return emp.getQualifiedTableName();
        }

        public String getQualifiedTableName(
                Function<String, String> quoteFunction) {
            return emp.getQualifiedTableName(quoteFunction);
        }

        public String getQualifiedTableName(
                BiFunction<NamingType, String, String> namingFunction,
                Function<String, String> quoteFunction) {
            return emp.getQualifiedTableName(namingFunction, quoteFunction);
        }

        public boolean isImmutable() {
            return emp.isImmutable();
        }

        public Emp newEntity(Map<String, Property<Emp, ?>> args) {
            return emp.newEntity(args);
        }

        public Class<Emp> getEntityClass() {
            return emp.getEntityClass();
        }

        public String getName() {
            return emp.getName();
        }

        public List<EntityPropertyType<Emp, ?>> getEntityPropertyTypes() {
            return emp.getEntityPropertyTypes();
        }

        public EntityPropertyType<Emp, ?> getEntityPropertyType(
                String propertyName) {
            return emp.getEntityPropertyType(propertyName);
        }

        public void saveCurrentStates(Emp __entity) {
            emp.saveCurrentStates(__entity);
        }

        public Emp getOriginalStates(Emp entity) {
            return emp.getOriginalStates(entity);
        }

        public GeneratedIdPropertyType<Object, Emp, ?, ?> getGeneratedIdPropertyType() {
            return emp.getGeneratedIdPropertyType();
        }

        public VersionPropertyType<Object, Emp, ?, ?> getVersionPropertyType() {
            return emp.getVersionPropertyType();
        }

        @Override
        public TenantIdPropertyType<Object, Emp, ?, ?> getTenantIdPropertyType() {
            return emp.getTenantIdPropertyType();
        }

        public List<EntityPropertyType<Emp, ?>> getIdPropertyTypes() {
            return emp.getIdPropertyTypes();
        }

        public void preInsert(Emp entity, PreInsertContext<Emp> context) {
            emp.preInsert(entity, context);
        }

        public void preUpdate(Emp entity, PreUpdateContext<Emp> context) {
            entity.setName("hoge");
        }

        public void preDelete(Emp entity, PreDeleteContext<Emp> context) {
            emp.preDelete(entity, context);
        }

        public void postInsert(Emp entity, PostInsertContext<Emp> context) {
            emp.postInsert(entity, context);
        }

        public void postUpdate(Emp entity, PostUpdateContext<Emp> context) {
            emp.postUpdate(entity, context);
        }

        public void postDelete(Emp entity, PostDeleteContext<Emp> context) {
            emp.postDelete(entity, context);
        }

        public String getCatalogName() {
            return emp.getCatalogName();
        }

        public String getSchemaName() {
            return emp.getSchemaName();
        }

        public String getTableName() {
            return emp.getTableName();
        }

        public String getTableName(
                BiFunction<NamingType, String, String> namingFunction) {
            return emp.getTableName(namingFunction);
        }

        public NamingType getNamingType() {
            return emp.getNamingType();
        }

        public boolean isQuoteRequired() {
            return emp.isQuoteRequired();
        }

        public PreUpdate(_Emp emp) {
            super();
            this.emp = emp;
        }

    }
}
