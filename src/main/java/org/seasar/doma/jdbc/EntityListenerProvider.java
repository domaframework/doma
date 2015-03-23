package org.seasar.doma.jdbc;

import java.util.function.Supplier;

import org.seasar.doma.jdbc.entity.EntityListener;

/**
 * {@link EntityListener} のプロバイダです。
 *
 * @author backpaper0
 * @since 2.2.0
 */
public interface EntityListenerProvider {

    /**
     * {@link EntityListener} のインスタンスを取得します。
     * <p>
     * デフォルトの実装では単純に {@link Supplier#get()} を実行して取得したインスタンスを返します。
     * 
     * {@link EntityListener} をDIコンテナで管理したい場合などはこのメソッドをオーバーライドし、
     * DIコンテナから取得したインスタンスを返すようにしてください。
     * 
     * このメソッドは{@code null}を返してはいけません。
     * 
     * @param listenerClass
     *            {@link EntityListener} の実装クラス
     * @param listenerSupplier
     *            {@link EntityListener} のインスタンスを返す {@link Supplier}
     * @param <ENTITY>
     *            エンティティの型
     * @param <LISTENER>
     *            リスナーの型
     * @return {@link EntityListener} のインスタンス
     */
    default <ENTITY, LISTENER extends EntityListener<ENTITY>> LISTENER get(
            Class<LISTENER> listenerClass, Supplier<LISTENER> listenerSupplier) {
        return listenerSupplier.get();
    }
}
