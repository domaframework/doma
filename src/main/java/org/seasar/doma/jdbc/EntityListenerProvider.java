package org.seasar.doma.jdbc;

import java.util.function.Supplier;
import org.seasar.doma.jdbc.entity.EntityListener;

/**
 * A provider for an {@link EntityListener} object.
 *
 * @author backpaper0
 */
public interface EntityListenerProvider {

  /**
   * Returns an {@link EntityListener} object.
   *
   * <p>This method must not return {@code null}.
   *
   * @param listenerClass the implementation class of {@link EntityListener}
   * @param listenerSupplier the {@link Supplier} object that provides an {@link EntityListener}
   *     object
   * @param <ENTITY> the entity type
   * @param <LISTENER> the entity listener type
   * @return an {@link EntityListener} object
   */
  default <ENTITY, LISTENER extends EntityListener<ENTITY>> LISTENER get(
      Class<LISTENER> listenerClass, Supplier<LISTENER> listenerSupplier) {
    return listenerSupplier.get();
  }
}
