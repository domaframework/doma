package org.seasar.doma.internal.jdbc.command;

import java.util.stream.Collector;
import org.seasar.doma.jdbc.entity.EntityDesc;

/**
 * @author nakamura-to
 * @param <RESULT>
 * @param <ENTITY>
 */
public class EntityCollectorHandler<ENTITY, RESULT>
    extends AbstractCollectorHandler<ENTITY, RESULT> {

  public EntityCollectorHandler(
      EntityDesc<ENTITY> entityDesc, Collector<ENTITY, ?, RESULT> collector) {
    super(new EntityStreamHandler<>(entityDesc, s -> s.collect(collector)));
  }
}
