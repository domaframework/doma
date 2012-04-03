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

import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * 例外メッセージに含めるSQLログのタイプです。
 * 
 * @author taedium
 * @since 1.22.0
 */
public enum ExceptionSqlLogType {

    /**
     * 未加工SQL。
     * 
     * <p>
     * SQLが例外メッセージに含まれます。SQL中のバインドパラメータは {@code ?} で表されます。
     */
    RAW_SQL,

    /**
     * フォーマット済みSQL。
     * 
     * <p>
     * SQLが例外メッセージに含まれます。 SQL中のバインドパラメータはフォーマットされて表されます。 フォーマットには
     * {@link Dialect#getSqlLogFormattingVisitor()} が返すオブジェクトが使用されます。
     */
    FORMATTED_SQL,

    /**
     * 空文字。
     * 
     * <p>
     * SQLは例外メッセージに含まれません。
     */
    EMPTY
}
