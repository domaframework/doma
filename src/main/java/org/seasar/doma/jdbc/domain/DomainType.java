package org.seasar.doma.jdbc.domain;

import java.util.Optional;
import org.seasar.doma.internal.jdbc.scalar.Scalar;

/**
 * ドメイン型のメタタイプです。
 *
 * <p>このインタフェースの実装はスレッドセーフであることは要求されません。
 *
 * @author taedium
 * @since 1.8.0
 * @param <BASIC> ドメイン型が扱う基本型
 * @param <DOMAIN> ドメイン型
 */
public interface DomainType<BASIC, DOMAIN> {

  /**
   * 基本型のクラスを返します。
   *
   * <p>プリミティブ型を返す場合があるため、戻り値の型は {@code Class<BASIC>} ではなく {@code Class<?>} となっています。
   *
   * @return 基本型のクラス
   */
  Class<?> getBasicClass();

  /**
   * ドメイン型のクラスを返します。
   *
   * @return ドメイン型のクラス
   */
  Class<DOMAIN> getDomainClass();

  /**
   * スカラーを作成します。
   *
   * @return スカラー
   * @since 2.0.0
   */
  Scalar<BASIC, DOMAIN> createScalar();

  /**
   * 初期値を持ったスカラーを作成します。
   *
   * @param value 初期値
   * @return スカラー
   * @since 2.0.0
   */
  Scalar<BASIC, DOMAIN> createScalar(DOMAIN value);

  /**
   * {@link Optional} なスカラーを作成します。
   *
   * @return スカラー
   * @since 2.0.0
   */
  Scalar<BASIC, Optional<DOMAIN>> createOptionalScalar();

  /**
   * 初期値を持った {@link Optional} なスカラーを作成します。
   *
   * @param value 初期値
   * @return スカラー
   * @since 2.0.0
   */
  Scalar<BASIC, Optional<DOMAIN>> createOptionalScalar(DOMAIN value);
}
