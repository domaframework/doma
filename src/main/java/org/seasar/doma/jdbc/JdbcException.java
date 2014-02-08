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

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageResource;

/**
 * JDBCに関する例外です。
 * 
 * @author taedium
 * 
 */
public class JdbcException extends DomaException {

    private static final long serialVersionUID = 1L;

    /**
     * インスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            メッセージへの引数
     */
    public JdbcException(MessageResource messageCode, Object... args) {
        super(messageCode, args);
    }

    /**
     * この例外の原因となった {@link Throwable} を指定してインスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param cause
     *            原因
     * @param args
     *            メッセージへの引数
     */
    public JdbcException(MessageResource messageCode, Throwable cause,
            Object... args) {
        super(messageCode, cause, args);
    }

    /**
     * ログタイプに応じてログ用SQLを選択します。
     * 
     * @param logType
     *            ログタイプ
     * @param rawSql
     *            未加工SQL
     * @param formattedSql
     *            フォーマット済みSQL
     * @return ログ用SQL
     * @since 1.22.0
     */
    protected static String choiceSql(ExceptionSqlLogType logType,
            String rawSql, String formattedSql) {
        switch (logType) {
        case RAW_SQL:
            return rawSql;
        case FORMATTED_SQL:
            return formattedSql;
        case EMPTY:
            return "";
        default:
            throw new AssertionError("unreachable");
        }
    }
}
