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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.util.Resources;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class BuiltinSqlFileRepository implements SqlFileRepository {

    protected static final String SQL_EXTENSION = ".sql";

    protected final ConcurrentMap<String, SqlFile> sqlFileMap = new ConcurrentHashMap<String, SqlFile>(
            200);

    public SqlFile getSqlFile(String path, Dialect dialect) {
        if (path == null) {
            throw new DomaIllegalArgumentException("path", path);
        }
        if (!path.endsWith(SQL_EXTENSION)) {
            throw new DomaIllegalArgumentException("path", path);
        }
        if (dialect == null) {
            throw new DomaIllegalArgumentException("dialect", dialect);
        }
        SqlFile file = sqlFileMap.get(path);
        if (file != null) {
            return file;
        }
        file = createSqlFile(path, dialect);
        SqlFile current = sqlFileMap.putIfAbsent(path, file);
        return current != null ? current : file;
    }

    protected SqlFile createSqlFile(String path, Dialect dialect) {
        String primaryPath = getPrimaryPath(path, dialect);
        String sql = getSql(primaryPath);
        if (sql != null) {
            SqlNode sqlNode = createSqlNode(sql);
            return new SqlFile(primaryPath, sql, sqlNode);
        }
        sql = getSql(path);
        if (sql != null) {
            SqlNode sqlNode = createSqlNode(sql);
            return new SqlFile(path, sql, sqlNode);
        }
        throw new SqlFileNotFoundException(path);
    }

    protected String getPrimaryPath(String path, Dialect dialect) {
        String name = dialect.getName();
        return path.substring(0, path.length() - SQL_EXTENSION.length()) + "_"
                + name + SQL_EXTENSION;
    }

    protected SqlNode createSqlNode(String sql) {
        SqlParser parser = new SqlParser(sql);
        return parser.parse();
    }

    protected String getSql(String path) {
        try {
            return Resources.getResourceAsString(path);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new JdbcException(MessageCode.DOMA2010, cause, path, cause);
        }
    }

}
