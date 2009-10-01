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
package org.seasar.doma.internal.jdbc.entity;

import org.seasar.doma.wrapper.Wrapper;

/**
 * エンティティのプロパティを表します。
 * 
 * <p>
 * このインタフェースの実装はスレッドセーフであることは要求されません。
 * </p>
 * 
 * @author taedium
 * 
 * @param <W>
 *            ラッパーの型
 */
public interface EntityPropertyType<W extends Wrapper<?>> {

    W getWrapper();

    String getName();

    String getColumnName();

    boolean isId();

    boolean isVersion();

    boolean isInsertable();

    boolean isUpdatable();

    boolean isTransient();

}
