package org.seasar.doma.jdbc.criteria.context;

import java.util.List;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public interface Context {

  List<EntityMetamodel<?>> getEntityMetamodels();

  List<Criterion> getWhere();

  void setWhere(List<Criterion> where);

  Settings getSettings();
}
