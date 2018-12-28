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

import org.seasar.doma.internal.WrapException;

/**
 * クラスのヘルパーです。
 *
 * <p>クラスの扱いに関してアプリケーションサーバやフレームワークの差異を抽象化します。
 *
 * @author taedium
 * @since 1.27.0
 */
public interface ClassHelper {

  /**
   * 指定された文字列名を持つクラスまたはインタフェースに関連付けられた、{@link Class} オブジェクトを返します。
   *
   * @param <T> クラスの型
   * @param className 要求するクラスの完全指定の名前
   * @return 指定された名前を持つクラスの {@link Class} オブジェクト
   * @throws Exception 例外
   * @see Class#forName(String)
   */
  @SuppressWarnings("unchecked")
  default <T> Class<T> forName(String className) throws Exception {
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader == null) {
        return (Class<T>) Class.forName(className);
      } else {
        try {
          return (Class<T>) classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
          return (Class<T>) Class.forName(className);
        }
      }
    } catch (ClassNotFoundException e) {
      throw new WrapException(e);
    }
  }
}
