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
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.message.DomaMessageCode;

/**
 * SQLの解析結果を無制限にキャッシュする {@link SqlFileRepository} の実装です。
 * 
 * @author taedium
 * 
 */
public class CachedSqlFileRepository implements SqlFileRepository {

    /** SQLのパスのプレフィックスです。 */
    protected static final String SQL_PATH_PREFIX = "META-INF/";

    /** SQLのパスのサフィックスです。 */
    protected static final String SQL_PATH_SUFFIX = ".sql";

    /** SQLのパスをキー、SQLファイルを値とするマップです。 */
    protected final ConcurrentMap<String, SqlFile> sqlFileMap = new ConcurrentHashMap<String, SqlFile>(
            200);

    public SqlFile getSqlFile(String path, Dialect dialect) {
        if (path == null) {
            throw new DomaNullPointerException("path");
        }
        if (!path.startsWith(SQL_PATH_PREFIX)) {
            throw new DomaIllegalArgumentException("path",
                    "The path does not start with '" + SQL_PATH_PREFIX + "'");
        }
        if (!path.endsWith(SQL_PATH_SUFFIX)) {
            throw new DomaIllegalArgumentException("path",
                    "The path does not end with '" + SQL_PATH_SUFFIX + "'");
        }
        if (dialect == null) {
            throw new DomaNullPointerException("dialect");
        }
        SqlFile file = sqlFileMap.get(path);
        if (file != null) {
            return file;
        }
        file = createSqlFile(path, dialect);
        SqlFile current = sqlFileMap.putIfAbsent(path, file);
        return current != null ? current : file;
    }

    /**
     * SQLファイルを作成します。
     * 
     * @param path
     *            SQLのパス
     * @param dialect
     *            方言
     * @return SQLファイル
     */
    protected SqlFile createSqlFile(String path, Dialect dialect) {
        String primaryPath = getPrimaryPath(path, dialect);
        String sql = getSql(primaryPath);
        if (sql != null) {
            SqlNode sqlNode = parse(sql);
            return new SqlFile(primaryPath, sql, sqlNode);
        }
        sql = getSql(path);
        if (sql != null) {
            SqlNode sqlNode = parse(sql);
            return new SqlFile(path, sql, sqlNode);
        }
        throw new SqlFileNotFoundException(path);
    }

    /**
     * SQLファイルを見つける際の優先パスを取得します。
     * 
     * @param path
     *            SQLのパス
     * @param dialect
     *            方言
     * @return RDBMS固有の名前を含んだSQLのパス
     */
    protected String getPrimaryPath(String path, Dialect dialect) {
        String name = dialect.getName();
        return path.substring(0, path.length() - SQL_PATH_SUFFIX.length())
                + "_" + name + SQL_PATH_SUFFIX;
    }

    /**
     * SQLを解析します。
     * 
     * @param sql
     *            SQLの文字列
     * @return SQLの解析結果
     */
    protected SqlNode parse(String sql) {
        SqlParser parser = new SqlParser(sql);
        return parser.parse();
    }

    /**
     * SQLファイルからSQLを取り出し返します。
     * 
     * @param path
     *            パス
     * @return SQLの文字列
     */
    protected String getSql(String path) {
        try {
            return ResourceUtil.getResourceAsString(path);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new JdbcException(DomaMessageCode.DOMA2010, cause, path,
                    cause);
        }
    }

}
