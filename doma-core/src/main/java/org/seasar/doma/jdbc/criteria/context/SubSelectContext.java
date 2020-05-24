package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public interface SubSelectContext<RESULT> {

  SelectContext get();

  class Single<PROPERTY> implements SubSelectContext<PropertyMetamodel<PROPERTY>> {
    private final SelectContext context;
    private final PropertyMetamodel<PROPERTY> propertyMetamodel;

    public Single(SelectContext context, PropertyMetamodel<PROPERTY> propertyMetamodel) {
      this.context = Objects.requireNonNull(context);
      this.propertyMetamodel = Objects.requireNonNull(propertyMetamodel);
    }

    @Override
    public SelectContext get() {
      return context;
    }

    public PropertyMetamodel<PROPERTY> getPropertyMetamodel() {
      return propertyMetamodel;
    }
  }
}
