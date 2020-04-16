package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.Query;

public class EntityListParameter<ENTITY> extends AbstractListParameter<ENTITY> {

  protected final EntityType<ENTITY> entityType;

  protected final boolean resultMappingEnsured;

  public EntityListParameter(
      EntityType<ENTITY> entityType, List<ENTITY> list, String name, boolean resultMappingEnsured) {
    super(list, name);
    assertNotNull(entityType);
    this.entityType = entityType;
    this.resultMappingEnsured = resultMappingEnsured;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public EntityProvider<ENTITY> createObjectProvider(Query query) {
    return new EntityProvider<>(entityType, query, resultMappingEnsured);
  }
}
