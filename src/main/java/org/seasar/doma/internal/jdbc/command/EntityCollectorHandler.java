package org.seasar.doma.internal.jdbc.command;

import java.util.stream.Collector;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * @author nakamura-to
 * @param <RESULT>
 * @param <ENTITY>
 */
public class EntityCollectorHandler<ENTITY, RESULT>
    extends AbstractCollectorHandler<ENTITY, RESULT> {

  public EntityCollectorHandler(
      EntityType<ENTITY> entityType, Collector<ENTITY, ?, RESULT> collector) {
    super(new EntityStreamHandler<>(entityType, s -> s.collect(collector)));
  }
}
