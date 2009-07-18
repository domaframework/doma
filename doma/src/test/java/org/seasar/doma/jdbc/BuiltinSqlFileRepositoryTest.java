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
package org.seasar.doma.jdbc;

import junit.framework.TestCase;

import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

/**
 * @author taedium
 * 
 */
public class BuiltinSqlFileRepositoryTest extends TestCase {

    public void testGetSqlFile() throws Exception {
        StandardDialect dialect = new StandardDialect();
        String path = getClass().getName().replace(".", "/") + ".sql";
        BuiltinSqlFileRepository repository = new BuiltinSqlFileRepository();
        SqlFile sqlFile = repository.getSqlFile(path, dialect);
        assertNotNull(sqlFile);
        SqlFile sqlFile2 = repository.getSqlFile(path, dialect);
        assertSame(sqlFile, sqlFile2);
        assertEquals(path, sqlFile.getRealPath());
    }

    public void testGetSqlFile_oracle() throws Exception {
        OracleDialect dialect = new OracleDialect();
        String path = getClass().getName().replace(".", "/") + ".sql";
        BuiltinSqlFileRepository repository = new BuiltinSqlFileRepository();
        SqlFile sqlFile = repository.getSqlFile(path, dialect);
        assertEquals(getClass().getName().replace(".", "/") + "_oracle.sql", sqlFile
                .getRealPath());
    }

    public void testGetSqlFile_postgres() throws Exception {
        PostgresDialect dialect = new PostgresDialect();
        String path = getClass().getName().replace(".", "/") + ".sql";
        BuiltinSqlFileRepository repository = new BuiltinSqlFileRepository();
        SqlFile sqlFile = repository.getSqlFile(path, dialect);
        assertEquals(path, sqlFile.getRealPath());
    }
}
