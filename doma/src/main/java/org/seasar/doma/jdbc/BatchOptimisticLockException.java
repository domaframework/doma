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

import org.seasar.doma.message.Message;

/**
 * バッチ処理時に楽観的排他制御に失敗した場合にスローされる例外です。
 * <p>
 * {@link #getFormattedSql()}は {@code null} を返します。
 * 
 * @author taedium
 * 
 */
public class BatchOptimisticLockException extends OptimisticLockException {

    private static final long serialVersionUID = 1L;

    /**
     * 楽観的排他制御に失敗したSQLを指定してインスタンスを構築します。
     * 
     * @param logType
     *            ログタイプ
     * @param sql
     *            SQL
     */
    public BatchOptimisticLockException(ExceptionSqlLogType logType, Sql<?> sql) {
        this(logType, sql.getKind(), sql.getRawSql(), sql.getSqlFilePath());
    }

    /**
     * 楽観的排他制御に失敗した未加工SQLを指定してインスタンスを構築します。 * @param kind SQLの種別 * @param rawSql
     * 未加工SQL
     * 
     * @param logType
     *            ログタイプ
     * @param kind
     *            SQLの種別
     * @param sqlFilePath
     *            SQLファイルのパス
     */
    public BatchOptimisticLockException(ExceptionSqlLogType logType,
            SqlKind kind, String rawSql, String sqlFilePath) {
        super(Message.DOMA2028, kind, choiceSql(logType, rawSql, rawSql),
                sqlFilePath);
    }

}
