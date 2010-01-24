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

import org.seasar.doma.DomaNullPointerException;

/**
 * {@literal REQUIRES_NEW} の属性をもつトランザクションを制御するコントローラです。
 * <p>
 * {@literal REQUIRES_NEW}
 * の属性をもつトランザクションの最適な制御方法は、環境ごとに異なります。たとえば、Seasar2.4を利用している場合、
 * 次のような実装がふさわしいでしょう。
 * 
 * <h5>実装例</h5>
 * 
 * <pre>
 * import org.seasar.doma.jdbc.RequiresNewController;
 * import org.seasar.extension.tx.TransactionCallback;
 * import org.seasar.extension.tx.TransactionManagerAdapter;
 * import org.seasar.framework.container.SingletonS2Container;
 * 
 * public class S2RequiresNewController implements RequiresNewController {
 * 
 *     &#064;SuppressWarnings(&quot;unchecked&quot;)
 *     &#064;Override
 *     public &lt;R&gt; R requiresNew(final Callback&lt;R&gt; callback) throws Throwable {
 *         TransactionManagerAdapter txAdapter = SingletonS2Container
 *                 .getComponent(TransactionManagerAdapter.class);
 *         Object result = txAdapter.requiresNew(new TransactionCallback() {
 * 
 *             public Object execute(final TransactionManagerAdapter adapter)
 *                     throws Throwable {
 *                 return callback.execute();
 *             }
 * 
 *         });
 *         return (R) result;
 *     }
 * }
 * </pre>
 * 
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * <p>
 * 
 * @author taedium
 * 
 */
public interface RequiresNewController {

    /**
     * {@literal REQUIRES_NEW} の属性をもつトランザクションを実行します。
     * 
     * @param <R>
     *            戻り値の型
     * @param callback
     *            {@literal REQUIRES_NEW} のトランザクション属性下で扱いたい処理
     * @return 任意の値
     * @throws DomaNullPointerException
     *             ｛@code callback} が {@code null} の場合
     * @throws Throwable
     *             ｛@code callback} の処理中に何らかの例外が発生した場合
     */
    <R> R requiresNew(Callback<R> callback) throws Throwable;

    /**
     * {@literal REQUIRES_NEW} のトランザクション属性下で実行される処理です。
     * 
     * @author taedium
     * 
     * @param <R>
     *            戻り値の型
     */
    interface Callback<R> {

        /**
         * {@literal REQUIRES_NEW} の属性をもつトランザクション下で実行される処理を起動します。
         * 
         * @return 任意の型
         */
        R execute();
    }
}
