package org.seasar.doma.jdbc.criteria.query;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.context.Settings;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

class AliasManagerTest {
  private final Emp_ e = new Emp_();
  private final Dept_ d = new Dept_();
  private final Emp_ e2 = new Emp_();
  private final Dept_ d2 = new Dept_();

  private static class ContextImpl implements Context {

    private final List<EntityMetamodel<?>> entityMetamodels;

    public ContextImpl(List<EntityMetamodel<?>> entityMetamodels) {
      this.entityMetamodels = entityMetamodels;
    }

    @Override
    public List<EntityMetamodel<?>> getEntityMetamodels() {
      return entityMetamodels;
    }

    @Override
    public Settings getSettings() {
      throw new UnsupportedOperationException();
    }
  }

  @Test
  void entityDef() {
    AliasManager manager = new AliasManager(new ContextImpl(Arrays.asList(e, d)));
    Assertions.assertEquals("t0_", manager.getAlias(e));
    Assertions.assertEquals("t1_", manager.getAlias(d));
  }

  @Test
  void entityDef_notFound() {
    AliasManager manager = new AliasManager(new ContextImpl(Arrays.asList(e, d)));
    Assertions.assertNull(manager.getAlias(e2));
  }

  @Test
  void propertyMetamodel() {
    AliasManager manager = new AliasManager(new ContextImpl(Arrays.asList(e, d)));
    Assertions.assertEquals("t0_", manager.getAlias(e.name));
    Assertions.assertEquals("t1_", manager.getAlias(d.name));
  }

  @Test
  void propertyMetamodel_notFound() {
    AliasManager manager = new AliasManager(new ContextImpl(Arrays.asList(e, d)));
    Assertions.assertNull(manager.getAlias(e2.name));
  }

  @Test
  void entityMetamodel_parent() {
    AliasManager parent = new AliasManager(new ContextImpl(Arrays.asList(e, d)));
    AliasManager child = new AliasManager(new ContextImpl(Arrays.asList(e2, d2)), parent);
    Assertions.assertEquals("t0_", child.getAlias(e));
    Assertions.assertEquals("t1_", child.getAlias(d));
    Assertions.assertEquals("t2_", child.getAlias(e2));
    Assertions.assertEquals("t3_", child.getAlias(d2));
  }

  @Test
  void propertyMetamodel_parent() {
    AliasManager parent = new AliasManager(new ContextImpl(Arrays.asList(e, d)));
    AliasManager child = new AliasManager(new ContextImpl(Arrays.asList(e2, d2)), parent);
    Assertions.assertEquals("t0_", child.getAlias(e.name));
    Assertions.assertEquals("t1_", child.getAlias(d.name));
    Assertions.assertEquals("t2_", child.getAlias(e2.name));
    Assertions.assertEquals("t3_", child.getAlias(d2.name));
  }
}
