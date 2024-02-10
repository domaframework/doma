package org.seasar.doma.jdbc.criteria.context;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.seasar.doma.DomaException;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.message.Message;

public class InsertContext implements Context {
  public final EntityMetamodel<?> entityMetamodel;
  public final List<EntityMetamodel<?>> entityMetamodels;
  public final Map<Operand.Prop, Operand.Param> values = new LinkedHashMap<>();

  public Optional<DuplicateKeyType> duplicateKeyType = Optional.empty();
  public List<PropertyMetamodel<?>> upsertKeys = Collections.emptyList();
  public List<PropertyMetamodel<?>> upsertSetPropertyMetamodels = Collections.emptyList();
  public final InsertSettings settings = new InsertSettings();
  public SelectContext selectContext;

  public InsertContext(EntityMetamodel<?> entityMetamodel) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entityMetamodels = Collections.singletonList(entityMetamodel);
  }

  @Override
  public List<EntityMetamodel<?>> getEntityMetamodels() {
    return entityMetamodels;
  }

  @Override
  public InsertSettings getSettings() {
    return settings;
  }

  public void validateUpsertFields() {
    this.duplicateKeyType.ifPresent(
        duplicateKeyType -> {
          if (duplicateKeyType == DuplicateKeyType.UPDATE) {
            Objects.requireNonNull(this.upsertKeys);
            if (this.upsertKeys.isEmpty()) {
              throw new DomaException(Message.DOMA6012);
            }
            Objects.requireNonNull(this.upsertSetPropertyMetamodels);
            if (this.upsertSetPropertyMetamodels.isEmpty()) {
              throw new DomaException(Message.DOMA6012);
            }
          } else if (duplicateKeyType == DuplicateKeyType.IGNORE) {
            Objects.requireNonNull(this.upsertKeys);
            if (this.upsertKeys.isEmpty()) {
              throw new DomaException(Message.DOMA6012);
            }
          }
        });
  }
}
