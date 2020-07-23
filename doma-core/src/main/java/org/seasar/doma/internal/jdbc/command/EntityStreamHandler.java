package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.SelectQuery;

public class EntityStreamHandler<ENTITY, RESULT> extends AbstractStreamHandler<ENTITY, RESULT> {

  protected final EntityType<ENTITY> entityType;

  public EntityStreamHandler(
      EntityType<ENTITY> entityType, Function<Stream<ENTITY>, RESULT> mapper) {
    super(mapper);
    assertNotNull(entityType);
    this.entityType = entityType;
  }

  @Override
  protected ObjectProvider<ENTITY> createObjectProvider(SelectQuery query) {
    return new EntityProvider<>(entityType, query, query.isResultMappingEnsured());
  }
}
