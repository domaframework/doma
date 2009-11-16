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

import org.seasar.doma.MessageCode;
import org.seasar.doma.internal.message.DomaMessageCode;

/**
 * 楽観的排他制御に失敗した場合にスローされます。
 * <p>
 * 楽観的排他制御に失敗したとみなす条件は、バージョン番号を問い合わせ条件に含めた更新処理や削除処理で更新結果が1件でない場合です。
 * 
 * @author taedium
 * 
 */
public class OptimisticLockException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** 未加工SQL */
    protected final String rawSql;

    /** フォーマット済みSQL、バッチ処理時にスローされた場合 {@code null} */
    protected final String formattedSql;

    /** SQLファイルのパス、SQLが自動生成された場合 {@code null} */
    protected final String sqlFilePath;

    /**
     * 楽観的排他制御に失敗したSQLを指定してインスタンスを構築します。
     * 
     * @param sql
     *            SQL
     * @param sqlFilePath
     *            SQLファイルのパス
     */
    public OptimisticLockException(Sql<?> sql, String sqlFilePath) {
        this(sql.getRawSql(), sql.getFormattedSql(), sqlFilePath);
    }

    /**
     * 楽観的排他制御に失敗した未加工SQLとフォーマット済みSQLを指定してインスタンスを構築します。
     * 
     * @param rawSql
     *            未加工SQL
     * @param formattedSql
     *            フォーマット済みSQL
     * @param sqlFilePath
     *            SQLファイルのパス
     */
    public OptimisticLockException(String rawSql, String formattedSql,
            String sqlFilePath) {
        super(DomaMessageCode.DOMA2003, sqlFilePath, formattedSql);
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
        this.sqlFilePath = sqlFilePath;
    }

    /**
     * メッセージコードと未加工SQLを指定してサブクラスから呼び出しインスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param rawSql
     *            未加工SQL
     * @param sqlFilePath
     *            SQLファイルのパス
     */
    protected OptimisticLockException(MessageCode messageCode, String rawSql,
            String sqlFilePath) {
        super(messageCode, sqlFilePath, rawSql);
        this.rawSql = rawSql;
        this.formattedSql = null;
        this.sqlFilePath = sqlFilePath;
    }

    /**
     * 未加工SQLを返します。
     * 
     * @return 未加工SQL
     */
    public String getRawSql() {
        return rawSql;
    }

    /**
     * フォーマット済みSQLを返します。
     * 
     * @return フォーマット済みSQL、存在しない場合 {@code null}
     */
    public String getFormattedSql() {
        return formattedSql;
    }

    /**
     * SQLファイルのパスを返します。
     * 
     * @return SQLファイルのパス、SQLが自動生成された場合 {@code null}
     */
    public String getSqlFilePath() {
        return sqlFilePath;
    }

}
