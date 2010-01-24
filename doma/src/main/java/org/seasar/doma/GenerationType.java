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
package org.seasar.doma;

/**
 * 識別子を生成する方法です。
 * 
 * @author taedium
 * @see GeneratedValue
 */
public enum GenerationType {

    /** データベースのIDENTITYカラムを使って識別子を自動生成することを示します。 */
    IDENTITY,

    /** データベースのシーケンスを使って識別子を自動生成することを示します。 */
    SEQUENCE,

    /** データベースのテーブルを使って識別子を自動生成することを示します。 */
    TABLE
}
