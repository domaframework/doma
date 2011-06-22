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

import java.util.List;

import org.seasar.doma.DomaNullPointerException;

/**
 * SQLの文字列の解析結果です。
 * <p>
 * このインタフェースの実装はスレッドセーフであることは要求されません。
 * <p>
 * このインスタンスのライフサイクルを制御できない場合は参照専用として扱わなければいけません。
 * 
 * @author taedium
 * 
 */
public interface SqlNode {

    /**
     * 子ノードを追加します。
     * 
     * @param child
     *            子ノード
     * @deprecated 1.16.0から非推奨となりました。
     */
    @Deprecated
    void addNode(SqlNode child);

    /**
     * 子ノードのリストを返します。
     * 
     * @return 子ノードのリスト
     */
    List<SqlNode> getChildren();

    /**
     * このノードをコピー(deep copy)します。
     * 
     * @return このノードのコピー
     * @deprecated 1.15.0から非推奨となりました。
     */
    @Deprecated
    SqlNode copy();

    /**
     * ビジターを受け入れ、ビジターの適切なメソッドにディスパッチします。
     * 
     * @param <R>
     *            戻り値の型
     * @param <P>
     *            パラメータの型
     * @param visitor
     *            ビジター
     * @param p
     *            パラメータ
     * @return ビジターで処理された値
     * @throws DomaNullPointerException
     *             ビジターが {@code null} の場合
     */
    <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p);
}
