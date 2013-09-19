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

import java.util.List;

import org.seasar.doma.message.Message;

/**
 * エンティティのすべてのプロパティに対し、結果セットのカラムがマッピングされない場合にスローされます。
 * 
 * @author taedium
 * @since 1.34.0
 */
public class ResultMappingException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** マッピング対象のエンティティクラスの名前 */
    protected final String entityClassName;

    /** マッピングされなかったプロパティの名前のリスト */
    protected final List<String> unmappedPropertyNames;

    /** 期待されるカラムの名前のリスト */
    protected final List<String> expectedColumnNames;

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
     * @param entityClassName
     *            マッピング対象のエンティティクラスの名前
     * @param unmappedPropertyNames
     *            マッピングされなかったプロパティの名前のリスト
     * @param expectedColumnNames
     *            期待されるカラムの名前のリスト
     * @param kind
     *            SQLの種別
     * @param rawSql
     *            未加工SQL
     * @param formattedSql
     *            フォーマット済みSQL
     * @param sqlFilePath
     *            SQLファイルのパス
     */
    public ResultMappingException(ExceptionSqlLogType logType,
            String entityClassName, List<String> unmappedPropertyNames,
            List<String> expectedColumnNames, SqlKind kind, String rawSql,
            String formattedSql, String sqlFilePath) {
        super(Message.DOMA2216, entityClassName, unmappedPropertyNames,
                expectedColumnNames, sqlFilePath, choiceSql(logType, rawSql,
                        formattedSql));
        this.entityClassName = entityClassName;
        this.unmappedPropertyNames = unmappedPropertyNames;
        this.expectedColumnNames = expectedColumnNames;
        this.kind = kind;
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
        this.sqlFilePath = sqlFilePath;
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
     * マッピングされなかったプロパティの名前のリストを返します。
     * 
     * @return マッピングされなかったプロパティの名前のリスト
     */
    public List<String> getUnmappedPropertyNames() {
        return unmappedPropertyNames;
    }

    /**
     * 期待されるカラムの名前のリストを返します。
     * 
     * @return 期待されるカラムの名前のリスト
     */
    public List<String> getExpectedColumnNames() {
        return expectedColumnNames;
    }

    /**
     * SQLの種別を返します。
     * 
     * @return SQLの種別
     */
    public SqlKind getKind() {
        return kind;
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
