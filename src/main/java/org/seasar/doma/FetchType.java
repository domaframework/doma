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
 * 検索の結果セットを即時に全て取得するか、遅延で少しづつ取得するかを示します。
 * 
 * @author nakamura-to
 * @since 2.0.0
 */
public enum FetchType {

    /**
     * 即時に全て取得します。
     * <p>
     * メモリの使用量が増え、DBへの接続時間は短くなります。
     */
    EAGER,

    /**
     * 遅延で少しづつ必要なだけを取得します。
     * <p>
     * メモリの使用量は減り、DBへの接続時間は長くなります。
     */
    LAZY;
}
