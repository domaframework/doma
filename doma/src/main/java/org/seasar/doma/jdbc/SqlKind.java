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

/**
 * SQLの種別を示します。
 * 
 * @author taedium
 * 
 */
public enum SqlKind {

    /** 検索 */
    SELECT,

    /** 挿入 */
    INSERT,

    /** 更新 */
    UPDATE,

    /** 削除 */
    DELETE,

    /** バッチ挿入 */
    BATCH_INSERT,

    /** バッチ更新 */
    BATCH_UPDATE,

    /** バッチ削除 */
    BATCH_DELETE,

    /** ストアドプロシージャー */
    PROCEDURE,

    /** ストアドファンクション */
    FUNCTION,

    /**
     * スクリプト
     * 
     * @since 1.7.0
     */
    SCRIPT
}
