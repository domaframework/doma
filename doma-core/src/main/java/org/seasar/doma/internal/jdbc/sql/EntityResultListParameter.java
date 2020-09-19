package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.Query;

public class EntityResultListParameter<ENTITY> extends AbstractResultListParameter<ENTITY> {

  final EntityType<ENTITY> entityType;
  final boolean resultMappingEnsured;

  public EntityResultListParameter(EntityType<ENTITY> entityType, boolean resultMappingEnsured) {
    super(new ArrayList<>());
    assertNotNull(entityType);
    this.entityType = entityType;
    this.resultMappingEnsured = resultMappingEnsured;
  }

  @Override
  public List<ENTITY> getResult() {
    return list;
  }

  @Override
  public EntityProvider<ENTITY> createObjectProvider(Query query) {
    return new EntityProvider<>(entityType, query, resultMappingEnsured);
  }
}
