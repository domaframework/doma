package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.query.Query;

public class EntityResultListParameter<ENTITY> extends AbstractResultListParameter<ENTITY> {

  final EntityDesc<ENTITY> entityDesc;
  final boolean resultMappingEnsured;

  public EntityResultListParameter(EntityDesc<ENTITY> entityDesc, boolean resultMappingEnsured) {
    super(new ArrayList<>());
    assertNotNull(entityDesc);
    this.entityDesc = entityDesc;
    this.resultMappingEnsured = resultMappingEnsured;
  }

  @Override
  public List<ENTITY> getResult() {
    return list;
  }

  @Override
  public EntityProvider<ENTITY> createObjectProvider(Query query) {
    return new EntityProvider<>(entityDesc, query, resultMappingEnsured);
  }
}
