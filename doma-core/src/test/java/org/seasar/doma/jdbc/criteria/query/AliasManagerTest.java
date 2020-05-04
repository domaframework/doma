package org.seasar.doma.jdbc.criteria.query;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.entity.Emp_;

class AliasManagerTest {
  private Emp_ e = new Emp_();
  private Dept_ d = new Dept_();
  private Emp_ e2 = new Emp_();
  private Dept_ d2 = new Dept_();

  private static class ContextImpl implements Context {

    private final List<EntityDef<?>> entityDefs;

    public ContextImpl(List<EntityDef<?>> entityDefs) {
      this.entityDefs = entityDefs;
    }

    @Override
    public List<EntityDef<?>> getEntityDefs() {
      return entityDefs;
    }

    @Override
    public List<Criterion> getWhere() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setWhere(List<Criterion> where) {
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
  void propertyDef() {
    AliasManager manager = new AliasManager(new ContextImpl(Arrays.asList(e, d)));
    Assertions.assertEquals("t0_", manager.getAlias(e.name));
    Assertions.assertEquals("t1_", manager.getAlias(d.name));
  }

  @Test
  void propertyDef_notFound() {
    AliasManager manager = new AliasManager(new ContextImpl(Arrays.asList(e, d)));
    Assertions.assertNull(manager.getAlias(e2.name));
  }

  @Test
  void entityDef_parent() {
    AliasManager parent = new AliasManager(new ContextImpl(Arrays.asList(e, d)));
    AliasManager child = new AliasManager(new ContextImpl(Arrays.asList(e2, d2)), parent);
    Assertions.assertEquals("t0_", child.getAlias(e));
    Assertions.assertEquals("t1_", child.getAlias(d));
    Assertions.assertEquals("t2_", child.getAlias(e2));
    Assertions.assertEquals("t3_", child.getAlias(d2));
  }

  @Test
  void propertyDef_parent() {
    AliasManager parent = new AliasManager(new ContextImpl(Arrays.asList(e, d)));
    AliasManager child = new AliasManager(new ContextImpl(Arrays.asList(e2, d2)), parent);
    Assertions.assertEquals("t0_", child.getAlias(e.name));
    Assertions.assertEquals("t1_", child.getAlias(d.name));
    Assertions.assertEquals("t2_", child.getAlias(e2.name));
    Assertions.assertEquals("t3_", child.getAlias(d2.name));
  }
}
