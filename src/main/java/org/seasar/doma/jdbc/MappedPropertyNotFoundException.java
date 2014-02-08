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
 * 結果セットに含まれたカラムにマッピングされたプロパティが見つからない場合にスローされます。
 * <p>
 * 
 * @author taedium
 * 
 */
public class MappedPropertyNotFoundException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** プロパティにマッピングされなかったカラム名 */
    protected final String columnName;

    /** マッピングを期待されるプロパティの名前 */
    protected final String expectedPropertyName;

    /** マッピング対象のエンティティクラスの名前 */
    protected final String entityClassName;

    /** SQLの種別 */
    protected final SqlKind kind;

    /** 未加工SQL */
    protected final String rawSql;

    /** フォーマット済みSQL、バッチ処理時にスローされた場合 {@code null} */
    protected final String formattedSql;

    /** SQLファイルのパス */
    protected final String sqlFilePath;

    /**
     * インスタンスを構築します。
     * 
     * @param logType
     *            ログタイプ
     * @param columnName
     *            プロパティにマッピングされなかったカラム名
     * @param expectedPropertyName
     *            マッピングを期待されるプロパティの名前
     * @param entityClassName
     *            マッピング対象のエンティティクラスの名前
     * @param kind
     *            SQLの種別
     * @param rawSql
     *            未加工SQL
     * @param formattedSql
     *            フォーマット済みSQL
     * @param sqlFilePath
     *            SQLファイルのパス
     */
    public MappedPropertyNotFoundException(ExceptionSqlLogType logType,
            String columnName, String expectedPropertyName,
            String entityClassName, SqlKind kind, String rawSql,
            String formattedSql, String sqlFilePath) {
        super(Message.DOMA2002, columnName, expectedPropertyName,
                entityClassName, sqlFilePath, choiceSql(logType, rawSql,
                        formattedSql));
        this.columnName = columnName;
        this.expectedPropertyName = expectedPropertyName;
        this.entityClassName = entityClassName;
        this.kind = kind;
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
        this.sqlFilePath = sqlFilePath;
    }

    /**
     * SQLの種別を返します。
     * 
     * @return SQLの種別
     * @since 1.5.0
     */
    public SqlKind getKind() {
        return kind;
    }

    /**
     * マッピング対象のエンティティクラスの名前を返します。
     * 
     * @return マッピング対象のエンティティクラスの名前
     */
    public String getEntityClassName() {
        return entityClassName;
    }

    /**
     * プロパティにマッピングされなかったカラム名を返します。
     * 
     * @return プロパティにマッピングされなかったカラム名
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * マッピングを期待されるプロパティの名前を返します。
     * 
     * @return マッピングを期待されるプロパティの名前
     */
    public String getExpectedPropertyName() {
        return expectedPropertyName;
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
