package org.seasar.doma.jdbc;

import java.util.Optional;

/**
 * マッピングに関するヒント情報です。
 *
 * <p>{@link JdbcMappingVisitor} の実装クラスにおいて、次のマッピングのカスタマイズに利用できます。
 *
 * <ul>
 *   <li>JavaのクラスからSQLの型へのマッピング（JavaからSQLのバインド変数へのマッピング）
 *   <li>SQLの型からJavaのクラスへのマッピング（SQLの結果セットからJavaのマッピング）
 * </ul>
 *
 * @author nakamura-to
 */
public interface JdbcMappingHint {

  /**
   * ドメインクラスにマッピングされている場合、そのドメインクラスを返します。
   *
   * @return ドメインクラス
   */
  Optional<Class<?>> getDomainClass();
}
