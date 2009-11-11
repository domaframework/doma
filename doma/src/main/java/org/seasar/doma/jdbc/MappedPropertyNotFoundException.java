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

import org.seasar.doma.internal.message.DomaMessageCode;

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

    /** マッピング対象のエンティティクラス */
    protected final String entityClassName;

    /** 未加工SQL */
    protected final String rawSql;

    /** フォーマット済みSQL、バッチ処理時にスローされた場合 {@code null} */
    protected final String formattedSql;

    /**
     * インスタンスを構築します。
     * 
     * @param columnName
     *            プロパティにマッピングされなかったカラム名
     * @param entityClassName
     *            マッピング対象のエンティティクラス
     * @param rawSql
     *            未加工SQL
     * @param formattedSql
     *            フォーマット済みSQL
     */
    public MappedPropertyNotFoundException(String columnName,
            String entityClassName, String rawSql, String formattedSql) {
        super(DomaMessageCode.DOMA2002, columnName, entityClassName,
                formattedSql, rawSql);
        this.columnName = columnName;
        this.entityClassName = entityClassName;
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
    }

    /**
     * マッピング対象のエンティティクラスを返します。
     * 
     * @return マッピング対象のエンティティクラス
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

}
