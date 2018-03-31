package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.query.Query;

/** @author taedium */
public class EntityListParameter<ENTITY> extends AbstractListParameter<ENTITY> {

  protected final EntityDesc<ENTITY> entityDesc;

  protected final boolean resultMappingEnsured;

  public EntityListParameter(
      EntityDesc<ENTITY> entityDesc, List<ENTITY> list, String name, boolean resultMappingEnsured) {
    super(list, name);
    assertNotNull(entityDesc);
    this.entityDesc = entityDesc;
    this.resultMappingEnsured = resultMappingEnsured;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public EntityProvider<ENTITY> createObjectProvider(Query query) {
    return new EntityProvider<>(entityDesc, query, resultMappingEnsured);
  }
}
