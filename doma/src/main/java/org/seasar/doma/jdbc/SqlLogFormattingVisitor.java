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

import org.seasar.doma.wrapper.Wrapper;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * SQLのバインド変数にマッピングされる {@link Wrapper} をログ用のフォーマットされた文字列へと変換する
 * {@link WrapperVisitor} の拡張です。
 * <p>
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * 
 * @author taedium
 * 
 */
public interface SqlLogFormattingVisitor extends
        WrapperVisitor<String, SqlLogFormattingFunction, RuntimeException> {
}
