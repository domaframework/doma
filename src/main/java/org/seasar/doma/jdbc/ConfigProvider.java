package org.seasar.doma.jdbc;

/**
 * 設定のプロバイダです。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public interface ConfigProvider {

  /**
   * 設定を返します。
   *
   * @return 設定
   */
  Config getConfig();
}
