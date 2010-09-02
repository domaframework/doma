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

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.message.Message;

/**
 * {@link SqlFileRepository} の骨格実装です。
 * <p>
 * SQLファイルの解析結果をキャッシュするには {@link #getSqlFileWithCacheControl(String, Dialect)}
 * を実装してください。
 * 
 * @author taedium
 * 
 */
public abstract class AbstractSqlFileRepository implements SqlFileRepository {

    @Override
    public final SqlFile getSqlFile(String path, Dialect dialect) {
        if (path == null) {
            throw new DomaNullPointerException("path");
        }
        if (!path.startsWith(Constants.SQL_PATH_PREFIX)) {
            throw new DomaIllegalArgumentException("path",
                    "The path does not start with '"
                            + Constants.SQL_PATH_PREFIX + "'");
        }
        if (!path.endsWith(Constants.SQL_PATH_SUFFIX)) {
            throw new DomaIllegalArgumentException("path",
                    "The path does not end with '" + Constants.SQL_PATH_SUFFIX
                            + "'");
        }
        if (dialect == null) {
            throw new DomaNullPointerException("dialect");
        }
        return getSqlFileWithCacheControl(path, dialect);
    }

    /**
     * キャッシュを制御してSQLファイルを返します。
     * 
     * @param path
     *            SQLファイルのパス
     * @param dialect
     *            方言
     * @return SQLファイル
     * @throws SqlFileNotFoundException
     *             SQLファイルが見つからない場合
     * @throws JdbcException
     *             上記以外で例外が発生した場合
     */
    protected abstract SqlFile getSqlFileWithCacheControl(String path,
            Dialect dialect);

    /**
     * SQLファイルを作成します。
     * 
     * @param path
     *            SQLのパス
     * @param dialect
     *            方言
     * @return SQLファイル
     */
    protected final SqlFile createSqlFile(String path, Dialect dialect) {
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
    protected final String getPrimaryPath(String path, Dialect dialect) {
        return SqlFileUtil.convertToDbmsSpecificPath(path, dialect);
    }

    /**
     * SQLを解析します。
     * 
     * @param sql
     *            SQLの文字列
     * @return SQLの解析結果
     */
    protected final SqlNode parse(String sql) {
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
    protected final String getSql(String path) {
        try {
            return ResourceUtil.getResourceAsString(path);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new JdbcException(Message.DOMA2010, cause, path, cause);
        }
    }

}
