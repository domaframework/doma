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
package org.seasar.doma.it.util;

import java.io.File;
import java.sql.Statement;

import org.seasar.framework.log.Logger;

/**
 * SQLファイルを実行するクラスです。
 * 
 * @author taedium
 */
public class SqlFileExecutor {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(SqlFileExecutor.class);

    /** 方言 */
    protected Dialect dialect;

    /** SQLファイルのエンコーディング */
    protected String sqlFileEncoding;

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter;

    /** SQLブロックの区切り文字 */
    protected String blockDelimiter;

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     * @param sqlFileEncoding
     *            SQLファイルのエンコーディング
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     * @param blockDelimiter
     *            SQLブロックの区切り文字
     */
    public SqlFileExecutor(Dialect dialect, String sqlFileEncoding,
            char statementDelimiter, String blockDelimiter) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (sqlFileEncoding == null) {
            throw new NullPointerException("sqlFileEncoding");
        }
        this.sqlFileEncoding = sqlFileEncoding;
        this.statementDelimiter = statementDelimiter;
        this.blockDelimiter = blockDelimiter;
        this.dialect = dialect;
    }

    /**
     * 実行します。
     * 
     * @param context
     *            実行コンテキスト
     * @param sqlFile
     *            SQLファイル
     */
    public void execute(SqlExecutionContext context, File sqlFile) {
        logger.debug("executing " + sqlFile.getPath());
        SqlFileReader reader = createSqlFileReader(sqlFile);
        try {
            for (String sql = reader.readSql(); sql != null; sql =
                reader.readSql()) {
                logger.debug(sql);
                Statement statement = context.getStatement();
                try {
                    statement.execute(sql);
                } catch (Exception e) {
                    context.addException(new SqlFailedRuntimeException(
                        e,
                        sqlFile.getPath(),
                        reader.getLineNumber(),
                        sql));
                }
            }
        } finally {
            reader.close();
        }
        logger.debug("executed " + sqlFile.getPath());
    }

    /**
     * SQLファイルのトークナイザを作成します。
     * 
     * @return {@link SqlFileTokenizer}
     */
    protected SqlFileTokenizer createSqlFileTokenizer() {
        return new SqlFileTokenizer(statementDelimiter, blockDelimiter != null
            ? blockDelimiter : dialect.getSqlBlockDelimiter());
    }

    /**
     * SQLファイルのリーダを作成します。
     * 
     * @param sqlFile
     *            SQLファイル
     * 
     * @return {@link SqlFileReader}の実装
     */
    protected SqlFileReader createSqlFileReader(File sqlFile) {
        return new SqlFileReader(
            sqlFile,
            sqlFileEncoding,
            createSqlFileTokenizer(),
            dialect);
    }

}
