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

/**
 * SQLのコメンターです。
 *
 * <p>この処理はSQLテンプレートの処理が完了した後で行われます。
 *
 * <p>このインタフェースでは、バインド変数を追加するなど、コメントを追加する以外のことを実行してはいけません。
 *
 * @author nakamura-to
 * @since 2.1.0
 */
public interface Commenter {

  /**
   * SQLにコメントを追記します。
   *
   * <p>デフォルトでは何も行いません。
   *
   * @param sql SQL
   * @param context コンテキスト
   * @return コメントが追記されたSQL
   */
  default String comment(String sql, CommentContext context) {
    return sql;
  }
}
