package org.seasar.doma.jdbc.criteria;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.DeleteSettings;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SelectSettings;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.context.UpdateSettings;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.statement.SetOperand;
import org.seasar.doma.jdbc.criteria.statement.UnifiedDeleteStarting;
import org.seasar.doma.jdbc.criteria.statement.UnifiedInsertStarting;
import org.seasar.doma.jdbc.criteria.statement.UnifiedSelectStarting;
import org.seasar.doma.jdbc.criteria.statement.UnifiedUpdateStarting;

public class QueryDsl {
  private final Config config;

  public QueryDsl(Config config) {
    this.config = Objects.requireNonNull(config);
  }

  public <ENTITY> UnifiedSelectStarting<ENTITY> from(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return from(entityMetamodel, null, settings -> {});
  }

  public <ENTITY> UnifiedSelectStarting<ENTITY> from(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<SelectSettings> settingsConsumer) {
    return from(entityMetamodel, null, settingsConsumer);
  }

  public <ENTITY> UnifiedSelectStarting<ENTITY> from(
      EntityMetamodel<ENTITY> entityMetamodel, SetOperand<?> setOperandForSubQuery) {
    return from(entityMetamodel, setOperandForSubQuery, settings -> {});
  }

  public <ENTITY> UnifiedSelectStarting<ENTITY> from(
      EntityMetamodel<ENTITY> entityMetamodel,
      SetOperand<?> setOperandForSubQuery,
      Consumer<SelectSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    SetOperationContext<?> setOperationContextForSubQuery =
        setOperandForSubQuery == null ? null : setOperandForSubQuery.getContext();
    SelectContext context =
        new SelectContext(entityMetamodel, Optional.ofNullable(setOperationContextForSubQuery));
    settingsConsumer.accept(context.getSettings());
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    return new UnifiedSelectStarting<>(config, declaration, entityMetamodel);
  }

  public <ENTITY> UnifiedUpdateStarting<ENTITY> update(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return update(entityMetamodel, settings -> {});
  }

  public <ENTITY> UnifiedUpdateStarting<ENTITY> update(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<UpdateSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    UpdateSettings settings = new UpdateSettings();
    settingsConsumer.accept(settings);
    return new UnifiedUpdateStarting<>(config, entityMetamodel, settings);
  }

  public <ENTITY> UnifiedDeleteStarting<ENTITY> delete(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return delete(entityMetamodel, settings -> {});
  }

  public <ENTITY> UnifiedDeleteStarting<ENTITY> delete(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<DeleteSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    DeleteSettings settings = new DeleteSettings();
    settingsConsumer.accept(settings);
    return new UnifiedDeleteStarting<>(config, entityMetamodel, settings);
  }

  public <ENTITY> UnifiedInsertStarting<ENTITY> insert(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return insert(entityMetamodel, settings -> {});
  }

  public <ENTITY> UnifiedInsertStarting<ENTITY> insert(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<InsertSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    InsertSettings settings = new InsertSettings();
    settingsConsumer.accept(settings);
    return new UnifiedInsertStarting<>(config, entityMetamodel, settings);
  }
}
