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
package org.seasar.doma.jdbc.entity;

import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcMappable;

/**
 * プロパティです。
 * 
 * @author nakamura-to
 * 
 * @param <ENTITY>
 *            エンティティの型
 * @param <BASIC>
 *            基本型
 * @since 2.0.0
 */
public interface Property<ENTITY, BASIC> extends JdbcMappable<BASIC> {

    /**
     * プロパティの値を返します。
     * 
     * @return プロパティの値
     */
    Object get();

    /**
     * エンティティからこのインスタンスへ値を読み込みます。
     * 
     * @param entity
     *            エンティティ
     * @return このインスタンス
     */
    Property<ENTITY, BASIC> load(ENTITY entity);

    /**
     * エンティティへこのインスタンスが保持する値を保存します。
     * 
     * @param entity
     *            エンティティ
     * @return このインスタンス
     */
    Property<ENTITY, BASIC> save(ENTITY entity);

    /**
     * このプロパティを入力用パラメータとして返します。
     * 
     * @return 入力用パラメータ
     * @since 2.4.0
     */
    InParameter<BASIC> asInParameter();

}
