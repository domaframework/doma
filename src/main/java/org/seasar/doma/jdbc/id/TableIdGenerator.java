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
package org.seasar.doma.jdbc.id;

import org.seasar.doma.jdbc.JdbcException;

/**
 * データベースのテーブルを使用するジェネレータです。
 * <p>
 * 
 * @author taedium
 * 
 */
public interface TableIdGenerator extends IdGenerator {

    /**
     * テーブルの完全修飾名を設定します。
     * 
     * @param qualifiedTableName
     *            テーブルの完全修飾名
     */
    void setQualifiedTableName(String qualifiedTableName);

    /**
     * 初期値を設定します。
     * 
     * @param initialValue
     *            初期値
     */
    void setInitialValue(long initialValue);

    /**
     * 割り当てサイズを設定します。
     * 
     * @param allocationSize
     *            割り当てサイズ
     */
    void setAllocationSize(long allocationSize);

    /**
     * 主キーのカラム名を設定します。
     * 
     * @param pkColumnName
     *            主キーのカラム名
     */
    void setPkColumnName(String pkColumnName);

    /**
     * 主キーのカラムの値を設定します。
     * 
     * @param pkColumnValue
     *            主キーのカラムの値
     */
    void setPkColumnValue(String pkColumnValue);

    /**
     * 生成される識別子を保持するカラム名を設定します。
     * 
     * @param valueColumnName
     *            生成される識別子を保持するカラム名
     */
    void setValueColumnName(String valueColumnName);

    /**
     * このジェネレータを初期化します。
     * 
     * @throws JdbcException
     *             初期化に失敗した場合
     */
    void initialize();
}
