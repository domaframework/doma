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
package org.seasar.doma.jdbc;

import junit.framework.TestCase;

import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

/**
 * @author taedium
 * 
 */
public class GreedyCacheSqlFileRepositoryTest extends TestCase {

    public void testGetSqlFile() throws Exception {
        StandardDialect dialect = new StandardDialect();
        String path = "META-INF/" + getClass().getName().replace(".", "/")
                + ".sql";
        GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
        SqlFile sqlFile = repository.getSqlFile(path, dialect);
        assertNotNull(sqlFile);
        SqlFile sqlFile2 = repository.getSqlFile(path, dialect);
        assertSame(sqlFile, sqlFile2);
        assertEquals(path, sqlFile.getPath());
    }

    public void testGetSqlFile_oracle() throws Exception {
        OracleDialect dialect = new OracleDialect();
        String path = "META-INF/" + getClass().getName().replace(".", "/")
                + ".sql";
        GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
        SqlFile sqlFile = repository.getSqlFile(path, dialect);
        assertEquals("META-INF/" + getClass().getName().replace(".", "/")
                + "-oracle.sql", sqlFile.getPath());
    }

    public void testGetSqlFile_postgres() throws Exception {
        PostgresDialect dialect = new PostgresDialect();
        String path = "META-INF/" + getClass().getName().replace(".", "/")
                + ".sql";
        GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
        SqlFile sqlFile = repository.getSqlFile(path, dialect);
        assertEquals(path, sqlFile.getPath());
    }
}
