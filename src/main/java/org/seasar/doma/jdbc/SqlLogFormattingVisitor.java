package org.seasar.doma.jdbc;

import org.seasar.doma.wrapper.Wrapper;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * SQLのバインド変数にマッピングされる {@link Wrapper} をログ用のフォーマットされた文字列へと変換する {@link WrapperVisitor} の拡張です。
 *
 * <p>このインタフェースの実装はスレッドセーフでなければいけません。
 *
 * @author taedium
 */
public interface SqlLogFormattingVisitor
    extends WrapperVisitor<String, SqlLogFormattingFunction, Void, RuntimeException> {}
