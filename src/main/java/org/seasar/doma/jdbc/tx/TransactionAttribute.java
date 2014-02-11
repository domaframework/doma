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
package org.seasar.doma.jdbc.tx;

/**
 * トランザクション属性を示します。
 * 
 * @author nakamura-to
 * @since 2.0.0
 */
public enum TransactionAttribute {

    /**
     * 既存のトランザクションが存在する場合はそのトランザクションに参加し、存在しない場合は新規のトランザクションを作成します。
     */
    REQURED,

    /**
     * 新規のトランザクションを作成します。
     * <p>
     * 既存のトランザクションが存在する場合、そのトランザクションを中断し後で再開させます。
     */
    REQURES_NEW,

    /**
     * トランザクションに参加しません。
     * <p>
     * 既存のトランザクションが存在する場合、そのトランザクションを中断し後で再開させます。
     */
    NOT_SUPPORTED
}
